package com.example.iwawka.domain.repositories.interfaces
import com.example.iwawka.domain.models.Chat
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getChats(): List<Chat>
    suspend fun getChat(chatId: String): Chat?
    fun observeChats(): Flow<List<Chat>>
}