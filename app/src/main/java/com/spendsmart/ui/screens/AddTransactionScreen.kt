package com.spendsmart.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddTransactionScreen(onBack: () -> Unit, onSave: () -> Unit) {

    var isExpense         by remember { mutableStateOf(true) }
    var amount            by remember { mutableStateOf("") }
    var selectedCategory  by remember { mutableStateOf("Food") }
    var description       by remember { mutableStateOf("") }
    var date              by remember { mutableStateOf("March 21, 2026") }
    var recurringReminder by remember { mutableStateOf("") }

    val categories = listOf("Food", "Transport", "Health")
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        // ── TOP BAR ──
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) { Text("💰", fontSize = 18.sp) }
            Spacer(Modifier.width(10.dp))
            Text("SpendSmart", fontWeight = FontWeight.Black, fontSize = 16.sp, color = colorScheme.onBackground)
        }

        Spacer(Modifier.height(16.dp))

        // ── TITLE BADGE ──
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(colorScheme.primary)
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Text("Add Transaction", fontSize = 15.sp, fontWeight = FontWeight.Black, color = colorScheme.onPrimary)
        }

        Spacer(Modifier.height(20.dp))

        // ── EXPENSE / INCOME TOGGLE ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(50.dp))
                .border(1.dp, colorScheme.outline, RoundedCornerShape(50.dp))
                .background(colorScheme.surface)
        ) {
            listOf(true to "Expense", false to "Income").forEach { (isExp, label) ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(50.dp))
                        .background(if (isExpense == isExp) colorScheme.primary else colorScheme.surface)
                        .clickable { isExpense = isExp }
                        .padding(vertical = 13.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        label,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isExpense == isExp) colorScheme.onPrimary else colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // ── AMOUNT DISPLAY ──
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(colorScheme.surface)
                .border(1.dp, colorScheme.outline, RoundedCornerShape(20.dp))
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "ENTER AMOUNT",
                fontSize = 11.sp, fontWeight = FontWeight.Bold,
                color = colorScheme.onSurfaceVariant, letterSpacing = 1.sp
            )
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    color = colorScheme.onSurface
                ),
                placeholder = {
                    Text(
                        "$0",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        color = colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorScheme.primary,
                    unfocusedBorderColor = colorScheme.outline,
                    focusedContainerColor = colorScheme.surface,
                    unfocusedContainerColor = colorScheme.surface
                )
            )
        }

        Spacer(Modifier.height(20.dp))

        // ── CATEGORIES ──
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            categories.forEach { cat ->
                val selected = cat == selectedCategory
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(if (selected) colorScheme.primary else colorScheme.surface)
                        .border(1.dp, if (selected) colorScheme.primary else colorScheme.outline, RoundedCornerShape(50.dp))
                        .clickable { selectedCategory = cat }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        cat,
                        fontSize = 13.sp, fontWeight = FontWeight.Bold,
                        color = if (selected) colorScheme.onPrimary else colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // ── DESCRIPTION ──
        Text("Description", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = colorScheme.onSurface)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g. Grocery shopping") },
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorScheme.primary,
                unfocusedBorderColor = colorScheme.outline
            )
        )

        Spacer(Modifier.height(14.dp))

        // ── DATE ──
        Text("Date", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = colorScheme.onSurface)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Select date") },
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorScheme.primary,
                unfocusedBorderColor = colorScheme.outline
            )
        )

        Spacer(Modifier.height(14.dp))

        // ── RECURRING REMINDER ──
        Text("Recurring Reminder", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = colorScheme.onSurface)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = recurringReminder,
            onValueChange = { recurringReminder = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("No repeat") },
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorScheme.primary,
                unfocusedBorderColor = colorScheme.outline
            )
        )

        Spacer(Modifier.height(28.dp))

        // ── SAVE BUTTON ──
        Button(
            onClick = onSave,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
        ) {
            Text("Save Transaction", fontSize = 16.sp, fontWeight = FontWeight.Black, color = colorScheme.onPrimary)
        }

        Spacer(Modifier.height(24.dp))
    }
}
