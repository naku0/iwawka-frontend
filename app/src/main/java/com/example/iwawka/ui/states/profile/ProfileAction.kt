package com.example.iwawka.ui.states.profile

import com.example.iwawka.domain.models.Profile

sealed class ProfileAction {
    object LoadProfile : ProfileAction()
    data class ProfileLoaded(val profile: Profile?) : ProfileAction()
    data class ProfileLoadError(val error: String) : ProfileAction()
    data class UpdateProfile(val profile: Profile) : ProfileAction()
    object ClearError : ProfileAction()
}
