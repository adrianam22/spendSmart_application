package com.spendsmart.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.spendsmart.ui.screens.*
import com.spendsmart.ui.theme.SuccessGreen

// ══════════════════════════════════════
//  ROUTES
// ══════════════════════════════════════
object Routes {
    const val LOGIN           = "login"
    const val DASHBOARD       = "dashboard"
    const val ADD_TRANSACTION = "add_transaction"
    const val BUDGETS         = "budgets"
    const val SET_BUDGET      = "set_budget"
    const val SETTINGS        = "settings"
}

// ══════════════════════════════════════
//  BOTTOM NAV ITEMS
// ══════════════════════════════════════
data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: String
)

val bottomNavItems = listOf(
    BottomNavItem(Routes.DASHBOARD,       "Home",     "🏠"),
    BottomNavItem(Routes.ADD_TRANSACTION, "Add",      ""),
    BottomNavItem(Routes.BUDGETS,         "Budgets",  "💳"),
    BottomNavItem(Routes.SETTINGS,        "Settings", "⚙️"),
)

val pagesWithoutNav = listOf(Routes.LOGIN)

// ══════════════════════════════════════
//  MAIN SCAFFOLD + NAV GRAPH
// ══════════════════════════════════════
@Composable
fun NavGraph(
    navController: NavHostController,
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomNav = currentRoute !in pagesWithoutNav
    
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colorScheme.background,
        bottomBar = {
            if (showBottomNav) {
                BottomNavBar(navController = navController)
            }
        }
    ) { innerPadding ->

        NavHost(
            navController    = navController,
            startDestination = Routes.DASHBOARD,
            modifier         = Modifier.padding(innerPadding)
        ) {
            composable(Routes.LOGIN) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Routes.DASHBOARD) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                )
            }
            composable(Routes.DASHBOARD) {
                DashboardScreen(
                    onSetBudget      = { navController.navigate(Routes.SET_BUDGET) },
                    onAddTransaction = { navController.navigate(Routes.ADD_TRANSACTION) },
                    onSettings       = { navController.navigate(Routes.SETTINGS) }
                )
            }
            composable(Routes.ADD_TRANSACTION) {
                AddTransactionScreen(
                    onBack = { navController.popBackStack() },
                    onSave = {
                        navController.navigate(Routes.DASHBOARD) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Routes.BUDGETS) {
                BudgetScreen()
            }

            composable(Routes.SET_BUDGET) {
                BudgetScreen()
            }
            composable(Routes.SETTINGS) {
                SettingsScreen(
                    onBack   = { navController.popBackStack() },
                    onLogout = {
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

// ══════════════════════════════════════
//  BOTTOM NAVIGATION BAR
// ══════════════════════════════════════
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
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            bottomNavItems.forEach { item ->
                val isSelected = currentDestination?.hierarchy?.any {
                    it.route == item.route
                } == true

                Box(
                    modifier = Modifier.width(72.dp),
                    contentAlignment = Alignment.Center

                ) {
                    if (item.route == Routes.ADD_TRANSACTION) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .shadow(8.dp, RoundedCornerShape(16.dp))
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(colorScheme.primary, colorScheme.primaryContainer)
                                    )
                                )
                                .clickable {
                                    navController.navigate(Routes.ADD_TRANSACTION) {
                                        launchSingleTop = true
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add",
                                tint = colorScheme.onPrimary,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .clickable {
                                    navController.navigate(item.route)
                                    {  popUpTo(navController.graph.findStartDestination().id)
                                        launchSingleTop = true
                                    }
                                }
                                .padding(vertical = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text(
                                    text = item.icon,
                                    fontSize = 22.sp
                                )
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(5.dp)
                                            .clip(CircleShape)
                                            .background(colorScheme.primary)
                                            .align(Alignment.BottomCenter)
                                            .offset(y = 6.dp)
                                    )
                                }
                            }

                            Spacer(Modifier.height(4.dp))

                            Text(
                                text = item.label,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                                color = if (isSelected) colorScheme.primary else colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
