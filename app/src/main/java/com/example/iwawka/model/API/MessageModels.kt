package com.example.iwawka.model.api

import kotlinx.serialization.Serializable

@Serializable
data class SendMessageRequest(
    val chatId: Int,
    val content: String
)

@Serializable
data class MessageDto(
    val id: Int,
    val chatId: Int,
    val content: String,
    val created: String,
    val senderId: Int,
    val isRead: Boolean = false
)

@Serializable
data class ChatDto(
    val id: String,
    val userName: String,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int = 0,
    val avatarUrl: String? = null,
    val isOnline: Boolean = false
)

