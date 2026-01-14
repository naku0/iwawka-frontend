package com.example.iwawka.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.iwawka.domain.models.Chat
import com.example.iwawka.ui.components.navigation.NavItem
import com.example.iwawka.ui.components.navigation.NavigationBar
import com.example.iwawka.ui.screens.chat.ChatDetailScreen
import com.example.iwawka.ui.screens.messages.MessagesScreen
import com.example.iwawka.ui.screens.profile.ProfileScreen
import com.example.iwawka.ui.screens.settings.SettingsScreen
import com.example.iwawka.ui.states.chat.ChatAction
import com.example.iwawka.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onLogout: () -> Unit
) {
    val chatsState by viewModel.chatsState.collectAsState()

    var currentScreen by remember { mutableStateOf("messages") }
    var messagesSearchOpen by remember { mutableStateOf(false) }
    var messagesSearchText by remember { mutableStateOf("") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (chatsState.selectedChat == null) {
                // Показываем TopAppBar только на главных экранах
                if (currentScreen != "messages" || messagesSearchOpen) {
                    TopAppBar(
                        title = {
                            Text(
                                text = when (currentScreen) {
                                    "profile" -> "Профиль"
                                    "settings" -> "Настройки"
                                    "messages" -> if (messagesSearchOpen) "Поиск чатов" else "Сообщения"
                                    else -> "App"
                                }
                            )
                        }
                    )
                }
            } else {
                ChatTopBar(
                    chat = chatsState.selectedChat!!,
                    onBackClick = {
                        viewModel.dispatchChatAction(ChatAction.SelectChat(null))
                    }
                )
            }
        },
        bottomBar = {
            // Показываем нижнюю навигацию только на главных экранах
            if (chatsState.selectedChat == null) {
                NavigationBar(
                    selectedItemId = currentScreen,
                    onItemSelected = { screenId ->
                        currentScreen = screenId
                        // Закрываем поиск при переключении вкладок
                        if (screenId != "messages") {
                            messagesSearchOpen = false
                            messagesSearchText = ""
                        }
                    }
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
                    "profile" -> ProfileScreen(
                        viewModel = viewModel,
                    )
                    "settings" -> SettingsScreen(
                        onLogout = onLogout,
                    )
                    "messages" -> MessagesScreen(
                        onChatClick = { chatId ->
                            chatsState.chats
                                .find { it.id.toString() == chatId }
                                ?.let(viewModel::selectChat)
                        },
                        viewModel = viewModel,
                        searchEnabled = messagesSearchOpen,
                        searchText = messagesSearchText,
                        onSearchTextChange = { messagesSearchText = it },
                    )
                }
            }
        }
    }
}

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
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
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