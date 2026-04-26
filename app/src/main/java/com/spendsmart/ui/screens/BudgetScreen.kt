package com.spendsmart.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spendsmart.R
import com.spendsmart.ui.viewmodel.BudgetViewModel
import com.spendsmart.ui.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    budgetViewModel: BudgetViewModel,
    transactionViewModel: TransactionViewModel
) {
    val budget by budgetViewModel.currentBudget.collectAsState()
    val spentThisMonth by budgetViewModel.spentThisMonth.collectAsState()
    val monthlyTransactions by budgetViewModel.monthlyTransactions.collectAsState(initial = emptyList())
    
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme
    
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val calendar = Calendar.getInstance()
    val monthName = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time)
    
    val dailyRate = budgetViewModel.calculateDailyRate(spentThisMonth, budgetViewModel.currentDay)
    val prediction = budgetViewModel.calculateMonthPrediction(dailyRate, budgetViewModel.daysInMonth)
    val limit = budget?.totalLimit ?: 0.0
    val remaining = limit - spentThisMonth
    val diff = prediction - limit

    LaunchedEffect(Unit) {
        budgetViewModel.syncWithFirebase()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(stringResource(R.string.budgets), fontWeight = FontWeight.Black, fontSize = 24.sp)
        Spacer(Modifier.height(20.dp))

        // --- SECȚIUNEA 1: REZUMAT LUNAR ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = colorScheme.primaryContainer)
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("${stringResource(R.string.this_month_label)} ($monthName)", fontWeight = FontWeight.Bold, color = colorScheme.onPrimaryContainer)
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text(stringResource(R.string.spent), fontSize = 12.sp)
                        Text("${spentThisMonth.toInt()} RON", fontSize = 20.sp, fontWeight = FontWeight.Black)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(stringResource(R.string.remaining), fontSize = 12.sp)
                        Text(
                            "${remaining.toInt()} RON", 
                            fontSize = 20.sp, 
                            fontWeight = FontWeight.Black,
                            color = if (remaining >= 0) Color(0xFF2E7D32) else colorScheme.error
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = if (limit > 0 && prediction > limit) colorScheme.errorContainer else Color(0xFFE8F5E9)
        ) {
            Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                if (limit > 0 && prediction > limit) {
                    Text("${stringResource(R.string.prediction_exceed)} ${diff.toInt()} RON", color = colorScheme.onErrorContainer, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                } else if (limit > 0) {
                    val saved = limit - prediction
                    Text("${stringResource(R.string.prediction_on_track)} ${saved.toInt()} RON", color = Color(0xFF2E7D32), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                } else {
                    Text(stringResource(R.string.set_budget_prompt), fontSize = 13.sp)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // --- SECȚIUNEA 2: PROGRES CATEGORII ---
        Text(stringResource(R.string.categories), fontWeight = FontWeight.Black, fontSize = 18.sp)
        Spacer(Modifier.height(12.dp))

        if (budget == null || limit == 0.0) {
            Box(Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.set_budget_prompt), color = colorScheme.onSurfaceVariant)
            }
        } else {
            val categories = listOf(
                Triple("🛒", "Food", budget?.foodLimit ?: 0.0),
                Triple("🚗", "Transport", budget?.transportLimit ?: 0.0),
                Triple("🏥", "Health", budget?.healthLimit ?: 0.0),
                Triple("🎭", "Fun", budget?.funLimit ?: 0.0),
                Triple("🛍️", "Shopping", budget?.shoppingLimit ?: 0.0),
                Triple("🧾", "Bills", budget?.billsLimit ?: 0.0),
                Triple("🎓", "Education", budget?.educationLimit ?: 0.0)
            )

            categories.forEach { (emoji, name, catLimit) ->
                if (catLimit > 0) {
                    val catSpent = budgetViewModel.calculateCategorySpent(monthlyTransactions, name)
                    val progress = (catSpent / catLimit).toFloat()
                    
                    CategoryProgressItem(
                        emoji = emoji,
                        name = name,
                        spent = catSpent,
                        limit = catLimit,
                        progress = progress
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { showBottomSheet = true },
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(50.dp)
        ) {
            Text(stringResource(R.string.modify_budget_limits), fontWeight = FontWeight.Black)
        }
        
        Spacer(Modifier.height(20.dp))
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            BudgetSettingsContent(
                initialBudget = budget,
                onSave = { b ->
                    budgetViewModel.saveBudget(
                        b.totalLimit, b.foodLimit, b.transportLimit, b.healthLimit,
                        b.funLimit, b.shoppingLimit, b.billsLimit, b.educationLimit
                    )
                    showBottomSheet = false
                    Toast.makeText(context, context.getString(R.string.success_budget_saved), Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@Composable
fun CategoryProgressItem(emoji: String, name: String, spent: Double, limit: Double, progress: Float) {
    val color = when {
        progress < 0.6f -> Color(0xFF4CAF50)
        progress < 0.8f -> Color(0xFFFF9800)
        else -> Color(0xFFF44336)
    }

    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("$emoji $name", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text("${spent.toInt()} RON ${stringResource(R.string.spent_of)} ${limit.toInt()} RON", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(5.dp)),
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Text("${(progress * 100).toInt()}% ${stringResource(R.string.spent).lowercase()}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color, modifier = Modifier.align(Alignment.End).padding(top = 4.dp))
    }
}

@Composable
fun BudgetSettingsContent(
    initialBudget: com.spendsmart.data.model.Budget?,
    onSave: (com.spendsmart.data.model.Budget) -> Unit
) {
    var total by remember { mutableStateOf(initialBudget?.totalLimit?.toString() ?: "") }
    var food by remember { mutableStateOf(initialBudget?.foodLimit?.toString() ?: "") }
    var transport by remember { mutableStateOf(initialBudget?.transportLimit?.toString() ?: "") }
    var health by remember { mutableStateOf(initialBudget?.healthLimit?.toString() ?: "") }
    var funVal by remember { mutableStateOf(initialBudget?.funLimit?.toString() ?: "") }
    var shopping by remember { mutableStateOf(initialBudget?.shoppingLimit?.toString() ?: "") }
    var bills by remember { mutableStateOf(initialBudget?.billsLimit?.toString() ?: "") }
    var education by remember { mutableStateOf(initialBudget?.educationLimit?.toString() ?: "") }

    val context = LocalContext.current

    Column(Modifier.padding(24.dp).padding(bottom = 40.dp).verticalScroll(rememberScrollState())) {
        Text(stringResource(R.string.budget_configuration), fontWeight = FontWeight.Black, fontSize = 20.sp)
        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = total, onValueChange = { total = it },
            label = { Text(stringResource(R.string.total_monthly_limit)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(Modifier.height(20.dp))
        
        Text(stringResource(R.string.limit_per_category), fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        val fields = listOf(
            stringResource(R.string.category_food) to { v: String -> food = v } to food,
            stringResource(R.string.category_transport) to { v: String -> transport = v } to transport,
            stringResource(R.string.category_health) to { v: String -> health = v } to health,
            stringResource(R.string.category_fun) to { v: String -> funVal = v } to funVal,
            stringResource(R.string.category_shopping) to { v: String -> shopping = v } to shopping,
            stringResource(R.string.category_bills) to { v: String -> bills = v } to bills,
            stringResource(R.string.category_education) to { v: String -> education = v } to education
        )

        fields.chunked(2).forEach { rowFields ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                rowFields.forEach { field ->
                    OutlinedTextField(
                        value = field.second,
                        onValueChange = field.first.second,
                        label = { Text(field.first.first) },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                if (rowFields.size == 1) Spacer(Modifier.weight(1f))
            }
            Spacer(Modifier.height(8.dp))
        }

        Spacer(Modifier.height(24.dp))

        val totalVal = total.toDoubleOrNull() ?: 0.0
        
        Button(
            onClick = {
                if (totalVal <= 0) {
                    Toast.makeText(context, context.getString(R.string.total_monthly_limit) + " " + context.getString(R.string.loading), Toast.LENGTH_SHORT).show()
                } else {
                    onSave(com.spendsmart.data.model.Budget(
                        totalLimit = totalVal,
                        foodLimit = food.toDoubleOrNull() ?: 0.0,
                        transportLimit = transport.toDoubleOrNull() ?: 0.0,
                        healthLimit = health.toDoubleOrNull() ?: 0.0,
                        funLimit = funVal.toDoubleOrNull() ?: 0.0,
                        shoppingLimit = shopping.toDoubleOrNull() ?: 0.0,
                        billsLimit = bills.toDoubleOrNull() ?: 0.0,
                        educationLimit = education.toDoubleOrNull() ?: 0.0
                    ))
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text(stringResource(R.string.save), fontWeight = FontWeight.Bold)
        }
    }
}
