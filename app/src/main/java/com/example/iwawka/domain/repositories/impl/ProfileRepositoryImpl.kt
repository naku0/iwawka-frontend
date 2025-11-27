package com.example.iwawka.data.repositories

import com.example.iwawka.clientStorage.TempProfilesStorage
import com.example.iwawka.domain.models.Profile
import com.example.iwawka.domain.repositories.interfaces.ProfileRepository

// ничего не трогал тут, кроме исправления соответствия имени класса
class ProfileRepositoryImpl : ProfileRepository {
    override suspend fun getProfile(userId: String): Profile? {
        return TempProfilesStorage.getProfile(userId)
    }

    override suspend fun updateProfile(profile: Profile): Boolean {
        return TempProfilesStorage.updateProfile(profile)
    }
}