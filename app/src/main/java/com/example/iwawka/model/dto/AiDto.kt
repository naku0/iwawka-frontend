package com.example.iwawka.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class AiRequest(
    val messages: List<MessageItem>
)

@Serializable
data class MessageItem(
    val author: String,
    val message: String
)

@Serializable
data class AiResponse(
    val text: String
)