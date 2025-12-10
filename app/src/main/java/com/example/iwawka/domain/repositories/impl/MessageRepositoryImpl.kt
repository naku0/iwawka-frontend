package com.example.iwawka.domain.repositories.impl

import com.example.iwawka.domain.models.Message
import com.example.iwawka.domain.repositories.interfaces.MessageRepository
import com.example.iwawka.model.API.IwawkaApi
import com.example.iwawka.model.API.Mappers
import com.example.iwawka.model.API.SendMessageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MessageRepositoryImpl(
    private val api: IwawkaApi,
    private val currentUserId: String
) : MessageRepository {

    override suspend fun sendMessage(chatId: String, content: String): Result<Message> {
        return try {
            val request = SendMessageRequest(
                chatId = chatId.toIntOrNull() ?: return Result.failure(IllegalArgumentException("Invalid chatId")),
                content = content
            )
            val response = api.sendMessage(request)
            if (response.success) {
                // After sending, we need to get the message back or create a domain model
                // For now, we'll create a temporary message
                val message = Message(
                    id = System.currentTimeMillis().toString(),
                    text = content,
                    senderId = currentUserId,
                    chatId = chatId,
                    timestamp = "Только что",
                    isFromMe = true,
                    isRead = false
                )
                Result.success(message)
            } else {
                Result.failure(Exception("Failed to send message"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMessages(chatId: String): Result<List<Message>> {
        return try {
            val chatIdInt = chatId.toIntOrNull() ?: return Result.failure(IllegalArgumentException("Invalid chatId"))
            val response = api.getMessages(chatIdInt)
            if (response.success) {
                val messages = response.data.map { Mappers.toDomain(it, currentUserId) }
                Result.success(messages)
            } else {
                Result.failure(Exception("Failed to get messages"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteMessage(messageId: String): Result<Unit> {
        return try {
            val messageIdInt = messageId.toIntOrNull() ?: return Result.failure(IllegalArgumentException("Invalid messageId"))
            val response = api.deleteMessage(messageIdInt)
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete message"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeMessages(chatId: String): Flow<List<Message>> = flow {
        while (true) {
            getMessages(chatId).getOrNull()?.let { emit(it) }
            delay(5000) // Poll every 5 seconds
        }
    }
}

