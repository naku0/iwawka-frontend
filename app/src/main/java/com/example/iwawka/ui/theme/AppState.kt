package com.example.iwawka.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

enum class ThemeMode { SYSTEM, LIGHT, DARK }

class AppState {
    var themeMode by mutableStateOf(ThemeMode.SYSTEM)

    var accentArgb by mutableIntStateOf(Color.Black.toArgb())
    val accentColor: Color get() = Color(accentArgb)
}

val LocalAppState = staticCompositionLocalOf { AppState() }
