// clientStorage/TempStorage.kt
package com.example.iwawka.model.clientStorage

import com.example.iwawka.domain.models.Chat
import com.example.iwawka.domain.models.Message
import com.example.iwawka.domain.models.Profile
import com.example.iwawka.domain.models.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.collections.find

object TempStorage {
    private val profiles = mutableMapOf<String, Profile>().apply {
        put("1", Profile(
            user = User(
                id = "1",
                name = "Иван Иванов",
                phone = "+7 (999) 123-45-67",
                status = "активный"
            ),
            bio = "Люблю путешествия и фотографию. Ищу интересные места для съёмки."
        ))
    }

    suspend fun getProfile(userId: String): Profile? {
        // Имитация задержки сети
        delay(500)
        return profiles[userId]
    }

    suspend fun updateProfile(profile: Profile): Boolean {
        delay(300)
        profiles[profile.user.id] = profile
        return true
    }

    // Простая функция для получения ID текущего пользователя
    suspend fun getCurrentUserId(): String {
        delay(100)
        return "1"
    }

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

    suspend fun getChats(): List<Chat> {
        delay(500) // имитация загрузки
        return sampleChats
    }

    suspend fun getChat(chatId: String): Chat? {
        delay(300)
        return sampleChats.find { it.id == chatId }
    }

    fun observeChats(): Flow<List<Chat>> = flow {
        while (true) {
            emit(sampleChats)
            delay(10000) // обновляем каждые 10 секунд (для демо)
        }
    }

    private val messages = mutableMapOf<String, MutableList<Message>>().apply {
        put("1", mutableListOf(
            Message(
                id = "1_1",
                text = "Привет! Как дела?",
                timestamp = "10:25",
                isFromMe = false,
                isRead = true
            ),
            Message(
                id = "1_2",
                text = "Привет! Всё отлично, спасибо! А у тебя как?",
                timestamp = "10:26",
                isFromMe = true,
                isRead = true
            ),
            Message(
                id = "1_3",
                text = "Тоже всё хорошо! Хочешь встретиться завтра?",
                timestamp = "10:27",
                isFromMe = false,
                isRead = true
            )
        ))
    }

    // Функции для работы с сообщениями
    suspend fun getMessages(chatId: String): List<Message> {
        delay(300)
        return messages[chatId] ?: emptyList()
    }

    suspend fun sendMessage(chatId: String, text: String): Message {
        delay(200)
        val message = Message(
            id = "${chatId}_${System.currentTimeMillis()}",
            text = text,
            timestamp = "Только что",
            isFromMe = true,
            isRead = false
        )

        if (!messages.containsKey(chatId)) {
            messages[chatId] = mutableListOf()
        }
        messages[chatId]?.add(message)
        return message
    }

    suspend fun markAsRead(messageId: String) {
        delay(100)
        messages.values.forEach { chatMessages ->
            chatMessages.find { it.id == messageId }?.isRead = true
        }
    }

    fun observeMessages(chatId: String): Flow<List<Message>> = flow {
        while (true) {
            emit(messages[chatId] ?: emptyList())
            delay(5000) // Обновляем каждые 5 секунд
        }
    }
}