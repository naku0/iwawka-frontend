package com.example.iwawka.domain.usecases.profile

import com.example.iwawka.domain.models.Profile
import com.example.iwawka.domain.repositories.interfaces.ProfileRepository

class UpdateProfileUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(profile: Profile): Boolean {
        return profileRepository.updateProfile(profile)
    }
}