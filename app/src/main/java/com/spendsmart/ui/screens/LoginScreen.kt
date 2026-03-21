package com.spendsmart.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spendsmart.ui.theme.*

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {

    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPass by remember { mutableStateOf(false) }
    var emailErr by remember { mutableStateOf("") }
    var passErr  by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayLight)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(48.dp))

        // ── APP LOGO ──
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(GreenAccent),
            contentAlignment = Alignment.Center
        ) {
            Text("💰", fontSize = 36.sp)
        }

        Spacer(Modifier.height(14.dp))

        Text(
            text = "SpendSmart",
            fontSize = 26.sp,
            fontWeight = FontWeight.Black,
            color = Black
        )

        Text(
            text = "Track your spending smartly",
            fontSize = 14.sp,
            color = TextSecondary,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(Modifier.height(40.dp))

        // ── CARD FORMULAR ──
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(Modifier.padding(24.dp)) {

                // Email
                Text("Email", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Black)
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; emailErr = "" },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("example@email.com", color = TextSecondary) },
                    leadingIcon = { Icon(Icons.Default.Email, null, tint = TextSecondary) },
                    shape = RoundedCornerShape(50.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = emailErr.isNotEmpty(),
                    supportingText = if (emailErr.isNotEmpty()) {{ Text(emailErr, color = RedColor, fontSize = 12.sp) }} else null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Black,
                        unfocusedBorderColor = GrayBorder,
                        focusedContainerColor = White,
                        unfocusedContainerColor = White
                    )
                )

                Spacer(Modifier.height(12.dp))

                // Password
                Text("Password", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Black)
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; passErr = "" },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("••••••••", color = TextSecondary) },
                    leadingIcon = { Icon(Icons.Default.Lock, null, tint = TextSecondary) },
                    visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        Text(
                            if (showPass) "Hide" else "Show",
                            fontSize = 12.sp, fontWeight = FontWeight.Bold,
                            color = TextSecondary,
                            modifier = Modifier
                                .clickable { showPass = !showPass }
                                .padding(end = 8.dp)
                        )
                    },
                    shape = RoundedCornerShape(50.dp),
                    singleLine = true,
                    isError = passErr.isNotEmpty(),
                    supportingText = if (passErr.isNotEmpty()) {{ Text(passErr, color = RedColor, fontSize = 12.sp) }} else null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Black,
                        unfocusedBorderColor = GrayBorder,
                        focusedContainerColor = White,
                        unfocusedContainerColor = White
                    )
                )

                // Forgot password
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    Text(
                        "Forgot password?",
                        fontSize = 13.sp, fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 18.dp)
                            .clickable { }
                    )
                }

                // Sign In button
                Button(
                    onClick = {
                        var valid = true
                        if (email.isBlank()) { emailErr = "Enter your email"; valid = false }
                        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) { emailErr = "Invalid email"; valid = false }
                        if (password.isBlank()) { passErr = "Enter your password"; valid = false }
                        else if (password.length < 6) { passErr = "Minimum 6 characters"; valid = false }
                        if (valid) onLoginSuccess()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Black)
                ) {
                    Text("Sign In", fontSize = 16.sp, fontWeight = FontWeight.Black, color = White)
                }

                // Divider
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(Modifier.weight(1f), color = GrayBorder)
                    Text(" OR ", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                    HorizontalDivider(Modifier.weight(1f), color = GrayBorder)
                }

                // Fingerprint button
                OutlinedButton(
                    onClick = { /* BiometricPrompt */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(50.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 2.dp
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Black)
                ) {
                    Text("🫆  ", fontSize = 18.sp)
                    Text("Sign In with Fingerprint", fontSize = 15.sp, fontWeight = FontWeight.Black)
                }

                // Sign Up link
                Spacer(Modifier.height(16.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Don't have an account? ", fontSize = 13.sp, color = TextSecondary)
                    Text(
                        "Sign Up",
                        fontSize = 13.sp, fontWeight = FontWeight.Black,
                        color = Black,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { }
                    )
                }
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}