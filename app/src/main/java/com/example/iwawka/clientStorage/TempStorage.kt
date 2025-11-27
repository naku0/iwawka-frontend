// clientStorage/TempStorage.kt
package com.example.iwawka.clientStorage

import com.example.iwawka.domain.models.Profile
import com.example.iwawka.domain.models.User
import kotlinx.coroutines.delay

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
}