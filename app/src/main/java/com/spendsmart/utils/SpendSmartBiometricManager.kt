package com.spendsmart.utils

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

object SpendSmartBiometricManager {
    private const val PREFS_NAME = "biometric_credentials"
    private const val KEY_EMAIL = "saved_email"
    private const val KEY_PASSWORD = "saved_password"
    private const val KEY_ASSOCIATED = "biometric_associated"

    private fun getEncryptedPrefs(context: Context) = EncryptedSharedPreferences.create(
        PREFS_NAME,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun canAuthenticate(context: Context): Int {
        return BiometricManager.from(context).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
    }

    fun saveCredentialsForBiometric(context: Context, email: String, pass: String) {
        getEncryptedPrefs(context).edit().apply {
            putString(KEY_EMAIL, email)
            putString(KEY_PASSWORD, pass)
            putBoolean(KEY_ASSOCIATED, true)
            apply()
        }
    }

    fun getCredentialsAfterBiometric(context: Context): Pair<String, String>? {
        val prefs = getEncryptedPrefs(context)
        val email = prefs.getString(KEY_EMAIL, null)
        val pass = prefs.getString(KEY_PASSWORD, null)
        return if (email != null && pass != null) Pair(email, pass) else null
    }

    fun isBiometricAssociated(context: Context): Boolean {
        return getEncryptedPrefs(context).getBoolean(KEY_ASSOCIATED, false)
    }

    fun removeBiometricAssociation(context: Context) {
        getEncryptedPrefs(context).edit().clear().apply()
    }
}