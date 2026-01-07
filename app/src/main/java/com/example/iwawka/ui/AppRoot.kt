package com.example.iwawka.ui

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.iwawka.di.AppModule
import com.example.iwawka.model.dto.LoginRequest
import com.example.iwawka.model.dto.RegisterRequest
import com.example.iwawka.ui.navigation.Screen
import com.example.iwawka.ui.navigation.rememberNavState
import com.example.iwawka.ui.screens.MainScreen
import com.example.iwawka.ui.screens.auth.LoginScreen
import com.example.iwawka.ui.screens.auth.RegisterScreen
import kotlinx.coroutines.launch

@Composable
fun AppRoot() {
    val nav = rememberNavState(
        start = Screen.Login
    )

    val api = remember { AppModule.provideApi() }
    val tokenStorage = remember { AppModule.provideTokenStorage() }
    val viewModel = remember { AppModule.provideMainViewModel() }

    var isLoading by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    when (nav.current) {
        Screen.Login -> LoginScreen(
            isLoading = isLoading,
            errorMessage = errorMessage,
            onForgotPasswordClick = {
                // TODO: потом
            },
            onRegisterClick = {
                errorMessage = null
                nav.navigate(Screen.Register)
            },
            onLoginClick = { email, password ->
                scope.launch {
                    isLoading = true
                    errorMessage = null
                    try {
                        val resp = api.login(LoginRequest(email, password))
                        if (resp.success) {
                            tokenStorage.saveTokens(
                                resp.data.accessToken,
                                resp.data.refreshToken,
                                resp.data.expiresIn
                            )
                            nav.navigate(Screen.Main)
                        } else {
                            errorMessage = resp.message ?: "Ошибка входа"
                        }
                    } catch (e: Exception) {
                        errorMessage = e.message ?: "Ошибка сети"
                    } finally {
                        isLoading = false
                    }
                }
            }
        )

        Screen.Register -> RegisterScreen(
            isLoading = isLoading,
            errorMessage = errorMessage,
            onBack = {
                errorMessage = null
                nav.back()
            },
            onRegisterClick = { username, email, password ->
                scope.launch {
                    isLoading = true
                    errorMessage = null
                    try {
                        val resp = api.register(RegisterRequest(username, email, password))
                        if (resp.success) {
                            tokenStorage.saveTokens(
                                resp.data.accessToken,
                                resp.data.refreshToken,
                                resp.data.expiresIn
                            )
                            nav.navigate(Screen.Main)
                        } else {
                            errorMessage = resp.message ?: "Ошибка регистрации"
                        }
                    } catch (e: Exception) {
                        errorMessage = e.message ?: "Ошибка сети"
                    } finally {
                        isLoading = false
                    }
                }
            }
        )


        Screen.Main -> MainScreen(
            viewModel = viewModel,
            onLogout = {
                tokenStorage.clearTokens()
                nav.logout()
            }
        )
    }
}
