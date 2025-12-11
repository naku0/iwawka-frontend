package com.example.iwawka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.lifecycle.lifecycleScope
import com.example.iwawka.model.clientStorage.TempStorage
import com.example.iwawka.di.AppModule
import com.example.iwawka.ui.screens.MainScreen
import com.example.iwawka.ui.theme.AppState
import com.example.iwawka.ui.theme.LocalAppState
import com.example.iwawka.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by lazy {
        AppModule.provideMainViewModel()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize AppModule with context
        AppModule.initialize(this)
        enableEdgeToEdge()
        lifecycleScope.launch {
            val curId = TempStorage.getCurrentUserId()
            viewModel.loadProfile(curId)
        }
        setContent {
            CompositionLocalProvider(
                LocalAppState provides remember{ AppState() }
            ) {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}