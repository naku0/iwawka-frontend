package com.example.iwawka.clientStorage

import com.example.iwawka.domain.models.User
import kotlinx.coroutines.delay

object TempUsersStorage {
    private val users = mutableMapOf<String, User>().apply {
        put("1", User(
            id = "1",
            name = "Иван Иванов",
            phone = "+7 (999) 123-45-67",
            status = "активный" // ну и че это за пиздец? В чем он активный?
        ))
        put("2", User(
            id = "2",
            name = "Мария Смирнова",
            phone = "+7 (999) 987-65-43",
            status = "не в сети"
        ))
    }

    suspend fun getUser(userId: String): User? {
        // Имитация задержки сети
        delay(500)
        return users[userId]
    }
    suspend fun getAllUsers(): List<User> {
        delay(300)
        return users.values.toList()
    }

    suspend fun updateUser(user: User): Boolean {
        delay(300)
        users[user.id] = user
        return true
    }

    suspend fun createUser(user: User): Boolean {
        delay(300)
        users[user.id] = user
        return true
    }

    suspend fun deleteUser(userId: String): Boolean {
        delay(300)
        return users.remove(userId) != null
    }
}