package com.example.iwawka.domain.usecases.profile

import com.example.iwawka.domain.models.Profile
import com.example.iwawka.domain.repositories.interfaces.ProfileRepository

class GetProfileUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(userId: String): Profile? {
        return profileRepository.getProfile(userId)
    }
}