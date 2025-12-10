package com.example.iwawka.domain.repositories.impl

import com.example.iwawka.domain.models.Profile
import com.example.iwawka.domain.repositories.interfaces.ProfileRepository
import com.example.iwawka.model.API.IwawkaApi
import com.example.iwawka.model.API.Mappers

class ProfileRepositoryImpl(
    private val api: IwawkaApi
) : ProfileRepository {
    
    override suspend fun getProfile(userId: String): Profile? {
        // Note: API doesn't have getProfile endpoint, so this would need to be implemented
        // For now, returning null or you might want to cache profile locally
        return null
    }

    override suspend fun updateProfile(profile: Profile): Boolean {
        return try {
            val userId = profile.user.id.toIntOrNull() 
                ?: return false
            val request = Mappers.toUpdateRequest(profile)
            val response = api.updateUser(userId, request)
            response.success
        } catch (e: Exception) {
            false
        }
    }
}