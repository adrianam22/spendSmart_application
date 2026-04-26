package com.spendsmart.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spendsmart.R
import com.spendsmart.data.model.Transaction
import com.spendsmart.ui.viewmodel.TransactionViewModel
import com.spendsmart.ui.viewmodel.BudgetViewModel
import com.spendsmart.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DashboardScreen(
    transactionViewModel: TransactionViewModel,
    budgetViewModel: BudgetViewModel,
    onSetBudget: () -> Unit,
    onAddTransaction: () -> Unit,
    onSettings: () -> Unit
) {
    val transactions by transactionViewModel.filteredTransactions.collectAsState()
    val totalBalance by transactionViewModel.availableBalance.collectAsState()
    val monthlyInc by transactionViewModel.monthlyIncome.collectAsState()
    val monthlyExp by transactionViewModel.monthlyExpense.collectAsState()
    
    val selectedCategory by transactionViewModel.selectedCategory.collectAsState()
    val budget by budgetViewModel.currentBudget.collectAsState()
    val spentThisMonth by budgetViewModel.spentThisMonth.collectAsState()
    
    var transactionToDelete by remember { mutableStateOf<Transaction?>(null) }
    var showFilterMenu by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme

    // Declanșăm verificarea alertei de sistem
    LaunchedEffect(spentThisMonth, budget) {
        budgetViewModel.checkBudgetAlert(context)
    }

    val allCategories = listOf(
        "All" to R.string.category_all to "📱",
        "Food" to R.string.category_food to "🛒",
        "Transport" to R.string.category_transport to "🚗",
        "Health" to R.string.category_health to "🏥",
        "Fun" to R.string.category_fun to "🎭",
        "Shopping" to R.string.category_shopping to "🛍️",
        "Bills" to R.string.category_bills to "🧾",
        "Education" to R.string.category_education to "🎓",
        "Salary" to R.string.category_salary to "💼",
        "Gift" to R.string.category_gift to "🎁",
        "Investment" to R.string.category_investment to "📈",
        "Freelance" to R.string.category_freelance to "💻"
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTransaction,
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
            ) { Icon(Icons.Default.Add, stringResource(R.string.add_transaction)) }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            // HEADER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(colorScheme.primary, colorScheme.primaryContainer)))
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("💰", fontSize = 24.sp)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(stringResource(R.string.hello), fontSize = 12.sp, color = colorScheme.onPrimary.copy(alpha = 0.7f))
                        Text(stringResource(R.string.app_name), fontSize = 18.sp, fontWeight = FontWeight.Black, color = colorScheme.onPrimary)
                    }
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = onSettings) {
                        Icon(Icons.Default.Notifications, null, tint = colorScheme.onPrimary)
                    }
                }
            }

            // BALANCE CARD
            Card(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = colorScheme.secondaryContainer)
            ) {
                Column(Modifier.padding(22.dp)) {
                    Text(stringResource(R.string.available_balance), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = colorScheme.onSecondaryContainer.copy(alpha = 0.7f))
                    Text(
                        text = String.format(Locale.US, "%,.2f RON", totalBalance),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,
                        color = if (totalBalance > 0) Color(0xFF2E7D32) else colorScheme.error
                    )
                    
                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider(color = colorScheme.onSecondaryContainer.copy(alpha = 0.1f), thickness = 1.dp)
                    Spacer(Modifier.height(16.dp))

                    Text(stringResource(R.string.this_month_label), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = colorScheme.onSecondaryContainer)
                    Spacer(Modifier.height(8.dp))
                    
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            text = String.format(Locale.US, "+%,.2f RON", monthlyInc),
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp
                        )
                        Text(
                            text = String.format(Locale.US, "-%,.2f RON", monthlyExp),
                            color = colorScheme.error,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp
                        )
                    }
                    
                    Text(
                        text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date()),
                        fontSize = 11.sp,
                        color = colorScheme.onSecondaryContainer.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // BUDGET ALERT BANNER
            val limit = budget?.totalLimit ?: 0.0
            val progress = if (limit > 0) (spentThisMonth / limit).toFloat() else 0f
            if (progress >= 0.8f) {
                Surface(
                    modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                    color = colorScheme.errorContainer,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(stringResource(R.string.budget_alert_message), color = colorScheme.onErrorContainer, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(12.dp))
                }
                Spacer(Modifier.height(16.dp))
            }

            Button(
                onClick = onSetBudget,
                modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(50.dp)
            ) { Text(stringResource(R.string.set_monthly_budget), fontWeight = FontWeight.Black) }

            Spacer(Modifier.height(24.dp))

            // FILTER MENU
            Row(
                Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.recent_activity), fontWeight = FontWeight.Black, fontSize = 18.sp, modifier = Modifier.weight(1f))
                Box {
                    IconButton(onClick = { showFilterMenu = true }) {
                        Icon(Icons.Default.FilterList, "Filter")
                    }
                    DropdownMenu(expanded = showFilterMenu, onDismissRequest = { showFilterMenu = false }) {
                        allCategories.forEach { categoryInfo ->
                            val label = categoryInfo.first.first
                            val resId = categoryInfo.first.second
                            val emoji = categoryInfo.second
                            DropdownMenuItem(
                                text = { Text("$emoji " + stringResource(resId)) },
                                onClick = {
                                    transactionViewModel.selectCategory(label)
                                    showFilterMenu = false
                                },
                                leadingIcon = if (selectedCategory == label) { { Icon(Icons.Default.Check, null) } } else null
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            
            if (transactions.isEmpty()) {
                Box(Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.no_transactions), color = colorScheme.onSurfaceVariant)
                }
            } else {
                transactions.take(10).forEach { transaction ->
                    ImprovedTransactionItem(
                        transaction = transaction,
                        onLongClick = { transactionToDelete = transaction }
                    )
                    Spacer(Modifier.height(10.dp))
                }
            }
            
            Spacer(Modifier.height(80.dp))
        }
    }

    if (transactionToDelete != null) {
        AlertDialog(
            onDismissRequest = { transactionToDelete = null },
            title = { Text(stringResource(R.string.delete_transaction)) },
            text = { Text(stringResource(R.string.delete_confirm)) },
            confirmButton = {
                TextButton(onClick = {
                    transactionViewModel.deleteTransaction(transactionToDelete!!)
                    transactionToDelete = null
                }) { Text(stringResource(R.string.delete), color = colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { transactionToDelete = null }) { Text(stringResource(R.string.cancel)) }
            }
        )
    }
}

@Composable
fun ImprovedTransactionItem(transaction: Transaction, onLongClick: () -> Unit) {
    val isPositive = transaction.type == "income"
    val colorScheme = MaterialTheme.colorScheme
    val dateStr = SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date(transaction.date))

    val categoryIcons = mapOf(
        "Food" to "🛒", "Transport" to "🚗", "Health" to "🏥", "Fun" to "🎭", "Shopping" to "🛍️", "Bills" to "🧾", "Education" to "🎓",
        "Salary" to "💼", "Gift" to "🎁", "Investment" to "📈", "Freelance" to "💻"
    )
    val icon = categoryIcons[transaction.category] ?: "💸"

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(colorScheme.surface)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = { onLongClick() })
            }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(46.dp).clip(RoundedCornerShape(14.dp)).background(if (isPositive) SuccessGreen.copy(alpha = 0.1f) else colorScheme.error.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 20.sp)
        }

        Spacer(Modifier.width(14.dp))

        Column(Modifier.weight(1f)) {
            Text(transaction.description, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = colorScheme.onSurface)
            Text(dateStr, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = colorScheme.onSurfaceVariant)
        }

        Text(
            text = if (isPositive) "+$${String.format(Locale.US, "%,.2f", transaction.amount)}" else "-$${String.format(Locale.US, "%,.2f", transaction.amount)}",
            fontSize = 14.sp, fontWeight = FontWeight.Black,
            color = if (isPositive) SuccessGreen else colorScheme.error
        )
    }
}
