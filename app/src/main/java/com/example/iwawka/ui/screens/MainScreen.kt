package com.example.iwawka.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.iwawka.domain.models.Chat
import com.example.iwawka.ui.components.navigation.AppDrawer
import com.example.iwawka.ui.screens.chat.ChatDetailScreen
import com.example.iwawka.ui.screens.settings.SettingsScreen
import com.example.iwawka.ui.screens.messages.MessagesScreen
import com.example.iwawka.ui.screens.profile.ProfileScreen
import com.example.iwawka.ui.states.chat.ChatAction
import com.example.iwawka.ui.theme.DarkColorScheme
import com.example.iwawka.ui.theme.LightColorScheme
import com.example.iwawka.ui.theme.LocalAppState
import com.example.iwawka.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch
// ui/screens/MainScreen.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val appState = LocalAppState.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var currentScreen by remember { mutableStateOf("messages") }

    val profileState by viewModel.profileState.collectAsState()
    val chatsState by viewModel.chatsState.collectAsState()

    val currentTheme = if (appState.isDarkTheme) DarkColorScheme else LightColorScheme

    LaunchedEffect(Unit) {
        viewModel.loadProfile("1")
    }

    MaterialTheme(colorScheme = currentTheme) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                AppDrawer(
                    currentScreen = currentScreen,
                    onItemClick = { screen ->
                        currentScreen = screen
                        scope.launch { drawerState.close() }
                    },
                    user = profileState.profile?.user
                )
            }
        ) {
            Scaffold(
                topBar = {
                    // Показываем TopAppBar только когда НЕ открыт чат
                    if (chatsState.selectedChat == null) {
                        TopAppBar(
                            title = {
                                Text(
                                    text = when (currentScreen) {
                                        "profile" -> "Профиль"
                                        "settings" -> "Настройки"
                                        "messages" -> "Сообщения"
                                        else -> "App"
                                    }
                                )
                            },
                            navigationIcon = {
                                IconButton(
                                    onClick = {
                                        scope.launch { drawerState.open() }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Open menu"
                                    )
                                }
                            }
                        )
                    } else {
                        // TopBar для экрана чата
                        ChatTopBar(
                            chat = chatsState.selectedChat!!,
                            onBackClick = { viewModel.dispatchChatAction(ChatAction.SelectChat(null)) }
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    if (chatsState.selectedChat != null) {
                        ChatDetailScreen(
                            chat = chatsState.selectedChat!!,
                            onBackClick = {
                                viewModel.dispatchChatAction(ChatAction.SelectChat(null))
                            },
                            viewModel = viewModel,
                        )
                    } else {
                        when (currentScreen) {
                            "profile" -> ProfileScreen(viewModel = viewModel)
                            "settings" -> SettingsScreen()
                            "messages" -> MessagesScreen(
                                onChatClick = { chatId ->
                                    // Находим чат по ID и выбираем его
                                    val chat = chatsState.chats.find { it.id == chatId }
                                    chat?.let { viewModel.selectChat(it) }
                                },
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

// TopBar для экрана чата
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatTopBar(chat: Chat, onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            Column {
                Text(chat.userName)
                Text(
                    text = if (chat.isOnline) "online" else "offline",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Назад"
                )
            }
        }
    )
}