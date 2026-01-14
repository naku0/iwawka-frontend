package com.example.iwawka.domain.models

import Attachment
import kotlinx.serialization.Serializable

/*
@Serializable
data class Message(
    val id: String,
    val text: String,
    val senderId: String,
    val chatId: String,
    val timestamp: String,
    val replyToId: String? = null,
    val attachment: Attachment? = null,
    val isSent: Boolean = true,
    val isEdited: Boolean = false,
    val isFromMe: Boolean,
    val isRead: Boolean = false
)
*/

@Serializable
data class Message(
    val id: String,
    val text: String,
    val timestamp: String,
    val isFromMe: Boolean,
    var isRead: Boolean = false,
    val senderId: String,
    // Убрали пока не нужные поля: , chatId, replyToId, attachment, isSent, isEdited
)