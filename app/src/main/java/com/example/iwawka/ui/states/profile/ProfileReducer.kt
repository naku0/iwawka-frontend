package com.example.iwawka.ui.states.profile

class ProfileReducer {
    fun reduce(currentState: ProfileState, action: ProfileAction): ProfileState {
        return when (action) {
            is ProfileAction.LoadProfile -> {
                currentState.copy(
                    isLoading = true,
                    error = null
                )
            }
            is ProfileAction.ProfileLoaded -> {
                currentState.copy(
                    profile = action.profile,
                    isLoading = false,
                    error = if (action.profile == null) "Профиль не найден" else null
                )
            }
            is ProfileAction.ProfileLoadError -> {
                currentState.copy(
                    isLoading = false,
                    error = action.error
                )
            }
            is ProfileAction.UpdateProfile -> {
                currentState.copy(
                    profile = action.profile
                )
            }
            is ProfileAction.ClearError -> {
                currentState.copy(
                    error = null
                )
            }
        }
    }
}