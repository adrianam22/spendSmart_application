package com.spendsmart.ui.screens

import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.spendsmart.R
import com.spendsmart.ui.viewmodel.AuthViewModel
import com.spendsmart.ui.viewmodel.BudgetViewModel
import com.spendsmart.utils.LanguageUtils
import com.spendsmart.utils.SpendSmartBiometricManager
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    authViewModel: AuthViewModel,
    budgetViewModel: BudgetViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit
) {
    val user by authViewModel.currentUser.collectAsState()
    val budget by budgetViewModel.currentBudget.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isBiometricAssociated by remember { mutableStateOf(SpendSmartBiometricManager.isBiometricAssociated(context)) }
    var showEditBudgetDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showBiometricAuthDialog by remember { mutableStateOf(false) }
    var showBiometricDeauthDialog by remember { mutableStateOf(false) }
    
    var currentLanguage by remember { mutableStateOf(LanguageUtils.getCurrentLanguage(context)) }

    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(R.string.settings), fontWeight = FontWeight.Black, fontSize = 24.sp)
        }

        Spacer(Modifier.height(20.dp))

        SectionTitle(stringResource(R.string.profile))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = colorScheme.surface)
        ) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(14.dp)).background(colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
                    Text(user?.displayName?.take(2)?.uppercase() ?: "??", fontWeight = FontWeight.Black)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(user?.displayName ?: "User", fontWeight = FontWeight.Black)
                    Text(user?.email ?: "", fontSize = 12.sp, color = colorScheme.onSurfaceVariant)
                }
                TextButton(onClick = { showEditProfileDialog = true }) { Text(stringResource(R.string.edit)) }
            }
        }

        Spacer(Modifier.height(16.dp))

        SectionTitle(stringResource(R.string.total_monthly_limit))
        Card(
            modifier = Modifier.fillMaxWidth().clickable { showEditBudgetDialog = true },
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = colorScheme.surface)
        ) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("💰", fontSize = 20.sp)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(stringResource(R.string.total_monthly_limit), fontWeight = FontWeight.Bold)
                    Text("${stringResource(R.string.spent)}: $${String.format(java.util.Locale.US, "%.2f", budget?.totalLimit ?: 0.0)}", fontSize = 12.sp, color = colorScheme.primary)
                }
                Icon(Icons.Default.KeyboardArrowRight, null)
            }
        }

        Spacer(Modifier.height(16.dp))

        SectionTitle(stringResource(R.string.security_preferences))
        SettingsGroup {
            SettingsRowArrow("🔑", stringResource(R.string.change_password)) { showChangePasswordDialog = true }
            HorizontalDivider(color = colorScheme.outlineVariant, thickness = 1.dp)
            SettingsRowToggle(
                icon = "🫆",
                title = stringResource(R.string.fingerprint_login),
                subtitle = stringResource(R.string.biometric_auth),
                checked = isBiometricAssociated,
                onCheckedChange = { 
                    if (it) showBiometricAuthDialog = true else showBiometricDeauthDialog = true 
                }
            )
            HorizontalDivider(color = colorScheme.outlineVariant, thickness = 1.dp)
            
            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Language, null, modifier = Modifier.width(28.dp))
                Text(stringResource(R.string.language), fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(selected = currentLanguage == "ro", onClick = { currentLanguage = "ro"; LanguageUtils.changeLanguage(context, "ro") }, label = { Text("RO") })
                    FilterChip(selected = currentLanguage == "en", onClick = { currentLanguage = "en"; LanguageUtils.changeLanguage(context, "en") }, label = { Text("EN") })
                }
            }
            
            HorizontalDivider(color = colorScheme.outlineVariant, thickness = 1.dp)
            SettingsRowToggle("🌙", stringResource(R.string.dark_mode), "", isDarkMode) { onDarkModeChange(it) }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorScheme.errorContainer, contentColor = colorScheme.onErrorContainer)
        ) {
            Text(stringResource(R.string.log_out), fontWeight = FontWeight.Black)
        }
    }

    // DIALOGS
    if (showBiometricAuthDialog) {
        var passInput by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showBiometricAuthDialog = false },
            title = { Text("Asociază amprenta") },
            text = {
                Column {
                    Text("Introdu parola contului tău pentru a asocia amprenta")
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = passInput, onValueChange = { passInput = it },
                        label = { Text(stringResource(R.string.password)) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val user = FirebaseAuth.getInstance().currentUser
                    val email = user?.email
                    if (email != null) {
                        val credential = EmailAuthProvider.getCredential(email, passInput)
                        user.reauthenticate(credential).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val executor = ContextCompat.getMainExecutor(context)
                                val biometricPrompt = BiometricPrompt(context as FragmentActivity, executor,
                                    object : BiometricPrompt.AuthenticationCallback() {
                                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                                            SpendSmartBiometricManager.saveCredentialsForBiometric(context, email, passInput)
                                            isBiometricAssociated = true
                                            showBiometricAuthDialog = false
                                            Toast.makeText(context, "Amprentă asociată cu succes!", Toast.LENGTH_SHORT).show()
                                        }
                                    })
                                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                                    .setTitle("SpendSmart")
                                    .setSubtitle("Scanează amprenta pentru înregistrare")
                                    .setNegativeButtonText("Anulează")
                                    .build()
                                biometricPrompt.authenticate(promptInfo)
                            } else {
                                Toast.makeText(context, "Parolă incorectă!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }) { Text(stringResource(R.string.confirm)) }
            },
            dismissButton = { TextButton(onClick = { showBiometricAuthDialog = false }) { Text(stringResource(R.string.cancel)) } }
        )
    }

    if (showBiometricDeauthDialog) {
        AlertDialog(
            onDismissRequest = { showBiometricDeauthDialog = false },
            title = { Text("Dezasociază amprenta") },
            text = { Text("Ești sigur că vrei să elimini autentificarea cu amprentă?") },
            confirmButton = {
                Button(onClick = {
                    SpendSmartBiometricManager.removeBiometricAssociation(context)
                    isBiometricAssociated = false
                    showBiometricDeauthDialog = false
                }) { Text("Da") }
            },
            dismissButton = { TextButton(onClick = { showBiometricDeauthDialog = false }) { Text("Nu") } }
        )
    }

    if (showEditBudgetDialog) {
        var newLimit by remember { mutableStateOf(budget?.totalLimit?.toString() ?: "") }
        AlertDialog(
            onDismissRequest = { showEditBudgetDialog = false },
            title = { Text(stringResource(R.string.edit_budget_limits)) },
            text = {
                OutlinedTextField(
                    value = newLimit, onValueChange = { newLimit = it },
                    label = { Text(stringResource(R.string.total_monthly_limit)) },
                    shape = RoundedCornerShape(12.dp)
                )
            },
            confirmButton = {
                Button(onClick = {
                    budgetViewModel.saveBudget(
                        total = newLimit.toDoubleOrNull() ?: 0.0,
                        food = budget?.foodLimit ?: 0.0,
                        transport = budget?.transportLimit ?: 0.0,
                        health = budget?.healthLimit ?: 0.0,
                        funVal = budget?.funLimit ?: 0.0,
                        shopping = budget?.shoppingLimit ?: 0.0,
                        bills = budget?.billsLimit ?: 0.0,
                        education = budget?.educationLimit ?: 0.0
                    )
                    showEditBudgetDialog = false
                    Toast.makeText(context, context.getString(R.string.success_budget_saved), Toast.LENGTH_SHORT).show()
                }) { Text(stringResource(R.string.save)) }
            }
        )
    }

    if (showEditProfileDialog) {
        var newName by remember { mutableStateOf(user?.displayName ?: "") }
        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text(stringResource(R.string.edit)) },
            text = {
                OutlinedTextField(
                    value = newName, onValueChange = { newName = it },
                    label = { Text(stringResource(R.string.username)) },
                    shape = RoundedCornerShape(12.dp)
                )
            },
            confirmButton = {
                Button(onClick = {
                    scope.launch {
                        authViewModel.updateProfile(newName, null)
                        showEditProfileDialog = false
                        Toast.makeText(context, context.getString(R.string.success_profile_updated), Toast.LENGTH_SHORT).show()
                    }
                }) { Text(stringResource(R.string.save)) }
            }
        )
    }

    if (showChangePasswordDialog) {
        var newPass by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showChangePasswordDialog = false },
            title = { Text(stringResource(R.string.change_password)) },
            text = {
                OutlinedTextField(
                    value = newPass, onValueChange = { newPass = it },
                    label = { Text(stringResource(R.string.new_password)) },
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(12.dp)
                )
            },
            confirmButton = {
                Button(onClick = {
                    scope.launch {
                        authViewModel.updatePassword(newPass)
                        showChangePasswordDialog = false
                        Toast.makeText(context, context.getString(R.string.success_password_changed), Toast.LENGTH_SHORT).show()
                    }
                }) { Text(stringResource(R.string.update)) }
            }
        )
    }
}

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
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
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
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 18.sp, modifier = Modifier.width(28.dp))
        Column(Modifier.weight(1f)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = colorScheme.onSurface)
            if (subtitle.isNotEmpty()) {
                Text(subtitle, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = colorScheme.onSurfaceVariant)
            }
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
