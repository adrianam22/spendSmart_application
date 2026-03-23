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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spendsmart.ui.theme.*

@Composable
fun SettingsScreen(
    onBack: () -> Unit, 
    onLogout: () -> Unit,
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit
) {

    var fingerprintEnabled    by remember { mutableStateOf(true) }
    var pushNotifications     by remember { mutableStateOf(true) }
    var budgetAlert           by remember { mutableStateOf(true) }

    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showEditProfileDialog by remember { mutableStateOf(false) }
    
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
            Text("Settings", fontSize = 15.sp, fontWeight = FontWeight.Black, color = colorScheme.onPrimary)
        }

        Spacer(Modifier.height(20.dp))

        // ── PROFILE ──
        SectionTitle("Profile")
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
            border = CardDefaults.outlinedCardBorder().copy(width = 1.dp),
            elevation = CardDefaults.cardElevation(2.dp)
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
                        .background(colorScheme.secondaryContainer)
                        .border(1.dp, colorScheme.outline, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("AJ", fontSize = 14.sp, fontWeight = FontWeight.Black, color = colorScheme.onSecondaryContainer)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("Andrew Johnson", fontSize = 15.sp, fontWeight = FontWeight.Black, color = colorScheme.onSurface)
                    Text("andrew@email.com", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colorScheme.onSurfaceVariant)
                }
                // Edit button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(colorScheme.primary)
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                        .clickable { showEditProfileDialog = true }
                ) {
                    Text("Edit", fontSize = 13.sp, fontWeight = FontWeight.Black, color = colorScheme.onPrimary)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── SECURITY ──
        SectionTitle("Security")
        SettingsGroup {
            SettingsRowArrow(icon = "🔑", title = "Change Password", onClick = { showChangePasswordDialog = true })
            HorizontalDivider(color = colorScheme.outlineVariant, thickness = 1.dp)
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
            HorizontalDivider(color = colorScheme.outlineVariant, thickness = 1.dp)
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
            HorizontalDivider(color = colorScheme.outlineVariant, thickness = 1.dp)
            SettingsRowBadge(icon = "🌍", title = "Language", badge = "EN")
            HorizontalDivider(color = colorScheme.outlineVariant, thickness = 1.dp)
            SettingsRowToggle(
                icon = "🌙",
                title = "Dark Mode",
                checked = isDarkMode,
                onCheckedChange = onDarkModeChange
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
            colors = ButtonDefaults.buttonColors(containerColor = colorScheme.errorContainer, contentColor = colorScheme.onErrorContainer)
        ) {
            Text("Log Out", fontSize = 16.sp, fontWeight = FontWeight.Black)
        }

        Spacer(Modifier.height(24.dp))
    }

    // ── DIALOGS ──

    if (showChangePasswordDialog) {
        AlertDialog(
            onDismissRequest = { showChangePasswordDialog = false },
            title = { Text("Change Password", fontWeight = FontWeight.Black) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    var oldPass by remember { mutableStateOf("") }
                    var newPass by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = oldPass,
                        onValueChange = { oldPass = it },
                        label = { Text("Old Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newPass,
                        onValueChange = { newPass = it },
                        label = { Text("New Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = { showChangePasswordDialog = false }) {
                    Text("Update")
                }
            },
            dismissButton = {
                TextButton(onClick = { showChangePasswordDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showEditProfileDialog) {
        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text("Edit Profile", fontWeight = FontWeight.Black) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    var name by remember { mutableStateOf("Andrew Johnson") }
                    var email by remember { mutableStateOf("andrew@email.com") }

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = { showEditProfileDialog = false }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// ── Componente reutilizabile ──

@Composable
fun SectionTitle(text: String) {
    Text(
        text,
        fontSize = 16.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun SettingsGroup(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder().copy(width = 1.dp),
        elevation = CardDefaults.cardElevation(2.dp)
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
        verticalAlignment = Alignment.CenterVertically) {
        val colorScheme = MaterialTheme.colorScheme
        Text(icon, fontSize = 18.sp, modifier = Modifier.width(28.dp))
        Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = colorScheme.onSurface, modifier = Modifier.weight(1f))
        Icon(Icons.Default.KeyboardArrowRight, null, tint = colorScheme.onSurfaceVariant)
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
    val colorScheme = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 18.sp, modifier = Modifier.width(28.dp))
        Column(Modifier.weight(1f)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = colorScheme.onSurface)
            if (subtitle.isNotEmpty()) {
                Text(subtitle, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = colorScheme.onSurfaceVariant)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun SettingsRowBadge(icon: String, title: String, badge: String) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 18.sp, modifier = Modifier.width(28.dp))
        Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = colorScheme.onSurface, modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .border(1.dp, colorScheme.outline, RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text(badge, fontSize = 12.sp, fontWeight = FontWeight.Black, color = colorScheme.onSurface)
        }
    }
}
