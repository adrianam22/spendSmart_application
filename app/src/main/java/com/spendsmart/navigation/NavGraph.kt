package com.spendsmart.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.spendsmart.ui.screens.*
import com.spendsmart.ui.viewmodel.AuthViewModel
import com.spendsmart.ui.viewmodel.BudgetViewModel
import com.spendsmart.ui.viewmodel.TransactionViewModel

object Routes {
    const val LOGIN           = "login"
    const val SIGN_UP         = "sign_up"
    const val DASHBOARD       = "dashboard"
    const val ADD_TRANSACTION = "add_transaction"
    const val BUDGETS         = "budgets"
    const val SETTINGS        = "settings"
}

data class BottomNavItem(val route: String, val label: String, val icon: String)

val bottomNavItems = listOf(
    BottomNavItem(Routes.DASHBOARD,       "Home",     "🏠"),
    BottomNavItem(Routes.ADD_TRANSACTION, "Add",      ""),
    BottomNavItem(Routes.BUDGETS,         "Budgets",  "💳"),
    BottomNavItem(Routes.SETTINGS,        "Settings", "⚙️"),
)

val pagesWithoutNav = listOf(Routes.LOGIN, Routes.SIGN_UP)

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    transactionViewModel: TransactionViewModel,
    budgetViewModel: BudgetViewModel,
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomNav = currentRoute !in pagesWithoutNav
    
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colorScheme.background,
        bottomBar = { if (showBottomNav) BottomNavBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.LOGIN,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.LOGIN) {
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = {
                        navController.navigate(Routes.DASHBOARD) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onNavigateToSignUp = { navController.navigate(Routes.SIGN_UP) }
                )
            }
            composable(Routes.SIGN_UP) {
                SignUpScreen(
                    viewModel = authViewModel,
                    onSignUpSuccess = {
                        navController.navigate(Routes.DASHBOARD) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onBackToLogin = { navController.popBackStack() }
                )
            }
            composable(Routes.DASHBOARD) {
                DashboardScreen(
                    transactionViewModel = transactionViewModel,
                    budgetViewModel = budgetViewModel,
                    onSetBudget = { navController.navigate(Routes.BUDGETS) },
                    onAddTransaction = { navController.navigate(Routes.ADD_TRANSACTION) },
                    onSettings = { navController.navigate(Routes.SETTINGS) }
                )
            }
            composable(Routes.ADD_TRANSACTION) {
                AddTransactionScreen(
                    viewModel = transactionViewModel,
                    onBack = { navController.popBackStack() },
                    onSave = { navController.popBackStack() }
                )
            }
            composable(Routes.BUDGETS) {
                BudgetScreen(
                    budgetViewModel = budgetViewModel,
                    transactionViewModel = transactionViewModel
                )
            }
            composable(Routes.SETTINGS) {
                SettingsScreen(
                    authViewModel = authViewModel,
                    budgetViewModel = budgetViewModel,
                    onBack = { navController.popBackStack() },
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    isDarkMode = isDarkMode,
                    onDarkModeChange = onDarkModeChange
                )
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(24.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(colorScheme.surface)
            .padding(horizontal = 8.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(64.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            bottomNavItems.forEach { item ->
                val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                Box(modifier = Modifier.width(72.dp), contentAlignment = Alignment.Center) {
                    if (item.route == Routes.ADD_TRANSACTION) {
                        IconButton(
                            onClick = { navController.navigate(Routes.ADD_TRANSACTION) },
                            modifier = Modifier.size(48.dp).background(Brush.linearGradient(listOf(colorScheme.primary, colorScheme.primaryContainer)), RoundedCornerShape(14.dp))
                        ) {
                            Icon(Icons.Default.Add, null, tint = colorScheme.onPrimary)
                        }
                    } else {
                        Column(
                            modifier = Modifier.clickable {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id)
                                    launchSingleTop = true
                                }
                            },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(item.icon, fontSize = 22.sp)
                            Text(item.label, fontSize = 10.sp, color = if (isSelected) colorScheme.primary else colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}
