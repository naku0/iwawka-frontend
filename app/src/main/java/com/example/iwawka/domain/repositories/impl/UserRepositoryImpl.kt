package com.example.iwawka.domain.repositories.impl

import com.example.iwawka.clientStorage.TempUsersStorage
import com.example.iwawka.domain.models.User
import com.example.iwawka.domain.repositories.interfaces.UserRepository

class UserRepositoryImpl : UserRepository {
    override suspend fun getUser(userId: String): User? {
        return TempUsersStorage.getUser(userId)
    }

    override suspend fun getAllUsers(): List<User> {
        return TempUsersStorage.getAllUsers()
    }

    override suspend fun createUser(user: User): Boolean {
        return TempUsersStorage.createUser(user)
    }

    override suspend fun updateUser(user: User): Boolean {
        return TempUsersStorage.updateUser(user)
    }

    override suspend fun deleteUser(userId: String): Boolean {
        return TempUsersStorage.deleteUser(userId)
    }
}