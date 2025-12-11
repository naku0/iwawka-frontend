package com.example.iwawka.ui.states.message

import com.example.iwawka.domain.models.Message

data class MessageState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSending: Boolean = false
)