package com.example.iwawka.model.API

import com.example.iwawka.domain.models.Message
import com.example.iwawka.domain.models.Profile
import com.example.iwawka.domain.models.User
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Mappers {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private val displayDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())


    fun toDomain(dto: MessageDto, currentUserId: String): Message {
        return Message(
            id = dto.id.toString(),
            text = dto.content,
            timestamp = formatTimestamp(dto.created),
            isFromMe = dto.senderId.toString() == currentUserId,
            isRead = false
        )
    }

    fun toUpdateRequest(profile: Profile): UpdateUserRequest {
        return UpdateUserRequest(
            name = profile.user.name,
            email = null, // API doesn't have email in domain model
            bio = profile.bio,
            status = profile.user.status.toIntOrNull()
        )
    }

    private fun formatTimestamp(created: String): String {
        return try {
            val date = dateFormat.parse(created)
            date?.let { displayDateFormat.format(it) } ?: created
        } catch (e: Exception) {
            created
        }
    }
}

