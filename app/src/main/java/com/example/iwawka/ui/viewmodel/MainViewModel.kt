package com.example.iwawka.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iwawka.domain.models.Profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.iwawka.domain.usecases.profile.GetProfileUseCase
import com.example.iwawka.domain.usecases.profile.UpdateProfileUseCase
import com.example.iwawka.ui.states.profile.ProfileAction
import com.example.iwawka.ui.states.profile.ProfileReducer
import com.example.iwawka.ui.states.profile.ProfileState
import kotlinx.coroutines.flow.update

class MainViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {

    private val profileReducer = ProfileReducer()
    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    fun dispatchProfileAction(action: ProfileAction) {
        _profileState.update { currentState ->
            profileReducer.reduce(currentState, action)
        }
    }

    fun loadProfile(userId: String) {
        viewModelScope.launch {
            dispatchProfileAction(ProfileAction.LoadProfile)
            try {
                val profile = getProfileUseCase(userId)
                dispatchProfileAction(ProfileAction.ProfileLoaded(profile))
            } catch (e: Exception) {
                dispatchProfileAction(ProfileAction.ProfileLoadError(e.message ?: "Ошибка загрузки"))
            }
        }
    }

    fun updateProfile(profile: Profile) {
        viewModelScope.launch {
            try {
                val success = updateProfileUseCase(profile)
                if (success) {
                    dispatchProfileAction(ProfileAction.UpdateProfile(profile))
                }
            } catch (e: Exception) {
                dispatchProfileAction(ProfileAction.ProfileLoadError("Ошибка обновления"))
            }
        }
    }

    fun clearError() {
        dispatchProfileAction(ProfileAction.ClearError)
    }
}