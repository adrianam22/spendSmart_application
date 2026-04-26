package com.spendsmart.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.spendsmart.data.local.dao.BudgetDao
import com.spendsmart.data.model.Budget
import com.spendsmart.data.repository.utils.AuthStateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await

class BudgetRepository(
    private val budgetDao: BudgetDao,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    fun getBudget(month: Int, year: Int): Flow<Budget?> {
        return AuthStateFlow(auth).flatMapLatest { user ->
            if (user != null) {
                budgetDao.getBudget(user.uid, month, year)
            } else {
                flowOf(null)
            }
        }
    }

    suspend fun updateBudget(budget: Budget) {
        val userId = auth.currentUser?.uid ?: return
        val budgetWithUser = budget.copy(userId = userId)
        
        budgetDao.insertBudget(budgetWithUser)
        
        try {
            firestore.collection("users").document(userId)
                .collection("budgets").document("${budget.month}_${budget.year}")
                .set(budgetWithUser.copy(isSynced = true))
                .await()
            budgetDao.updateBudget(budgetWithUser.copy(isSynced = true))
        } catch (e: Exception) { }
    }

    suspend fun syncBudgets() {
        val userId = auth.currentUser?.uid ?: return
        
        // 1. Upload unsynced local budgets
        val unsynced = budgetDao.getUnsyncedBudgets()
        unsynced.forEach { budget ->
            try {
                firestore.collection("users").document(userId)
                    .collection("budgets").document("${budget.month}_${budget.year}")
                    .set(budget.copy(isSynced = true))
                    .await()
                budgetDao.updateBudget(budget.copy(isSynced = true))
            } catch (e: Exception) { }
        }

        // 2. Download budgets from Firebase
        try {
            val snapshot = firestore.collection("users").document(userId)
                .collection("budgets").get().await()
            val remoteBudgets = snapshot.toObjects(Budget::class.java)
            remoteBudgets.forEach { 
                budgetDao.insertBudget(it.copy(isSynced = true))
            }
        } catch (e: Exception) { }
    }
}