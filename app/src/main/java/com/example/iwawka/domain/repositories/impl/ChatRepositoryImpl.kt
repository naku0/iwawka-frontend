package com.example.iwawka.domain.repositories.impl
import com.example.iwawka.domain.models.Chat
import com.example.iwawka.domain.repositories.interfaces.ChatRepository
import com.example.iwawka.model.clientStorage.TempStorage
import kotlinx.coroutines.flow.Flow

class ChatRepositoryImpl : ChatRepository {

    override suspend fun getChats(): List<Chat> {
        return TempStorage.getChats()
    }

    override suspend fun getChat(chatId: String): Chat? {
        return TempStorage.getChat(chatId)
    }

    override fun observeChats(): Flow<List<Chat>> {
        return TempStorage.observeChats()
    }
}