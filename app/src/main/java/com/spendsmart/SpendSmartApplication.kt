package com.spendsmart

import android.app.Application
import com.google.firebase.FirebaseApp
import com.spendsmart.data.local.AppDatabase
import com.spendsmart.data.repository.AuthRepository
import com.spendsmart.data.repository.BudgetRepository
import com.spendsmart.data.repository.CategoryRepository
import com.spendsmart.data.repository.TransactionRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SpendSmartApplication : Application() {

    private val database by lazy { AppDatabase.getDatabase(this) }
    
    val authRepository by lazy { 
        AuthRepository(
            FirebaseAuth.getInstance(),
            FirebaseFirestore.getInstance()
        ) 
    }
    val categoryRepository by lazy { CategoryRepository(database.categoryDao()) }
    val transactionRepository by lazy { 
        TransactionRepository(
            database.transactionDao(), 
            FirebaseFirestore.getInstance(),
            FirebaseAuth.getInstance()
        ) 
    }
    val budgetRepository by lazy {
        BudgetRepository(
            database.budgetDao(),
            FirebaseFirestore.getInstance(),
            FirebaseAuth.getInstance()
        )
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
