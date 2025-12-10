package com.example.iwawka.domain.repositories.impl

import com.example.iwawka.domain.models.Chat
import com.example.iwawka.domain.repositories.interfaces.ChatRepository
import com.example.iwawka.model.API.CreateChatRequest
import com.example.iwawka.model.API.IwawkaApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ChatRepositoryImpl(
    private val api: IwawkaApi
) : ChatRepository {

    override suspend fun getChats(): Result<List<Chat>> {
        // Note: API doesn't have getChats endpoint, so this would need to be implemented
        // For now, returning empty list or you might want to cache chats locally
        return Result.success(emptyList())
    }

    override suspend fun getChat(chatId: String): Result<Chat> {
        // Note: API doesn't have getChat endpoint, so this would need to be implemented
        // For now, returning error
        return Result.failure(NotImplementedError("getChat endpoint not available in API"))
    }

    override suspend fun createChat(memberIds: List<String>): Result<String> {
        return try {
            val request = CreateChatRequest(
                memberIds = memberIds.mapNotNull { it.toIntOrNull() }
            )
            val response = api.createChat(request)
            if (response.success) {
                Result.success(response.data.chatId.toString())
            } else {
                Result.failure(Exception("Failed to create chat"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeChats(): Flow<List<Chat>> = flow {
        while (true) {
            getChats().getOrNull()?.let { emit(it) }
            delay(10000) // Poll every 10 seconds
        }
    }
}
