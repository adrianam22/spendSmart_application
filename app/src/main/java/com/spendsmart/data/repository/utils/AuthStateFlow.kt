package com.spendsmart.data.repository.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun AuthStateFlow(auth: FirebaseAuth): Flow<FirebaseUser?> = callbackFlow {
    val listener = FirebaseAuth.AuthStateListener { 
        trySend(it.currentUser)
    }
    auth.addAuthStateListener(listener)
    awaitClose {
        auth.removeAuthStateListener(listener)
    }
}