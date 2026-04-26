package com.spendsmart.ui.screens

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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spendsmart.R
import com.spendsmart.ui.viewmodel.AuthViewModel

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel,
    onSignUpSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var enableBiometric by remember { mutableStateOf(false) }
    var showPass by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(48.dp))

        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text("💰", fontSize = 36.sp)
        }

        Spacer(Modifier.height(14.dp))

        Text(stringResource(R.string.create_account), fontSize = 26.sp, fontWeight = FontWeight.Black, color = colorScheme.onBackground)
        Text(stringResource(R.string.track_spending_smartly), fontSize = 14.sp, color = colorScheme.onSurfaceVariant)

        Spacer(Modifier.height(40.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(Modifier.padding(24.dp)) {
                Text(stringResource(R.string.username), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(stringResource(R.string.username)) },
                    leadingIcon = { Icon(Icons.Default.Person, null) },
                    shape = RoundedCornerShape(50.dp),
                    singleLine = true
                )

                Spacer(Modifier.height(12.dp))

                Text(stringResource(R.string.email), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("example@email.com") },
                    leadingIcon = { Icon(Icons.Default.Email, null) },
                    shape = RoundedCornerShape(50.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(Modifier.height(12.dp))

                Text(stringResource(R.string.password), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("••••••••") },
                    leadingIcon = { Icon(Icons.Default.Lock, null) },
                    visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                    shape = RoundedCornerShape(50.dp),
                    singleLine = true
                )

                Spacer(Modifier.height(12.dp))

                Text(stringResource(R.string.confirm_password), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("••••••••") },
                    leadingIcon = { Icon(Icons.Default.Lock, null) },
                    visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                    shape = RoundedCornerShape(50.dp),
                    singleLine = true
                )

                Spacer(Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = enableBiometric,
                        onCheckedChange = { enableBiometric = it }
                    )
                    Text(stringResource(R.string.enable_biometric), fontSize = 14.sp)
                }

                if (errorMsg.isNotEmpty()) {
                    Text(errorMsg, color = colorScheme.error, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                }

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (username.isBlank() || email.isBlank() || password.isBlank()) {
                            errorMsg = "Please fill all fields"
                            return@Button
                        }
                        if (password != confirmPassword) {
                            errorMsg = "Passwords do not match"
                            return@Button
                        }
                        isLoading = true
                        viewModel.signUp(email, password, username, enableBiometric,
                            onSuccess = { 
                                isLoading = false
                                onSignUpSuccess() 
                            },
                            onError = { 
                                isLoading = false
                                errorMsg = it 
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(50.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) CircularProgressIndicator(color = colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                    else Text(stringResource(R.string.sign_up), fontSize = 16.sp, fontWeight = FontWeight.Black)
                }

                Spacer(Modifier.height(16.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(stringResource(R.string.already_have_account), fontSize = 13.sp)
                    Text(
                        stringResource(R.string.sign_in),
                        fontSize = 13.sp, fontWeight = FontWeight.Black,
                        color = colorScheme.primary,
                        modifier = Modifier.clickable { onBackToLogin() }
                    )
                }
            }
        }
    }
}
