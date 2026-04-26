package com.spendsmart.data.repository

import android.content.Context
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.spendsmart.utils.SpendSmartBiometricManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    private val _currentUser = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser

    init {
        auth.addAuthStateListener {
            _currentUser.value = it.currentUser
        }
    }

    suspend fun login(email: String, pass: String): AuthResult {
        return auth.signInWithEmailAndPassword(email, pass).await()
    }

    suspend fun loginWithBiometric(context: Context): Result<FirebaseUser> {
        val creds = SpendSmartBiometricManager.getCredentialsAfterBiometric(context)
        return if (creds != null) {
            try {
                val result = login(creds.first, creds.second)
                if (result.user != null) Result.success(result.user!!)
                else Result.failure(Exception("Login failed"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            Result.failure(Exception("Nu există niciun cont asociat amprentei. Te rugăm să asociezi mai întâi un cont."))
        }
    }

    suspend fun signUp(email: String, pass: String, username: String): AuthResult {
        val result = auth.createUserWithEmailAndPassword(email, pass).await()
        result.user?.let { user ->
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build()
            user.updateProfile(profileUpdates).await()
            
            val userData = hashMapOf(
                "uid" to user.uid,
                "email" to email,
                "displayName" to username,
                "createdAt" to System.currentTimeMillis()
            )
            firestore.collection("users").document(user.uid).set(userData).await()
        }
        return result
    }

    suspend fun updateProfile(displayName: String?, photoUrl: String?): Boolean {
        val user = auth.currentUser ?: return false
        val profileUpdates = UserProfileChangeRequest.Builder()
        displayName?.let { profileUpdates.setDisplayName(it) }
        user.updateProfile(profileUpdates.build()).await()
        
        val updates = mutableMapOf<String, Any>()
        displayName?.let { updates["displayName"] = it }
        if (updates.isNotEmpty()) {
            firestore.collection("users").document(user.uid).update(updates).await()
        }
        return true
    }

    suspend fun updatePassword(newPass: String): Boolean {
        auth.currentUser?.updatePassword(newPass)?.await()
        return true
    }

    fun logout() {
        auth.signOut()
    }
}