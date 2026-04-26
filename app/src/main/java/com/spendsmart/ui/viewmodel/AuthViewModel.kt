package com.spendsmart.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.spendsmart.data.repository.AuthRepository
import com.spendsmart.data.repository.TransactionRepository
import com.spendsmart.data.repository.BudgetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository,
    private val transactionRepository: TransactionRepository,
    private val budgetRepository: BudgetRepository
) : ViewModel() {

    val currentUser: StateFlow<FirebaseUser?> = repository.currentUser
    val isLoggedIn: Boolean get() = currentUser.value != null

    private val _isBiometricEnabled = MutableStateFlow(false)
    val isBiometricEnabled: StateFlow<Boolean> = _isBiometricEnabled.asStateFlow()

    fun login(email: String, pass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                repository.login(email, pass)
                syncAllData()
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Authentication failed")
            }
        }
    }

    fun loginWithBiometric(context: Context, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val result = repository.loginWithBiometric(context)
                if (result.isSuccess) {
                    syncAllData()
                    onSuccess()
                } else {
                    onError(result.exceptionOrNull()?.message ?: "Biometric login failed")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error")
            }
        }
    }

    private suspend fun syncAllData() {
        // Sincronizăm tot istoricul utilizatorului după logare
        transactionRepository.syncWithFirebase()
        budgetRepository.syncBudgets()
    }

    fun signUp(email: String, pass: String, username: String, enableBiometric: Boolean, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                repository.signUp(email, pass, username)
                _isBiometricEnabled.value = enableBiometric
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Sign up failed")
            }
        }
    }

    fun setBiometricEnabled(enabled: Boolean) {
        _isBiometricEnabled.value = enabled
    }

    fun updateProfile(displayName: String?, photoUrl: String?) {
        viewModelScope.launch {
            try {
                repository.updateProfile(displayName, photoUrl)
            } catch (e: Exception) { }
        }
    }

    fun updatePassword(newPass: String) {
        viewModelScope.launch {
            try {
                repository.updatePassword(newPass)
            } catch (e: Exception) { }
        }
    }

    fun logout() {
        repository.logout()
    }
}