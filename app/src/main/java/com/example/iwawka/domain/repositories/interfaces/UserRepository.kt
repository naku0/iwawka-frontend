package com.example.iwawka.domain.repositories.interfaces

import com.example.iwawka.domain.models.User

interface UserRepository {
    suspend fun getUser(userId: String): User?
    suspend fun getAllUsers(): List<User>
    suspend fun createUser(user: User): Boolean
    suspend fun updateUser(user: User): Boolean
    suspend fun deleteUser(userId: String): Boolean
}