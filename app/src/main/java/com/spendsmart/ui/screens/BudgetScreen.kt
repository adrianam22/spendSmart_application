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
fun BudgetScreen() {

    var selectedCategory  by remember { mutableStateOf("Food") }
    var limitAmount       by remember { mutableStateOf("") }
    var period            by remember { mutableStateOf("Monthly") }
    var alertThreshold    by remember { mutableStateOf("80") }
    var notes             by remember { mutableStateOf("") }

    val categories = listOf("Food", "Transport", "Health", "Fun")
    val periods    = listOf("Weekly", "Monthly", "Yearly")
    val categoryEmoji = mapOf("Food" to "🛒", "Transport" to "🚗", "Health" to "🏥", "Fun" to "🎮")

    val existingBudgets = remember { mutableStateListOf<Triple<String, String, String>>() }

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
            Text("Budgets", fontSize = 15.sp, fontWeight = FontWeight.Black, color = White)
        }

        Spacer(Modifier.height(20.dp))

        // ── EXISTING BUDGETS (mini-cards) ──
        if (existingBudgets.isNotEmpty()) {
            Text("Active Budgets", fontWeight = FontWeight.Black, fontSize = 14.sp, color = Black)
            Spacer(Modifier.height(10.dp))
            existingBudgets.forEach { (emoji, name, limit) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(White)
                        .border(2.dp, GrayBorder, RoundedCornerShape(14.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(emoji, fontSize = 20.sp)
                    Spacer(Modifier.width(10.dp))
                    Text(name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Black, modifier = Modifier.weight(1f))
                    Text(limit, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                }
                Spacer(Modifier.height(8.dp))
            }
            Spacer(Modifier.height(12.dp))
        }

        // ── FORM CARD ──
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(White)
                .border(2.dp, GrayBorder, RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Text(
                "SET NEW BUDGET",
                fontSize = 11.sp, fontWeight = FontWeight.Bold,
                color = TextSecondary, letterSpacing = 1.sp
            )

            Spacer(Modifier.height(16.dp))

            // ── LIMIT AMOUNT DISPLAY ──
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(GrayLight)
                    .border(2.dp, GrayBorder, RoundedCornerShape(16.dp))
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "SPENDING LIMIT",
                    fontSize = 11.sp, fontWeight = FontWeight.Bold,
                    color = TextSecondary, letterSpacing = 1.sp
                )
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = limitAmount,
                    onValueChange = { limitAmount = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
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

            // ── CATEGORY ──
            Text("Category", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Black)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                categories.forEach { cat ->
                    val selected = cat == selectedCategory
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(if (selected) Black else White)
                            .border(2.dp, Black, RoundedCornerShape(50.dp))
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
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

            // ── PERIOD TOGGLE ──
            Text("Period", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Black)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
                    .border(2.5.dp, Black, RoundedCornerShape(50.dp))
                    .background(White)
            ) {
                periods.forEach { p ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(50.dp))
                            .background(if (period == p) Black else White)
                            .clickable { period = p }
                            .padding(vertical = 11.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            p,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = if (period == p) White else Black
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── ALERT THRESHOLD ──
            Text("Alert Threshold (%)", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Black)
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value = alertThreshold,
                onValueChange = { alertThreshold = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g. 80", color = TextSecondary) },
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Black,
                    unfocusedBorderColor = Black,
                    focusedContainerColor = White,
                    unfocusedContainerColor = White
                )
            )

            Spacer(Modifier.height(14.dp))

            // ── NOTES ──
            Text("Notes", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Black)
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Optional note", color = TextSecondary) },
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Black,
                    unfocusedBorderColor = Black,
                    focusedContainerColor = White,
                    unfocusedContainerColor = White
                )
            )
        }

        Spacer(Modifier.height(28.dp))

        // ── SAVE BUTTON ──
        Button(
            onClick = {
                if (limitAmount.isNotBlank()) {
                    val emoji = categoryEmoji[selectedCategory] ?: "💳"
                    val periodLabel = period.lowercase()
                    existingBudgets.add(Triple(emoji, selectedCategory, "$$limitAmount / $periodLabel"))
                    limitAmount      = ""
                    alertThreshold   = "80"
                    notes            = ""
                    selectedCategory = "Food"
                    period           = "Monthly"
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Black)
        ) {
            Text("Save Budget", fontSize = 16.sp, fontWeight = FontWeight.Black, color = White)
        }

        Spacer(Modifier.height(24.dp))
    }
}