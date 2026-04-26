package com.spendsmart.ui.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spendsmart.ui.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddTransactionScreen(
    viewModel: TransactionViewModel,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    var isExpense by remember { mutableStateOf(true) }
    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var recurringReminder by remember { mutableStateOf("Niciunul") }
    
    var expandedDropdown by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme
    val calendar = Calendar.getInstance()

    val expenseCategories = listOf(
        Pair("🛒", "Food"), 
        Pair("🚗", "Transport"), 
        Pair("🏥", "Health"),
        Pair("🎭", "Fun"),
        Pair("🛍️", "Shopping"),
        Pair("🧾", "Bills"),
        Pair("🎓", "Education")
    )
    val incomeCategories = listOf(
        Pair("💼", "Salary"), 
        Pair("🎁", "Gift"), 
        Pair("📈", "Investment"),
        Pair("💻", "Freelance")
    )
    val currentCategories = if (isExpense) expenseCategories else incomeCategories
    val recurringOptions = listOf("Niciunul", "Zilnic", "Săptămânal", "Lunar")

    Column(
        modifier = Modifier.fillMaxSize().background(colorScheme.background).verticalScroll(rememberScrollState()).padding(20.dp)
    ) {
        Text("Add Transaction", fontWeight = FontWeight.Black, fontSize = 24.sp)
        Spacer(Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(50.dp)).background(colorScheme.surfaceVariant)) {
            listOf(true to "Expense", false to "Income").forEach { (isExp, label) ->
                Box(
                    modifier = Modifier.weight(1f).clickable { 
                        isExpense = isExp
                        selectedCategory = "" 
                    }.background(if (isExpense == isExp) colorScheme.primary else colorScheme.surfaceVariant).padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(label, color = if (isExpense == isExp) colorScheme.onPrimary else colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = amount, onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) amount = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Amount ($)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center, fontSize = 28.sp, fontWeight = FontWeight.Black),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(Modifier.height(20.dp))

        Text("Select Category", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            currentCategories.forEach { (emoji, label) ->
                val selected = selectedCategory == label
                FilterChip(
                    selected = selected,
                    onClick = { selectedCategory = label },
                    label = { Text(label) },
                    leadingIcon = { Text(emoji) }
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = description, onValueChange = { description = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Description (Optional)") },
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(Modifier.height(20.dp))

        val dateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(selectedDate))
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = dateStr, onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Date") },
                readOnly = true,
                trailingIcon = { Icon(Icons.Default.DateRange, null) },
                shape = RoundedCornerShape(16.dp),
                enabled = false, 
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = colorScheme.onSurface,
                    disabledBorderColor = colorScheme.outline,
                    disabledLabelColor = colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = colorScheme.onSurfaceVariant
                )
            )
            Box(Modifier.matchParentSize().clickable {
                DatePickerDialog(context, { _, y, m, d ->
                    calendar.set(y, m, d)
                    selectedDate = calendar.timeInMillis
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
            })
        }

        Spacer(Modifier.height(20.dp))

        Box {
            OutlinedTextField(
                value = recurringReminder, onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Recurring Reminder") },
                readOnly = true,
                trailingIcon = { IconButton(onClick = { expandedDropdown = true }) { Icon(Icons.Default.ArrowDropDown, null) } },
                shape = RoundedCornerShape(16.dp)
            )
            DropdownMenu(expanded = expandedDropdown, onDismissRequest = { expandedDropdown = false }) {
                recurringOptions.forEach { option ->
                    DropdownMenuItem(text = { Text(option) }, onClick = {
                        recurringReminder = option
                        expandedDropdown = false
                    })
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                val amt = amount.toDoubleOrNull() ?: 0.0
                if (amt <= 0) {
                    Toast.makeText(context, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                } else if (selectedCategory.isEmpty()) {
                    Toast.makeText(context, "Please select a category", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.addTransaction(
                        amount = amt,
                        type = if (isExpense) "expense" else "income",
                        category = selectedCategory,
                        desc = description.ifEmpty { selectedCategory },
                        date = selectedDate,
                        recurring = recurringReminder
                    )
                    Toast.makeText(context, "Transaction saved!", Toast.LENGTH_SHORT).show()
                    onSave()
                }
            },
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(50.dp)
        ) {
            Text("Save Transaction", fontWeight = FontWeight.Black, fontSize = 16.sp)
        }
    }
}
