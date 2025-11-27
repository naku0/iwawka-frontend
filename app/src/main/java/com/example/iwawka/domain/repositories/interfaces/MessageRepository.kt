package com.example.iwawka.domain.repositories.interfaces

import com.example.iwawka.domain.models.Message
// todo alooooó
interface MessageRepository {
    suspend fun getMessage(messageId: String): Message?
    suspend fun getAllMessagesByChatId(chatId: String): List<Message>
    suspend fun sendMessage(message: Message): Boolean
    suspend fun updateMessage(message: Message): Boolean // мб лучше будет (messageId: String, message: Message): Boolean
    suspend fun deleteMessage(messageId: String): Boolean
    suspend fun getMessagesByUser(userId: String): List<Message>
}