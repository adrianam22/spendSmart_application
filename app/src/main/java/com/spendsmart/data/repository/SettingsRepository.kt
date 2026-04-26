package com.spendsmart.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {

    private fun biometricKey(userId: String) = booleanPreferencesKey("biometric_enabled_$userId")

    fun isBiometricEnabled(userId: String): Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[biometricKey(userId)] ?: false
        }

    suspend fun setBiometricEnabled(userId: String, enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[biometricKey(userId)] = enabled
        }
    }
}