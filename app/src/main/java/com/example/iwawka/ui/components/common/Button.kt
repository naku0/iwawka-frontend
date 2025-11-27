package com.example.iwawka.ui.components.common

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.iwawka.ui.theme.LocalAppState

@Composable
fun CounterButton(modifier: Modifier = Modifier){
    val appState = LocalAppState.current

    Button(
        onClick= {
            appState.counter++
        },
        modifier = modifier
    ) {
        Text(
            text = "Count: ${appState.counter}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}