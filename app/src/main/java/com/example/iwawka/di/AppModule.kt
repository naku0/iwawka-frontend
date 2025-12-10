package com.example.iwawka.di

import com.example.iwawka.domain.repositories.impl.ChatRepositoryImpl
import com.example.iwawka.domain.repositories.impl.MessageRepositoryImpl
import com.example.iwawka.domain.repositories.impl.ProfileRepositoryImpl
import com.example.iwawka.domain.usecases.profile.GetProfileUseCase
import com.example.iwawka.domain.usecases.profile.UpdateProfileUseCase
import com.example.iwawka.model.API.IwawkaApi
import com.example.iwawka.ui.viewmodel.MainViewModel

object AppModule {
    private val api: IwawkaApi by lazy {
        IwawkaApi() // You can configure baseUrl here if needed
    }

    private val currentUserId: String by lazy {
        "1" // TODO: Get from auth/session
    }

    private val profileRepository: com.example.iwawka.domain.repositories.interfaces.ProfileRepository
        by lazy { ProfileRepositoryImpl(api) }

    private val messageRepository: com.example.iwawka.domain.repositories.interfaces.MessageRepository
        by lazy { MessageRepositoryImpl(api, currentUserId) }

    private val chatRepository: com.example.iwawka.domain.repositories.interfaces.ChatRepository
        by lazy { ChatRepositoryImpl(api) }

    private val getProfileUseCase: GetProfileUseCase
        get() = GetProfileUseCase(profileRepository)

    private val updateProfileUseCase: UpdateProfileUseCase
        get() = UpdateProfileUseCase(profileRepository)

    fun provideMainViewModel(): MainViewModel {
        return MainViewModel(getProfileUseCase, updateProfileUseCase)
    }
}