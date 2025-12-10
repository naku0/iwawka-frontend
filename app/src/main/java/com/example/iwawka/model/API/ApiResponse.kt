package com.example.iwawka.model.API

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T
)

@Serializable
object EmptyData

