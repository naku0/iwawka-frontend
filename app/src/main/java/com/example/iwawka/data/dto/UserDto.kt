package com.example.iwawka.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    val name: String,
    val phone: String,
)

@Serializable
data class LoginRequest(
    val phone: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val token: String,
    val user: UserDto
)