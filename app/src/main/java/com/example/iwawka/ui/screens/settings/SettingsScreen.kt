package com.example.iwawka.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.example.iwawka.ui.components.common.ThemeSwitcher
import com.example.iwawka.ui.theme.LocalAppState

@Composable
fun SettingsScreen() {
    val appState = LocalAppState.current

    val presets = listOf(
        Color.Black,               // дефолт
        Color(0xFF2563EB),         // blue
        Color(0xFF16A34A),         // green
        Color(0xFFDC2626),         // red
        Color(0xFFF59E0B),         // amber
        Color(0xFF7C3AED),         // violet
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Настройки темы",
            style = MaterialTheme.typography.titleLarge
        )

        ThemeSwitcher()

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Акцент",
                style = MaterialTheme.typography.bodyLarge
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                presets.forEach { color ->
                    val selected = appState.accentArgb == color.toArgb()

                    Spacer(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (selected) 2.dp else 1.dp,
                                color = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = if (selected) 1f else 0.2f
                                ),
                                shape = CircleShape
                            )
                            .clickable {
                                appState.accentArgb = color.value.toInt()
                            }
                    )
                }
            }

        }

        Text(
            text = "Другие настройки появятся здесь",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}
