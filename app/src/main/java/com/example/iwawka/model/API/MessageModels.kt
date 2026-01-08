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
    val senderId: Int
)

@Serializable
data class ChatDto(
    val id: Int,
    val name: String,
    val isOnline: Boolean
)

