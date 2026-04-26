package com.spendsmart.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.spendsmart.data.repository.AuthRepository
import com.spendsmart.data.repository.BudgetRepository
import com.spendsmart.data.repository.TransactionRepository
import com.spendsmart.ui.viewmodel.AuthViewModel
import com.spendsmart.ui.viewmodel.BudgetViewModel
import com.spendsmart.ui.viewmodel.TransactionViewModel

class ViewModelFactory(
    private val authRepository: AuthRepository,
    private val transactionRepository: TransactionRepository,
    private val budgetRepository: BudgetRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authRepository, transactionRepository, budgetRepository) as T
        }
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            return TransactionViewModel(transactionRepository, budgetRepository) as T
        }
        if (modelClass.isAssignableFrom(BudgetViewModel::class.java)) {
            return BudgetViewModel(budgetRepository, transactionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}