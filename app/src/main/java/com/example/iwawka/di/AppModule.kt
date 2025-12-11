package com.example.iwawka.di

import com.example.iwawka.data.repositories.ProfileRepositoryImpl
import com.example.iwawka.domain.repositories.impl.ChatRepositoryImpl
import com.example.iwawka.domain.repositories.impl.MessageRepositoryImpl
import com.example.iwawka.domain.repositories.interfaces.ChatRepository
import com.example.iwawka.domain.repositories.interfaces.MessageRepository
import com.example.iwawka.domain.repositories.interfaces.ProfileRepository
import com.example.iwawka.domain.usecases.chat.GetChatUseCase
import com.example.iwawka.domain.usecases.chat.GetChatsUseCase
import com.example.iwawka.domain.usecases.chat.ObserveChatsUseCase
import com.example.iwawka.domain.usecases.message.GetMessagesUseCase
import com.example.iwawka.domain.usecases.message.ObserveMessagesUseCase
import com.example.iwawka.domain.usecases.message.SendMessageUseCase
import com.example.iwawka.domain.usecases.profile.GetProfileUseCase
import com.example.iwawka.domain.usecases.profile.UpdateProfileUseCase
import com.example.iwawka.ui.viewmodel.MainViewModel

object AppModule {
    // Repositories
    private val profileRepository: ProfileRepository by lazy {
        ProfileRepositoryImpl()
    }

    private val chatRepository: ChatRepository by lazy {
        ChatRepositoryImpl()
    }

    private val messageRepository: MessageRepository by lazy {
        MessageRepositoryImpl()
    }

    // Domain Layer - Profile
    private val getProfileUseCase: GetProfileUseCase by lazy {
        GetProfileUseCase(profileRepository)
    }

    private val updateProfileUseCase: UpdateProfileUseCase by lazy {
        UpdateProfileUseCase(profileRepository)
    }

    // Domain Layer - Chats
    private val getChatsUseCase: GetChatsUseCase by lazy {
        GetChatsUseCase(chatRepository)
    }

    private val getChatUseCase: GetChatUseCase by lazy {
        GetChatUseCase(chatRepository)
    }

    private val observeChatsUseCase: ObserveChatsUseCase by lazy {
        ObserveChatsUseCase(chatRepository)
    }

    private val getMessagesUseCase: GetMessagesUseCase by lazy {
        GetMessagesUseCase(messageRepository)
    }

    private val sendMessageUseCase: SendMessageUseCase by lazy {
        SendMessageUseCase(messageRepository)
    }

    private val observeMessagesUseCase: ObserveMessagesUseCase by lazy {
        ObserveMessagesUseCase(messageRepository)
    }

    fun provideMainViewModel(): MainViewModel {
        return MainViewModel(
            getProfileUseCase = getProfileUseCase,
            updateProfileUseCase = updateProfileUseCase,
            getChatsUseCase = getChatsUseCase,
            getChatUseCase = getChatUseCase,
            observeChatsUseCase = observeChatsUseCase,
            getMessagesUseCase = getMessagesUseCase,
            sendMessageUseCase = sendMessageUseCase,
            observeMessagesUseCase = observeMessagesUseCase,
        )
    }
}