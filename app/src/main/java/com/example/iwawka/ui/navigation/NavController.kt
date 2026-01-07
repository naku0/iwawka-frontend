package com.example.iwawka.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

enum class Screen {
    Login,
    Register,
    Main
}

@Stable
class NavState(start: Screen) {
    var current by mutableStateOf(start)
        private set

    fun navigate(screen: Screen) { current = screen }
    fun back() { current = Screen.Login }
    fun logout() { current = Screen.Login }
}

@Composable
fun rememberNavState(start: Screen): NavState {
    var saved by rememberSaveable { mutableStateOf(start) }

    val nav = remember { NavState(saved) }

    LaunchedEffect(saved) { nav.navigate(saved) }
    LaunchedEffect(nav.current) { saved = nav.current }

    return nav
}
