package com.example.iwawka.ui.components.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.iwawka.ui.theme.LocalAppState
import com.example.iwawka.ui.theme.ThemeMode

@Composable
fun ThemeSwitcher() {
    val appState = LocalAppState.current
    val isDark = when (appState.themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Тёмная тема",
            style = MaterialTheme.typography.bodyLarge
        )

        Switch(
            checked = isDark,
            onCheckedChange = { checked ->
                appState.themeMode = if (checked) ThemeMode.DARK else ThemeMode.LIGHT
            }
        )
    }
}