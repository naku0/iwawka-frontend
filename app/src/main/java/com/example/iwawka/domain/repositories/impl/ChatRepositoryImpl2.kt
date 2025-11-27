package com.example.iwawka.domain.repositories.impl

import com.example.iwawka.domain.models.Chat
import com.example.iwawka.domain.repositories.interfaces.ChatRepository2
import kotlinx.coroutines.delay

class ChatRepository2Impl : ChatRepository2 {
    // ну тут такая же тема
    private val chats = mutableListOf<Chat>().apply {
        add(Chat(
            id = "1",
            userName = "Анна Петрова",
            lastMessage = "Привет! Как дела?",
            timestamp = "10:30",
            unreadCount = 2,
            isOnline = true
        ))
        add(Chat(
            id = "2",
            userName = "Иван Сидоров",
            lastMessage = "Завтра встреча в 15:00",
            timestamp = "09:15",
            unreadCount = 0,
            isOnline = false
        ))
        add(Chat(
            id = "3",
            userName = "Мария Иванова",
            lastMessage = "Спасибо за помощь!",
            timestamp = "Вчера",
            unreadCount = 1,
            isOnline = true
        ))
        add(Chat(
            id = "4",
            userName = "Алексей Козлов",
            lastMessage = "Отправил тебе файл",
            timestamp = "Вчера",
            unreadCount = 0,
            isOnline = false
        ))
        add(Chat(
            id = "5",
            userName = "Екатерина Смирнова",
            lastMessage = "Жду твоего ответа",
            timestamp = "21 окт",
            unreadCount = 3,
            isOnline = true
        ))
    }

    override suspend fun getChat(chatId: String): Chat? {
        delay(300)
        return chats.find { it.id == chatId }
    }

    override suspend fun getAllChats(): List<Chat> {
        delay(300)
        return chats.toList()
    }

    override suspend fun createChat(chat: Chat): Boolean {
        delay(300)
        if (chats.any { it.id == chat.id }) {
            return false
        }
        chats.add(chat)
        return true
    }

    override suspend fun updateChat(chat: Chat): Boolean {
        delay(300)
        val index = chats.indexOfFirst { it.id == chat.id }
        if (index != -1) {
            chats[index] = chat
            return true
        }
        return false
    }

    override suspend fun deleteChat(chatId: String): Boolean {
        delay(300)
        return chats.removeIf { it.id == chatId }
    }

    override suspend fun getChatsByUser(userId: String): List<Chat> {
        delay(300)
        return chats.toList()
    }
}