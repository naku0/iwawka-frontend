package com.example.iwawka.model.api

import kotlinx.serialization.Serializable

@Serializable
data class CreateChatRequest(
    val memberIds: List<Int>
)

@Serializable
data class CreateChatResponse(
    val chatId: Int
)

@Serializable
data class UpdateUserRequest(
    val name: String? = null,
    val email: String? = null,
    val bio: String? = null,
    val status: Int? = null
)

