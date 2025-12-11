// data/repositories/ProfileRepositoryImpl.kt
package com.example.iwawka.data.repositories

import com.example.iwawka.model.clientStorage.TempStorage
import com.example.iwawka.domain.models.Profile
import com.example.iwawka.domain.repositories.interfaces.ProfileRepository

class ProfileRepositoryImpl : ProfileRepository {
    override suspend fun getProfile(userId: String): Profile? {
        return TempStorage.getProfile(userId)
    }

    override suspend fun updateProfile(profile: Profile): Boolean {
        return TempStorage.updateProfile(profile)
    }
}