package com.example.iwawka.domain.repositories.impl

import com.example.iwawka.domain.models.Chat

/*
interface ChatRepository {
    // Messages
    suspend fun sendMessage(message: Message): Result<Message>
    suspend fun deleteMessage(messageId: String): Result<Unit>
    suspend fun loadMessages(chatId: String, beforeTimestamp: Long? = null): Result<List<Message>>
    fun observeMessages(chatId: String): Flow<List<Message>>

    // Chats
    suspend fun getChats(): Result<List<Chat>>
    suspend fun getChat(chatId: String): Result<Chat>

    // Typing indicators
    suspend fun startTyping(chatId: String)
    suspend fun stopTyping(chatId: String)
    fun observeTypingUsers(chatId: String): Flow<Set<String>>

    // Connection
    suspend fun connect()
    suspend fun disconnect()
} */
object ChatRepository {
    private val sampleChats = listOf(
        Chat(
            id = "1",
            userName = "Анна Петрова",
            lastMessage = "Привет! Как дела?",
            timestamp = "10:30",
            unreadCount = 2,
            isOnline = true
        ),
        Chat(
            id = "2",
            userName = "Иван Сидоров",
            lastMessage = "Завтра встреча в 15:00",
            timestamp = "09:15",
            unreadCount = 0,
            isOnline = false
        ),
        Chat(
            id = "3",
            userName = "Мария Иванова",
            lastMessage = "Спасибо за помощь!",
            timestamp = "Вчера",
            unreadCount = 1,
            isOnline = true
        ),
        Chat(
            id = "4",
            userName = "Алексей Козлов",
            lastMessage = "Отправил тебе файл",
            timestamp = "Вчера",
            unreadCount = 0,
            isOnline = false
        ),
        Chat(
            id = "5",
            userName = "Екатерина Смирнова",
            lastMessage = "Жду твоего ответа",
            timestamp = "21 окт",
            unreadCount = 3,
            isOnline = true
        )
    )

    fun getChatById(chatId: String): Chat? {
        return sampleChats.find { it.id == chatId }
    }

    fun getAllChats(): List<Chat> {
        return sampleChats
    }
}