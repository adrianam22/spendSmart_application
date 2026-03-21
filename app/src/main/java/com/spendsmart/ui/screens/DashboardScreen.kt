package com.spendsmart.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spendsmart.ui.theme.*

@Composable
fun DashboardScreen(
    onSetBudget: () -> Unit,
    onAddTransaction: () -> Unit,
    onSettings: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("Food") }
    val categories = listOf(
        Pair("🛒", "Food"),
        Pair("🚗", "Transport"),
        Pair("🏥", "Health"),
        Pair("🎮", "Fun")
    )

    val transactions = listOf(
        Triple("Auchan",       "Food · Feb 28",  -200f),
        Triple("Kaufland",     "Food · Feb 27",  -100f),
        Triple("March Salary", "Income · Mar 1", +5000f),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
            .verticalScroll(rememberScrollState())
    ) {

        // ── HEADER cu gradient ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF1A1A2E), Color(0xFF16213E))
                    )
                )
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(GreenAccent),
                    contentAlignment = Alignment.Center
                ) { Text("💰", fontSize = 20.sp) }

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(
                        "Good morning,",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF94A3B8)
                    )
                    Text(
                        "SpendSmart",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }

                Spacer(Modifier.weight(1f))

                // Notif button
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF1E2D4A))
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    // Red dot
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEF4444))
                            .align(Alignment.TopEnd)
                            .offset((-8).dp, 8.dp)
                            .border(1.5.dp, Color(0xFF1E2D4A), CircleShape)
                    )
                }
            }
        }

        // ── BALANCE CARD (gradient închis) ──
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp)
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF1A1A2E), Color(0xFF0F3460))
                    )
                )
                .padding(22.dp)
        ) {
            Column {
                Text(
                    "AVAILABLE BALANCE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B),
                    letterSpacing = 1.5.sp
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "$5,000.00",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = (-1).sp
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Income
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x1A3DC45A))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("↑ ", color = GreenAccent, fontWeight = FontWeight.Black, fontSize = 12.sp)
                                Text("INCOME", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8), letterSpacing = 1.sp)
                            }
                            Spacer(Modifier.height(4.dp))
                            Text("+$5,000", fontSize = 16.sp, fontWeight = FontWeight.Black, color = GreenAccent)
                        }
                    }
                    // Expenses
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x1AEF4444))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("↓ ", color = Color(0xFFEF4444), fontWeight = FontWeight.Black, fontSize = 12.sp)
                                Text("EXPENSES", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8), letterSpacing = 1.sp)
                            }
                            Spacer(Modifier.height(4.dp))
                            Text("−$300", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFFEF4444))
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── SET BUDGET BUTTON ──
        Button(
            onClick = onSetBudget,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1A1A2E)
            ),
            elevation = ButtonDefaults.buttonElevation(4.dp)
        ) {
            Text("＋ ", fontSize = 16.sp, color = GreenAccent, fontWeight = FontWeight.Black)
            Text("Set Monthly Budget", fontSize = 15.sp, fontWeight = FontWeight.Black, color = Color.White)
        }

        Spacer(Modifier.height(24.dp))

        // ── CATEGORIES ──
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Categories", fontSize = 17.sp, fontWeight = FontWeight.Black, color = Color(0xFF1A1A2E))
            Spacer(Modifier.weight(1f))
            Text(
                "See all →",
                fontSize = 13.sp, fontWeight = FontWeight.Bold,
                color = GreenAccent,
                modifier = Modifier.clickable { }
            )
        }

        Spacer(Modifier.height(12.dp))

        Row(
            Modifier.padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            categories.forEach { (emoji, label) ->
                val isSelected = label == selectedCategory
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) Color(0xFF1A1A2E) else Color.White)
                        .border(
                            2.dp,
                            if (isSelected) Color(0xFF1A1A2E) else Color(0xFFE2E8F0),
                            RoundedCornerShape(16.dp)
                        )
                        .clickable { selectedCategory = label }
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(emoji, fontSize = 20.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else Color(0xFF64748B)
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // ── RECENT TRANSACTIONS ──
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Recent Transactions", fontSize = 17.sp, fontWeight = FontWeight.Black, color = Color(0xFF1A1A2E))
            Spacer(Modifier.weight(1f))
            Text(
                "All →",
                fontSize = 13.sp, fontWeight = FontWeight.Bold,
                color = GreenAccent,
                modifier = Modifier.clickable { }
            )
        }

        Spacer(Modifier.height(12.dp))

        transactions.forEach { (name, date, amount) ->
            ImprovedTransactionItem(name = name, date = date, amount = amount)
            Spacer(Modifier.height(10.dp))
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun ImprovedTransactionItem(name: String, date: String, amount: Float) {
    val isPositive = amount >= 0
    val emoji = when {
        name.contains("Salary") -> "💼"
        name.contains("Auchan") || name.contains("Kaufland") -> "🛒"
        else -> "💸"
    }

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon circle
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(if (isPositive) Color(0xFFE8F8EE) else Color(0xFFFEF2F2)),
            contentAlignment = Alignment.Center
        ) {
            Text(emoji, fontSize = 20.sp)
        }

        Spacer(Modifier.width(14.dp))

        Column(Modifier.weight(1f)) {
            Text(name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A2E))
            Text(date, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF94A3B8))
        }

        // Amount badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (isPositive) Color(0xFFE8F8EE) else Color(0xFFFEF2F2))
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Text(
                text = if (isPositive) "+${amount.toInt()}" else "${amount.toInt()}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = if (isPositive) Color(0xFF16A34A) else Color(0xFFDC2626)
            )
        }
    }
}