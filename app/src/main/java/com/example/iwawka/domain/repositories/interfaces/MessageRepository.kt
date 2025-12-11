package com.example.iwawka.domain.repositories.interfaces

import com.example.iwawka.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun getMessages(chatId: String): List<Message>
    suspend fun sendMessage(chatId: String, text: String): Message
    suspend fun markAsRead(messageId: String)
    fun observeMessages(chatId: String): Flow<List<Message>>
}