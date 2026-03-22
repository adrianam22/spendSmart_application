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
import com.spendsmart.ui.theme.*

@Composable
fun AddTransactionScreen(onBack: () -> Unit, onSave: () -> Unit) {

    var isExpense         by remember { mutableStateOf(true) }
    var amount            by remember { mutableStateOf("") }
    var selectedCategory  by remember { mutableStateOf("Food") }
    var description       by remember { mutableStateOf("") }
    var date              by remember { mutableStateOf("March 21, 2026") }
    var recurringReminder by remember { mutableStateOf("") }

    val categories = listOf("Food", "Transport", "Health")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayLight)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        // ── TOP BAR ──
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(GreenAccent),
                contentAlignment = Alignment.Center
            ) { Text("💰", fontSize = 18.sp) }
            Spacer(Modifier.width(10.dp))
            Text("SpendSmart", fontWeight = FontWeight.Black, fontSize = 16.sp)
        }

        Spacer(Modifier.height(16.dp))

        // ── TITLE BADGE ──
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(Black)
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Text("Add Transaction", fontSize = 15.sp, fontWeight = FontWeight.Black, color = White)
        }

        Spacer(Modifier.height(20.dp))

        // ── EXPENSE / INCOME TOGGLE ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(50.dp))
                .border(2.5.dp, Black, RoundedCornerShape(50.dp))
                .background(White)
        ) {
            listOf(true to "Expense", false to "Income").forEach { (isExp, label) ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(50.dp))
                        .background(if (isExpense == isExp) Black else White)
                        .clickable { isExpense = isExp }
                        .padding(vertical = 13.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        label,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isExpense == isExp) White else Black
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
                .background(White)
                .border(2.dp, GrayBorder, RoundedCornerShape(20.dp))
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "ENTER AMOUNT",
                fontSize = 11.sp, fontWeight = FontWeight.Bold,
                color = TextSecondary, letterSpacing = 1.sp
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
                    color = Black
                ),
                placeholder = {
                    Text(
                        "$0",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        color = GrayBorder,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Black,
                    unfocusedBorderColor = GrayBorder,
                    focusedContainerColor = White,
                    unfocusedContainerColor = White
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
                        .background(if (selected) Black else White)
                        .border(2.dp, Black, RoundedCornerShape(50.dp))
                        .clickable { selectedCategory = cat }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        cat,
                        fontSize = 13.sp, fontWeight = FontWeight.Bold,
                        color = if (selected) White else Black
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // ── DESCRIPTION ──
        Text("Description", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Black)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g. Grocery shopping", color = TextSecondary) },
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Black,
                unfocusedBorderColor = Black,
                focusedContainerColor = White,
                unfocusedContainerColor = White
            )
        )

        Spacer(Modifier.height(14.dp))

        // ── DATE ──
        Text("Date", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Black)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Select date", color = TextSecondary) },
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Black,
                unfocusedBorderColor = Black,
                focusedContainerColor = White,
                unfocusedContainerColor = White
            )
        )

        Spacer(Modifier.height(14.dp))

        // ── RECURRING REMINDER ──
        Text("Recurring Reminder", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Black)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = recurringReminder,
            onValueChange = { recurringReminder = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("No repeat", color = TextSecondary) },
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Black,
                unfocusedBorderColor = Black,
                focusedContainerColor = White,
                unfocusedContainerColor = White
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
            colors = ButtonDefaults.buttonColors(containerColor = Black)
        ) {
            Text("Save Transaction", fontSize = 16.sp, fontWeight = FontWeight.Black, color = White)
        }

        Spacer(Modifier.height(24.dp))
    }
}