package com.spendsmart.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spendsmart.R
import com.spendsmart.data.model.Budget
import com.spendsmart.data.model.Transaction
import com.spendsmart.data.repository.BudgetRepository
import com.spendsmart.data.repository.TransactionRepository
import com.spendsmart.utils.NotificationHelper
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class BudgetViewModel(
    private val repository: BudgetRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val calendar = Calendar.getInstance()
    val month = calendar.get(Calendar.MONTH)
    val year = calendar.get(Calendar.YEAR)
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    val currentBudget: StateFlow<Budget?> = repository.getBudget(month, year)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val monthlyTransactions: Flow<List<Transaction>> = transactionRepository.allTransactions
        .map { list ->
            list.filter { 
                val cal = Calendar.getInstance().apply { timeInMillis = it.date }
                cal.get(Calendar.MONTH) == month && cal.get(Calendar.YEAR) == year
            }
        }

    val spentThisMonth: StateFlow<Double> = monthlyTransactions
        .map { it.filter { t -> t.type == "expense" }.sumOf { t -> t.amount } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Flag pentru a evita notificări multiple
    private var hasNotified80 = false

    fun checkBudgetAlert(context: Context) {
        val spent = spentThisMonth.value
        val limit = currentBudget.value?.totalLimit ?: 0.0
        
        if (limit > 0 && spent / limit >= 0.8 && !hasNotified80) {
            NotificationHelper.showBudgetAlert(
                context,
                context.getString(R.string.app_name),
                context.getString(R.string.budget_alert_message)
            )
            hasNotified80 = true
        } else if (limit > 0 && spent / limit < 0.8) {
            hasNotified80 = false // Resetăm dacă se șterg tranzacții și coborâm sub prag
        }
    }

    val budgetAlert: StateFlow<Boolean> = combine(spentThisMonth, currentBudget) { spent, budget ->
        val limit = budget?.totalLimit ?: 0.0
        limit > 0 && (spent / limit) >= 0.8
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun calculateDailyRate(spent: Double, day: Int): Double = if (day > 0) spent / day else 0.0
    fun calculateMonthPrediction(dailyRate: Double, totalDays: Int): Double = dailyRate * totalDays
    fun calculateCategorySpent(transactions: List<Transaction>, category: String): Double =
        transactions.filter { it.category == category && it.type == "expense" }.sumOf { it.amount }

    fun saveBudget(
        total: Double,
        food: Double,
        transport: Double,
        health: Double,
        funVal: Double,
        shopping: Double,
        bills: Double,
        education: Double
    ) {
        viewModelScope.launch {
            val existing = currentBudget.value
            val budget = (existing ?: Budget(month = month, year = year)).copy(
                totalLimit = total,
                foodLimit = food,
                transportLimit = transport,
                healthLimit = health,
                funLimit = funVal,
                shoppingLimit = shopping,
                billsLimit = bills,
                educationLimit = education,
                isSynced = false
            )
            repository.updateBudget(budget)
        }
    }

    suspend fun syncWithFirebase() {
        repository.syncBudgets()
    }
}