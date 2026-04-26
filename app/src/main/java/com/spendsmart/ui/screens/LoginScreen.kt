package com.spendsmart.ui.screens

import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.core.content.ContextCompat
import com.spendsmart.R
import com.spendsmart.ui.viewmodel.AuthViewModel
import com.spendsmart.utils.SpendSmartBiometricManager

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPass by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme

    // Verificăm dacă utilizatorul este deja logat (pentru auto-login la pornire)
    LaunchedEffect(viewModel.isLoggedIn) {
        if (viewModel.isLoggedIn) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(48.dp))
        Box(modifier = Modifier.size(80.dp).clip(RoundedCornerShape(24.dp)).background(colorScheme.primaryContainer), contentAlignment = Alignment.Center) { Text("💰", fontSize = 36.sp) }
        Spacer(Modifier.height(14.dp))
        Text(stringResource(R.string.login_title), fontSize = 26.sp, fontWeight = FontWeight.Black)
        Spacer(Modifier.height(40.dp))

        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = colorScheme.surface)) {
            Column(Modifier.padding(24.dp)) {
                Text(stringResource(R.string.email), fontWeight = FontWeight.Bold)
                OutlinedTextField(value = email, onValueChange = { email = it; errorMsg = "" }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(50.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
                Spacer(Modifier.height(12.dp))
                Text(stringResource(R.string.password), fontWeight = FontWeight.Bold)
                OutlinedTextField(value = password, onValueChange = { password = it; errorMsg = "" }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(50.dp), visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(), trailingIcon = { Text(if (showPass) "Hide" else "Show", modifier = Modifier.clickable { showPass = !showPass }.padding(end = 8.dp)) })

                if (errorMsg.isNotEmpty()) { Text(errorMsg, color = colorScheme.error, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp)) }
                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        isLoading = true
                        viewModel.login(email, password, onSuccess = { isLoading = false; onLoginSuccess() }, onError = { isLoading = false; errorMsg = it })
                    },
                    modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(50.dp), enabled = !isLoading
                ) {
                    if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    else Text(stringResource(R.string.sign_in), fontWeight = FontWeight.Black)
                }

                Spacer(Modifier.height(16.dp))

                // --- BUTON AMPRENTĂ REPARAT ---
                OutlinedButton(
                    onClick = {
                        if (!SpendSmartBiometricManager.isBiometricAssociated(context)) {
                            errorMsg = "Nu există cont asociat amprentei. Loghează-te manual și activează amprenta din Setări."
                            return@OutlinedButton
                        }
                        
                        val executor = ContextCompat.getMainExecutor(context)
                        val biometricPrompt = BiometricPrompt(context as FragmentActivity, executor,
                            object : BiometricPrompt.AuthenticationCallback() {
                                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                                    super.onAuthenticationSucceeded(result)
                                    isLoading = true
                                    // ACUM APELĂM LOGIN-UL REAL ÎN FIREBASE
                                    viewModel.loginWithBiometric(context, 
                                        onSuccess = { isLoading = false; onLoginSuccess() },
                                        onError = { isLoading = false; errorMsg = it }
                                    )
                                }
                                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                                    super.onAuthenticationError(errorCode, errString)
                                    errorMsg = errString.toString()
                                }
                            })

                        val promptInfo = BiometricPrompt.PromptInfo.Builder()
                            .setTitle("SpendSmart Login")
                            .setSubtitle("Scanează amprenta pentru acces la cont")
                            .setNegativeButtonText(context.getString(R.string.password))
                            .build()

                        biometricPrompt.authenticate(promptInfo)
                    },
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text("🫆 " + stringResource(R.string.sign_in_fingerprint), fontWeight = FontWeight.Black)
                }

                Spacer(Modifier.height(16.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(stringResource(R.string.dont_have_account))
                    Text(stringResource(R.string.sign_up), color = colorScheme.primary, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onNavigateToSignUp() })
                }
            }
        }
    }
}
