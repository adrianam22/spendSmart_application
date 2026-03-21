package com.spendsmart.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spendsmart.ui.theme.*

@Composable
fun SettingsScreen(onBack: () -> Unit, onLogout: () -> Unit) {

    var fingerprintEnabled    by remember { mutableStateOf(true) }
    var pushNotifications     by remember { mutableStateOf(true) }
    var budgetAlert           by remember { mutableStateOf(true) }
    var darkMode              by remember { mutableStateOf(false) }

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
            Text("Settings", fontSize = 15.sp, fontWeight = FontWeight.Black, color = White)
        }

        Spacer(Modifier.height(20.dp))

        // ── PROFILE ──
        SectionTitle("Profile")
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            border = CardDefaults.outlinedCardBorder().copy(width = 2.dp),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(
                Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(GreenLight)
                        .border(2.dp, Black, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("AJ", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Black)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("Andrew Johnson", fontSize = 15.sp, fontWeight = FontWeight.Black, color = Black)
                    Text("andrew@email.com", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                }
                // Edit button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Black)
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                        .clickable { }
                ) {
                    Text("Edit", fontSize = 13.sp, fontWeight = FontWeight.Black, color = White)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── SECURITY ──
        SectionTitle("Security")
        SettingsGroup {
            SettingsRowArrow(icon = "🔑", title = "Change Password", onClick = {})
            HorizontalDivider(color = GrayBorder, thickness = 1.dp)
            SettingsRowToggle(
                icon = "🫆",
                title = "Fingerprint Login",
                subtitle = "BiometricPrompt API",
                checked = fingerprintEnabled,
                onCheckedChange = { fingerprintEnabled = it }
            )
        }

        Spacer(Modifier.height(16.dp))

        // ── NOTIFICATIONS ──
        SectionTitle("Notifications")
        SettingsGroup {
            SettingsRowToggle(
                icon = "🔔",
                title = "Push Notifications",
                subtitle = "Budget & spending alerts",
                checked = pushNotifications,
                onCheckedChange = { pushNotifications = it }
            )
            HorizontalDivider(color = GrayBorder, thickness = 1.dp)
            SettingsRowToggle(
                icon = "⚠️",
                title = "Budget Alert (80%)",
                subtitle = "Notify when limit is close",
                checked = budgetAlert,
                onCheckedChange = { budgetAlert = it }
            )
        }

        Spacer(Modifier.height(16.dp))

        // ── PREFERENCES ──
        SectionTitle("Preferences")
        SettingsGroup {
            SettingsRowBadge(icon = "💱", title = "Currency", badge = "USD")
            HorizontalDivider(color = GrayBorder, thickness = 1.dp)
            SettingsRowBadge(icon = "🌍", title = "Language", badge = "EN")
            HorizontalDivider(color = GrayBorder, thickness = 1.dp)
            SettingsRowToggle(
                icon = "🌙",
                title = "Dark Mode",
                checked = darkMode,
                onCheckedChange = { darkMode = it }
            )
        }

        Spacer(Modifier.height(24.dp))

        // ── LOG OUT ──
        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Black)
        ) {
            Text("Log Out", fontSize = 16.sp, fontWeight = FontWeight.Black, color = White)
        }

        Spacer(Modifier.height(24.dp))
    }
}

// ── Componente reutilizabile ──

@Composable
fun SectionTitle(text: String) {
    Text(
        text,
        fontSize = 16.sp, fontWeight = FontWeight.Black, color = Black,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun SettingsGroup(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        border = CardDefaults.outlinedCardBorder().copy(width = 2.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column { content() }
    }
}

@Composable
fun SettingsRowArrow(icon: String, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 18.sp, modifier = Modifier.width(28.dp))
        Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Black, modifier = Modifier.weight(1f))
        Icon(Icons.Default.KeyboardArrowRight, null, tint = GrayBorder)
    }
}

@Composable
fun SettingsRowToggle(
    icon: String,
    title: String,
    subtitle: String = "",
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 18.sp, modifier = Modifier.width(28.dp))
        Column(Modifier.weight(1f)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Black)
            if (subtitle.isNotEmpty()) {
                Text(subtitle, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = White,
                checkedTrackColor = Black,
                uncheckedThumbColor = White,
                uncheckedTrackColor = GrayBorder,
                uncheckedBorderColor = GrayBorder
            )
        )
    }
}

@Composable
fun SettingsRowBadge(icon: String, title: String, badge: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 18.sp, modifier = Modifier.width(28.dp))
        Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Black, modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .border(2.dp, Black, RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text(badge, fontSize = 12.sp, fontWeight = FontWeight.Black, color = Black)
        }
    }
}