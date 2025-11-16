package com.example.iwawka.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.iwawka.ui.theme.LocalAppState

@Composable
fun ThemeSwitcher(modifier: Modifier = Modifier){
    val appState = LocalAppState.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ){
        Text(
            text = "dark theme",
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = appState.isDarkTheme,
            onCheckedChange = {
                appState.isDarkTheme = it
            }
        )
    }
}