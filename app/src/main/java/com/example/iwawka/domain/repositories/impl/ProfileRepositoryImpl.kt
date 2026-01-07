package com.example.iwawka.domain.repositories.impl

import com.example.iwawka.domain.models.Profile
import com.example.iwawka.domain.models.User
import com.example.iwawka.domain.repositories.interfaces.ProfileRepository
import com.example.iwawka.model.api.IwawkaApi
import com.example.iwawka.model.api.Mappers

class ProfileRepositoryImpl(
    private val api: IwawkaApi
) : ProfileRepository {
    
    override suspend fun getProfile(userId: String): Profile? {
        val resp = api.getMe()
        if(!resp.success) return null

        val userDto = resp.data
        return Profile(
            user = User(
                id = userDto.id.toString(),
                name = userDto.name.toString(),
                phone = userDto.phone.toString(),
                status = "online",
            ),
            bio = userDto.bio ?: ""
        )
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