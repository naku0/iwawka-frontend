package com.example.iwawka.domain.repositories.interfaces

import com.example.iwawka.domain.models.Chat
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getChats(): Result<List<Chat>>
    suspend fun getChat(chatId: String): Result<Chat>
    suspend fun createChat(memberIds: List<String>): Result<String> // Returns chatId
    fun observeChats(): Flow<List<Chat>>
}