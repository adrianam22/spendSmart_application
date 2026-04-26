package com.spendsmart

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.spendsmart.navigation.NavGraph
import com.spendsmart.ui.theme.SpendSmartTheme
import com.spendsmart.ui.viewmodel.AuthViewModel
import com.spendsmart.ui.viewmodel.BudgetViewModel
import com.spendsmart.ui.viewmodel.TransactionViewModel
import com.spendsmart.ui.viewmodel.factory.ViewModelFactory
import com.spendsmart.utils.LanguageUtils

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Restore saved language
        val currentLang = LanguageUtils.getCurrentLanguage(this)
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(currentLang))

        enableEdgeToEdge()
        
        val app = application as SpendSmartApplication
        val factory = ViewModelFactory(
            app.authRepository,
            app.transactionRepository,
            app.budgetRepository
        )

        setContent {
            val systemDark = isSystemInDarkTheme()
            var isDarkMode by rememberSaveable { mutableStateOf(systemDark) }

            val authViewModel: AuthViewModel = viewModel(factory = factory)
            val transactionViewModel: TransactionViewModel = viewModel(factory = factory)
            val budgetViewModel: BudgetViewModel = viewModel(factory = factory)

            SpendSmartTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    authViewModel = authViewModel,
                    transactionViewModel = transactionViewModel,
                    budgetViewModel = budgetViewModel,
                    isDarkMode = isDarkMode,
                    onDarkModeChange = { isDarkMode = it }
                )
            }
        }
    }
}
