package com.example.iwawka.domain.repositories.interfaces

import com.example.iwawka.domain.models.Chat

interface ChatRepository2 {
    suspend fun getChat(chatId: String): Chat?
    suspend fun getAllChats(): List<Chat>
    suspend fun createChat(chat: Chat): Boolean
    suspend fun updateChat(chatId: String): Boolean
    suspend fun deleteChat(chatId: String): Boolean
    suspend fun getAllChatsByUserId(userId: String): List<Chat>
}