package com.example.iwawka.domain.repositories.interfaces
import com.example.iwawka.domain.models.Profile

interface ProfileRepository {
    suspend fun getProfile(userId: String): Profile?
    suspend fun updateProfile(profile: Profile): Boolean
}