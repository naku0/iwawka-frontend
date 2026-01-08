package com.example.iwawka.domain.models

data class Group(
    val id: String,
    val userName: String,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int = 0,
    val avatarUrl: String? = null,
    val isOnline: Boolean = false
)

data class Chat(
    val id: String,
    val userName: String,
    val isOnline: Boolean
)