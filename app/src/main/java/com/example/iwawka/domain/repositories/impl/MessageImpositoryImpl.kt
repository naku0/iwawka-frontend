package com.example.iwawka.domain.repositories.impl

import com.example.iwawka.domain.models.Message
import com.example.iwawka.domain.repositories.interfaces.MessageRepository
import com.example.iwawka.model.clientStorage.TempStorage
import kotlinx.coroutines.flow.Flow

class MessageRepositoryImpl : MessageRepository {
    override suspend fun getMessages(chatId: String): List<Message> {
        return TempStorage.getMessages(chatId)
    }

    override suspend fun sendMessage(chatId: String, text: String): Message {
        return TempStorage.sendMessage(chatId, text)
    }

    override suspend fun markAsRead(messageId: String) {
        TempStorage.markAsRead(messageId)
    }

    override fun observeMessages(chatId: String): Flow<List<Message>> {
        return TempStorage.observeMessages(chatId)
    }
}