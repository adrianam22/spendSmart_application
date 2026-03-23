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

    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {

        // ── HEADER ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(colorScheme.primary, colorScheme.primaryContainer)
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
                        .background(colorScheme.onPrimary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) { Text("💰", fontSize = 20.sp) }

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(
                        "Hello,",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = colorScheme.onPrimary.copy(alpha = 0.7f)
                    )
                    Text(
                        "SpendSmart",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = colorScheme.onPrimary
                    )
                }

                Spacer(Modifier.weight(1f))

                // Notif button
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(colorScheme.onPrimary.copy(alpha = 0.1f))
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        null,
                        tint = colorScheme.onPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    // Red dot
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(colorScheme.error)
                            .align(Alignment.TopEnd)
                            .offset((-8).dp, 8.dp)
                            .border(1.5.dp, colorScheme.primary, CircleShape)
                    )
                }
            }
        }

        // ── BALANCE CARD ──
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp)
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(colorScheme.secondary, colorScheme.secondaryContainer)
                    )
                )
                .padding(22.dp)
        ) {
            Column {
                Text(
                    "AVAILABLE BALANCE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
                    letterSpacing = 1.5.sp
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "$5,000.00",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Black,
                    color = colorScheme.onSecondaryContainer,
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
                            .background(colorScheme.surface.copy(alpha = 0.3f))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("↑ ", color = SuccessGreen, fontWeight = FontWeight.Black, fontSize = 12.sp)
                                Text("INCOME", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = colorScheme.onSecondaryContainer.copy(alpha = 0.6f), letterSpacing = 1.sp)
                            }
                            Spacer(Modifier.height(4.dp))
                            Text("+$5,000", fontSize = 16.sp, fontWeight = FontWeight.Black, color = SuccessGreen)
                        }
                    }
                    // Expenses
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(colorScheme.surface.copy(alpha = 0.3f))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("↓ ", color = colorScheme.error, fontWeight = FontWeight.Black, fontSize = 12.sp)
                                Text("EXPENSES", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = colorScheme.onSecondaryContainer.copy(alpha = 0.6f), letterSpacing = 1.sp)
                            }
                            Spacer(Modifier.height(4.dp))
                            Text("−$300", fontSize = 16.sp, fontWeight = FontWeight.Black, color = colorScheme.error)
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
                containerColor = colorScheme.primary
            ),
            elevation = ButtonDefaults.buttonElevation(4.dp)
        ) {
            Text("＋ ", fontSize = 16.sp, color = colorScheme.onPrimary, fontWeight = FontWeight.Black)
            Text("Set Monthly Budget", fontSize = 15.sp, fontWeight = FontWeight.Black, color = colorScheme.onPrimary)
        }

        Spacer(Modifier.height(24.dp))

        // ── CATEGORIES ──
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Categories", fontSize = 17.sp, fontWeight = FontWeight.Black, color = colorScheme.onBackground)
            Spacer(Modifier.weight(1f))
            Text(
                "See all →",
                fontSize = 13.sp, fontWeight = FontWeight.Bold,
                color = colorScheme.primary,
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
                        .background(if (isSelected) colorScheme.primary else colorScheme.surfaceVariant)
                        .border(
                            2.dp,
                            if (isSelected) colorScheme.primary else colorScheme.outline,
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
                        color = if (isSelected) colorScheme.onPrimary else colorScheme.onSurfaceVariant
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
            Text("Recent Transactions", fontSize = 17.sp, fontWeight = FontWeight.Black, color = colorScheme.onBackground)
            Spacer(Modifier.weight(1f))
            Text(
                "All →",
                fontSize = 13.sp, fontWeight = FontWeight.Bold,
                color = colorScheme.primary,
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
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon circle
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(if (isPositive) SuccessGreen.copy(alpha = 0.1f) else colorScheme.error.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(emoji, fontSize = 20.sp)
        }

        Spacer(Modifier.width(14.dp))

        Column(Modifier.weight(1f)) {
            Text(name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = colorScheme.onSurface)
            Text(date, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = colorScheme.onSurfaceVariant)
        }

        // Amount badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (isPositive) SuccessGreen.copy(alpha = 0.1f) else colorScheme.error.copy(alpha = 0.1f))
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            val amountColor = if (isPositive) SuccessGreen else colorScheme.error
            Text(
                text = if (isPositive) "+${amount.toInt()}" else "${amount.toInt()}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = amountColor
            )
        }
    }
}
