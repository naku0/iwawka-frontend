package com.example.iwawka.di

import com.example.iwawka.domain.usecases.profile.GetProfileUseCase
import com.example.iwawka.domain.usecases.profile.UpdateProfileUseCase
import com.example.iwawka.ui.viewmodel.MainViewModel

object AppModule {
    private val profileRepository: com.example.iwawka.domain.repositories.interfaces.ProfileRepository
            by lazy { com.example.iwawka.data.repositories.ProfileRepositoryImpl() }

    private val getProfileUseCase: GetProfileUseCase
        get() = GetProfileUseCase(profileRepository)

    private val updateProfileUseCase: UpdateProfileUseCase
        get() = UpdateProfileUseCase(profileRepository)

    fun provideMainViewModel(): MainViewModel {
        return MainViewModel(getProfileUseCase, updateProfileUseCase)
    }
}