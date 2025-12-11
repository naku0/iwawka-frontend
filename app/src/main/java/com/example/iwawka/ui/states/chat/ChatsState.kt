package com.example.iwawka.ui.states.chat

import com.example.iwawka.domain.models.Chat

data class ChatsState(
    val chats: List<Chat> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedChat: Chat? = null
)
