package com.example.iwawka.ui.states

import com.example.iwawka.domain.models.Message

data class NetworkState(
    val isConnected: Boolean = true,
    val isSyncing: Boolean = false,
    val pendingMessages: List<Message> = emptyList(),
    val typingUsers: Set<String> = emptySet()
)
