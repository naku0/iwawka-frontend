package com.example.iwawka.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Rtt
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.iwawka.di.AppModule
import com.example.iwawka.domain.models.Chat
import com.example.iwawka.ui.components.messages.MessageBubble
import com.example.iwawka.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    chat: Chat,
    onBackClick: () -> Unit,
    viewModel: MainViewModel
) {
    var messageText by remember { mutableStateOf("") }
    val messagesState by viewModel.messageState.collectAsState()

    var selectionMode by remember { mutableStateOf(false) }
    var selectedIds by remember { mutableStateOf(setOf<String>()) }

    val explanationState by viewModel.explanationState.collectAsState()

    val myId = remember { AppModule.currentUserId.toInt() }

    LaunchedEffect(chat.id) {
        viewModel.loadMessages(chat.id)
        viewModel.observeMessages(chat.id)
        viewModel.markChatAsRead(chatId = chat.id)
    }

    LaunchedEffect(chat.id, messagesState.messages) {
        val lastIncoming = messagesState.messages
            .asSequence()
            .filter { !it.isFromMe }
            .mapNotNull { msg -> msg.id.toIntOrNull()?.let { id -> id to msg } }
            .maxByOrNull { it.first }
        lastIncoming?.second?.let{ msg ->
            viewModel.markMessageAsRead(chat.id, msg.id)
        }
    }

    DisposableEffect(chat.id) {
        onDispose {
            viewModel.stopObservingMessages()
        }
    }

    val cs = MaterialTheme.colorScheme
    val fieldShape = RoundedCornerShape(12.dp)

    fun toggleSelect(id: String) {
        selectedIds = if (selectedIds.contains(id)) selectedIds - id else selectedIds + id
        if (selectedIds.isEmpty()) selectionMode = false
    }

    fun startSelectionWith(id: String) {
        selectionMode = true
        selectedIds = setOf(id)
    }

    fun clearSelection() {
        selectionMode = false
        selectedIds = emptySet()
    }

    fun reply() {
        // TODO: Реализовать ответ
    }

    fun delete() {
        // TODO: Реализовать удаление
        viewModel.deleteMessages()
        clearSelection()
    }

    fun explain() {
        val selectedMessages = messagesState.messages
            .filter { it.id in selectedIds }
            .sortedBy { it.timestamp }

        if (selectedMessages.isNotEmpty()) {
            viewModel.explain(selectedMessages)
        }
    }

    fun copy() {
        // TODO: Реализовать копирование
        clearSelection()
    }

    Scaffold(
        containerColor = cs.background,
        topBar = {
            if (selectionMode) {
                SelectionTopBar(
                    selectedCount = selectedIds.size,
                    onClearSelection = ::clearSelection,
                    onDeleteSelected = ::delete,
                    onForwardSelected = { /* TODO: переслать */ },
                    onCopySelected = ::copy,
                    onExplainSelected = ::explain
                )
            } else {
                ChatTopBar(
                    chat = chat,
                    onBackClick = onBackClick
                )
            }
        },
        bottomBar = {
            if (!selectionMode) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(cs.background)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(
                                text = "Введите сообщение...",
                                color = cs.onSurface.copy(alpha = 0.5f)
                            )
                        },
                        shape = fieldShape,
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

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        shape = CircleShape,
                        color = if (messageText.isNotBlank()) cs.primary else cs.surface,
                        tonalElevation = 0.dp,
                        shadowElevation = 0.dp
                    ) {
                        IconButton(
                            onClick = {
                                viewModel.sendMessage(chat.id, messageText)
                                messageText = ""
                            },
                            enabled = messageText.isNotBlank(),
                            modifier = Modifier.size(56.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Отправить",
                                tint = if (messageText.isNotBlank())
                                    cs.onPrimary
                                else
                                    cs.onSurface.copy(alpha = 0.35f)
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(cs.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            reverseLayout = true
        ) {
            items(
                items = messagesState.messages.reversed(),
                key = { message -> message.id }
            ) { message ->
                MessageBubble(
                    message = message,
                    selectionMode = selectionMode,
                    isSelected = selectedIds.contains(message.id),
                    onClick = { msg ->
                        if (selectionMode) {
                            toggleSelect(msg.id)
                        }
                    },
                    onLongClick = { msg ->
                        startSelectionWith(msg.id)
                    },
                    onCopy = { copy() },
                    onDelete = { delete() },
                    onExplain = { explain() },
                    onReply = { reply() }
                )
            }
        }
    }

    if (explanationState.showDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideExplanationDialog() },
            title = {
                Text(
                    text = if (explanationState.error != null) "Ошибка" else "Объяснение",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                if (explanationState.isLoading) {
                    CircularProgressIndicator()
                } else if (explanationState.error != null) {
                    Text(explanationState.error!!)
                } else {
                    Text(explanationState.explanation)
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.hideExplanationDialog() }
                ) {
                    Text("OK")
                }
            }
        )
    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(chat: Chat, onBackClick: () -> Unit) {
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
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад"
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionTopBar(
    selectedCount: Int,
    onClearSelection: () -> Unit,
    onDeleteSelected: () -> Unit,
    onForwardSelected: () -> Unit,
    onCopySelected: () -> Unit,
    onExplainSelected: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Выбрано: $selectedCount",
                style = MaterialTheme.typography.titleMedium
            )
        },
        navigationIcon = {
            IconButton(onClick = onClearSelection) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Отменить выделение"
                )
            }
        },
        actions = {
            if (selectedCount == 1) {
                IconButton(onClick = onCopySelected) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Копировать"
                    )
                }
            }

            IconButton(onClick = onForwardSelected) {
                Icon(
                    imageVector = Icons.Filled.Reply,
                    contentDescription = "Переслать"
                )
            }

            IconButton(onClick = onDeleteSelected) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Удалить"
                )
            }

            IconButton(onClick = onExplainSelected) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Rtt,
                    contentDescription = "Объяснить"
                )
            }
        }
    )
}