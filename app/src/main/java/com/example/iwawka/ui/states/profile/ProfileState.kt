package com.example.iwawka.ui.states.profile

import com.example.iwawka.domain.models.Profile

data class ProfileState(
    val profile: Profile? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)