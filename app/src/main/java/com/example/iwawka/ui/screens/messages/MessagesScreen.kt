package com.example.iwawka.ui.screens.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.iwawka.ui.components.chat.ChatListItem
import com.example.iwawka.ui.states.ErrorState
import com.example.iwawka.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(
    viewModel: MainViewModel,
    onChatClick: (String) -> Unit = {},

    searchEnabled: Boolean = false,
    searchText: String = "",
    onSearchTextChange: (String) -> Unit = {},
) {
    val cs = MaterialTheme.colorScheme
    val chatsState by viewModel.chatsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadChats()
    }

    val filteredChats = chatsState.chats
        .sortedByDescending { it.timestamp }
        .filter { chat ->
            val q = searchText.trim()
            if (!searchEnabled || q.isBlank()) true
            else chat.userName.contains(q, ignoreCase = true) ||
                chat.lastMessage.contains(q, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cs.background)
    ) {
        if (searchEnabled) {
            OutlinedTextField(
                value = searchText,
                onValueChange = onSearchTextChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                singleLine = true,
                placeholder = {
                    Text(
                        text = "Поиск в сообщениях",
                        color = cs.onSurface.copy(alpha = 0.5f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Поиск",
                        tint = cs.onSurface.copy(alpha = 0.65f)
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = cs.surface,
                    unfocusedContainerColor = cs.surface,
                    focusedBorderColor = cs.primary,
                    unfocusedBorderColor = cs.outline,
                    cursorColor = cs.primary,
                    focusedTextColor = cs.onSurface,
                    unfocusedTextColor = cs.onSurface
                )
            )
        } else {
            Spacer(Modifier.height(8.dp))
        }

        when {
            chatsState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = cs.primary)
                }
            }

            chatsState.error != null -> {
                ErrorState(
                    error = chatsState.error!!,
                    onRetry = { viewModel.loadChats() }
                )
            }

            filteredChats.isEmpty() -> {
                EmptyChatsState(searchText = if (searchEnabled) searchText else "")
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = filteredChats,
                        key = { it.id }
                    ) { chat ->
                        ChatListItem(
                            chat = chat,
                            onChatClick = {
                                onChatClick(chat.id)
                                viewModel.selectChat(chat)
                            }
                        )
                        DividerSoft()
                    }
                }
            }
        }
    }
}

@Composable
private fun DividerSoft() {
    val cs = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(cs.outline.copy(alpha = 0.08f))
    )
}

@Composable
private fun EmptyChatsState(searchText: String) {
    val cs = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (searchText.isBlank()) "Что-то тут пусто 👀" else "Ничего не найдено",
                style = MaterialTheme.typography.titleMedium,
                color = cs.onSurface.copy(alpha = 0.85f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (searchText.isBlank())
                    "Начните новый диалог или подождите сообщения"
                else
                    "Попробуйте изменить запрос",
                style = MaterialTheme.typography.bodyMedium,
                color = cs.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
