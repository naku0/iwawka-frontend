package com.example.iwawka.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

class AppState {
    var counter by mutableIntStateOf(0);
    var isDarkTheme by mutableStateOf(false)
}

val LocalAppState = staticCompositionLocalOf { AppState() }