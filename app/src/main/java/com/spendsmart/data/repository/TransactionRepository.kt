package com.spendsmart.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.spendsmart.data.local.dao.TransactionDao
import com.spendsmart.data.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await

class TransactionRepository(
    private val transactionDao: TransactionDao,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    // Folosim flatMapLatest pentru a schimba fluxul de date automat când se schimbă utilizatorul
    val allTransactions: Flow<List<Transaction>> = com.spendsmart.data.repository.utils.AuthStateFlow(auth).flatMapLatest { user ->
        if (user != null) {
            transactionDao.getTransactionsByUser(user.uid)
        } else {
            flowOf(emptyList())
        }
    }

    suspend fun insert(transaction: Transaction) {
        val userId = auth.currentUser?.uid ?: return
        val transactionWithUser = transaction.copy(userId = userId)
        
        transactionDao.insertTransaction(transactionWithUser)
        
        try {
            firestore.collection("users").document(userId)
                .collection("transactions").document(transactionWithUser.id)
                .set(transactionWithUser.copy(isSynced = true))
                .await()
            transactionDao.updateTransaction(transactionWithUser.copy(isSynced = true))
        } catch (e: Exception) { }
    }

    suspend fun syncWithFirebase() {
        val userId = auth.currentUser?.uid ?: return
        
        // 1. Upload unsynced
        val unsynced = transactionDao.getUnsyncedTransactions()
        unsynced.forEach { transaction ->
            try {
                firestore.collection("users").document(userId)
                    .collection("transactions").document(transaction.id)
                    .set(transaction.copy(isSynced = true))
                    .await()
                transactionDao.updateTransaction(transaction.copy(isSynced = true))
            } catch (e: Exception) { }
        }
        
        // 2. Download from Firebase and update local DB
        try {
            val snapshot = firestore.collection("users").document(userId)
                .collection("transactions").get().await()
            val remoteTransactions = snapshot.toObjects(Transaction::class.java)
            remoteTransactions.forEach { 
                transactionDao.insertTransaction(it.copy(isSynced = true))
            }
        } catch (e: Exception) { }
    }

    suspend fun delete(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users").document(userId)
            .collection("transactions").document(transaction.id)
            .delete()
    }
}