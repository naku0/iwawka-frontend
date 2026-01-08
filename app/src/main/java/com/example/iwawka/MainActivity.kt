package com.example.iwawka

import android.os.Bundle
import androidx.compose.ui.graphics.toArgb
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.iwawka.di.AppModule
import com.example.iwawka.ui.AppRoot
import com.example.iwawka.ui.theme.AppState
import com.example.iwawka.ui.theme.IwawkaTheme
import com.example.iwawka.ui.theme.LocalAppState
import com.example.iwawka.ui.theme.ThemeMode

class MainActivity : ComponentActivity() {

    private val url: String = "http://10.0.2.2:8080"

    override fun onCreate(savedInstanceState: Bundle?) {
        android.util.Log.e("IWAWKA_BOOT", "0) before super")
        super.onCreate(savedInstanceState)
        android.util.Log.e("IWAWKA_BOOT", "1) after super")

        try {
            android.util.Log.e("IWAWKA_BOOT", "2) before initialize")
            AppModule.initialize(this, url)
            android.util.Log.e("IWAWKA_BOOT", "3) after initialize")
        } catch (t: Throwable) {
            android.util.Log.e("IWAWKA_BOOT", "CRASH in initialize", t)
            throw t
        }
        
        enableEdgeToEdge()

        setContent {
            var savedThemeMode by rememberSaveable { mutableStateOf(ThemeMode.SYSTEM) }
            var savedAccentArgb by rememberSaveable { mutableIntStateOf(androidx.compose.ui.graphics.Color.Black.toArgb()) }
            var savedRecentAccents by rememberSaveable { mutableStateOf(listOf<Int>()) }

            val appState = remember {
                AppState().also {
                    it.themeMode = savedThemeMode
                    it.accentArgb = savedAccentArgb
                    it.recentAccents.clear()
                    it.recentAccents.addAll(savedRecentAccents)
                }
            }

            // сохраняем изменения из appState -> saved*
            LaunchedEffect(appState.themeMode) {
                savedThemeMode = appState.themeMode
            }
            LaunchedEffect(appState.accentArgb) {
                appState.pushRecent(appState.accentArgb)
                savedAccentArgb = appState.accentArgb
                savedRecentAccents = appState.recentAccents.toList()
            }

            val darkTheme = when (appState.themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.DARK -> true
                ThemeMode.LIGHT -> false
            }

            CompositionLocalProvider(LocalAppState provides appState) {
                IwawkaTheme(
                    darkTheme = darkTheme,
                    accent = appState.accentColor
                ) {
                    // ВАЖНО: корневой Surface задаёт фон всему приложению
                    androidx.compose.material3.Surface(
                        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AppRoot()
                    }
                }
            }
        }

    }
}
