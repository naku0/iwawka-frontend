package com.example.iwawka.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AiRequest(
    val prompt: String,
    val user: UserDto
)

@Serializable
data class AiResponse(
    val text: String
)