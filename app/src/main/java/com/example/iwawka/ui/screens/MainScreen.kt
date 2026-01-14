package com.example.iwawka.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.iwawka.model.clientStorage.SessionStore
import com.example.iwawka.ui.components.navigation.NavigationBar
import com.example.iwawka.ui.screens.chat.ChatDetailScreen
import com.example.iwawka.ui.screens.messages.MessagesScreen
import com.example.iwawka.ui.screens.profile.ProfileScreen
import com.example.iwawka.ui.screens.settings.SettingsScreen
import com.example.iwawka.ui.states.chat.ChatAction
import com.example.iwawka.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onLogout: () -> Unit
) {
    val chatsState by viewModel.chatsState.collectAsState()
    val sessionStore = SessionStore()
    var currentScreen by remember { mutableStateOf("messages") }
    var messagesSearchOpen by remember { mutableStateOf(false) }
    var messagesSearchText by remember { mutableStateOf("") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,

        bottomBar = {
            if (chatsState.selectedChat == null) {
                NavigationBar(
                    selectedItemId = currentScreen,
                    onItemSelected = { screenId ->
                        currentScreen = screenId
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
                        userId = sessionStore.myUserId
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