package com.spendsmart.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.spendsmart.R

object LanguageUtils {
    private const val PREFS_NAME = "settings"
    private const val KEY_LANGUAGE = "language"

    fun changeLanguage(context: Context, languageCode: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply()
        
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
        
        // Afișăm o notificare de succes în limba corespunzătoare
        val message = if (languageCode == "ro") "Limba a fost schimbată în Română" else "Language changed to English"
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun getCurrentLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANGUAGE, "ro") ?: "ro"
    }
}