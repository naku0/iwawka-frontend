package com.example.iwawka.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

    LaunchedEffect(chat.id) {
        viewModel.loadMessages(chat.id)
        viewModel.observeMessages(chat.id)
    }

    DisposableEffect(chat.id) {
        onDispose {
            viewModel.stopObservingMessages()
        }
    }

    val cs = MaterialTheme.colorScheme
    val fieldShape = RoundedCornerShape(12.dp)

    Scaffold(
        containerColor = cs.background,
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(cs.background)
                    .padding(16.dp),
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
                            // TODO: Отправка сообщения
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
                MessageBubble(message = message)
            }
        }
    }
}
