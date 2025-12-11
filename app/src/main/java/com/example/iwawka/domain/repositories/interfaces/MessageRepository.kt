package com.example.iwawka.domain.repositories.interfaces

import com.example.iwawka.domain.models.Message
import kotlinx.coroutines.flow.Flow


interface MessageRepository {
    suspend fun sendMessage(chatId: String, content: String): Result<Message>
    suspend fun getMessages(chatId: String): Result<List<Message>>
    suspend fun deleteMessage(messageId: String): Result<Unit>
    suspend fun markAsRead(messageId: String): Result<Unit>
    fun observeMessages(chatId: String): Flow<List<Message>>
}

