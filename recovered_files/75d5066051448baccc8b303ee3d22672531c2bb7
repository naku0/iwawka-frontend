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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.iwawka.domain.models.Message
import com.example.iwawka.ui.components.messages.MessageBubble

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(userName: String, onBackClick: () -> Unit) {
    var messageText by remember { mutableStateOf("") }
    val messages = remember { generateSampleMessages() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = userName)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        },
        bottomBar = {
            // Поле ввода сообщения
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Введите сообщение...") },
                    shape = MaterialTheme.shapes.large,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        // TODO: Отправка сообщения
                        messageText = ""
                    },
                    enabled = messageText.isNotBlank(),
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Отправить",
                        tint = if (messageText.isNotBlank()) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            reverseLayout = true
        ) {
            items(
                items = messages.reversed(),
                key = { message -> message.id }
            ) { message ->
                MessageBubble(message = message)
            }
        }
    }
}

// Функция для генерации тестовых сообщений
fun generateSampleMessages(): List<Message> = listOf(
    Message(
        id = "1",
        text = "Привет! Как дела?",
        timestamp = "10:25",
        isFromMe = false,
        senderId = "user1",
        chatId = "chat1",
        isSent = true,
        isRead = true
    ),
    Message(
        id = "2",
        text = "Привет! Всё отлично, спасибо! А у тебя как?",
        timestamp = "10:26",
        isFromMe = true,
        senderId = "user2",
        chatId = "chat1",
        isSent = true,
        isRead = true
    ),
    Message(
        id = "3",
        text = "Тоже всё хорошо! Хочешь встретиться завтра?",
        timestamp = "10:27",
        isFromMe = false,
        senderId = "user1",
        chatId = "chat1",
        isSent = true,
        isRead = true
    )
)