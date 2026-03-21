package com.spendsmart.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Culorile aplicației ──
val Black       = Color(0xA6072531)
val White       = Color(0xFFFFFFFF)
val GreenAccent = Color(0xFF3DC45A)
val GreenLight  = Color(0xFFD4F0D0)
val GrayLight   = Color(0xFFF5F5F5)
val GrayBorder  = Color(0xFFE0E0E0)
val TextSecondary = Color(0xFF777777)
val RedColor    = Color(0xFFCC0000)
val OrangeColor = Color(0xFFCC7700)
val IncomeGreen = Color(0xFF1A7A1A)

private val SpendSmartColorScheme = lightColorScheme(
    primary        = Black,
    onPrimary      = White,
    secondary      = GreenAccent,
    onSecondary    = White,
    background     = Color(0xFFF5F5F5),
    onBackground   = Black,
    surface        = White,
    onSurface      = Black,
    outline        = GrayBorder,
)

@Composable
fun SpendSmartTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SpendSmartColorScheme,
        content = content
    )
}