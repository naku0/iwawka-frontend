package com.example.iwawka.di

import android.content.Context
import com.example.iwawka.domain.repositories.impl.ChatRepositoryImpl
import com.example.iwawka.domain.repositories.impl.MessageRepositoryImpl
import com.example.iwawka.domain.repositories.impl.ProfileRepositoryImpl
import com.example.iwawka.domain.usecases.chat.GetChatUseCase
import com.example.iwawka.domain.usecases.chat.GetChatsUseCase
import com.example.iwawka.domain.usecases.chat.ObserveChatsUseCase
import com.example.iwawka.domain.usecases.message.GetMessagesUseCase
import com.example.iwawka.domain.usecases.message.MarkAsReadUseCase
import com.example.iwawka.domain.usecases.message.ObserveMessagesUseCase
import com.example.iwawka.domain.usecases.message.SendMessageUseCase
import com.example.iwawka.domain.usecases.profile.GetProfileUseCase
import com.example.iwawka.domain.usecases.profile.UpdateProfileUseCase
import com.example.iwawka.model.api.IwawkaApi
import com.example.iwawka.model.auth.TokenStorage
import com.example.iwawka.model.clientStorage.SessionStore
import com.example.iwawka.ui.viewmodel.MainViewModel

object AppModule {
    private var tokenStorage: TokenStorage? = null
    private var api: IwawkaApi? = null

    private val sessionStore: SessionStore = SessionStore()

    fun initialize(context: Context, baseUrl: String) {
        tokenStorage = TokenStorage(context)
        api = IwawkaApi(tokenStorage, baseUrl)
    }

    private fun getApi(): IwawkaApi {
        return api ?: throw IllegalStateException("AppModule not initialized. Call AppModule.initialize(context) first.")
    }

    private fun getTokenStorage(): TokenStorage {
        return tokenStorage ?: throw IllegalStateException("AppModule not initialized. Call AppModule.initialize(context) first.")
    }

    val currentUserId: String
        get() {
            // TODO: Extract from JWT token or get from API
            return "1"
        }

    private val profileRepository: com.example.iwawka.domain.repositories.interfaces.ProfileRepository
        get() = ProfileRepositoryImpl(getApi())

    private val messageRepository: com.example.iwawka.domain.repositories.interfaces.MessageRepository
        get() = MessageRepositoryImpl(getApi(), sessionStore)

    private val chatRepository: com.example.iwawka.domain.repositories.interfaces.ChatRepository
        get() = ChatRepositoryImpl(getApi())

    private val getProfileUseCase: GetProfileUseCase
        get() = GetProfileUseCase(profileRepository)

    private val updateProfileUseCase: UpdateProfileUseCase
        get() = UpdateProfileUseCase(profileRepository)

    private val getChatsUseCase: GetChatsUseCase
        get() = GetChatsUseCase(chatRepository)

    private val getChatUseCase: GetChatUseCase
        get() = GetChatUseCase(chatRepository)

    private val observeChatsUseCase: ObserveChatsUseCase
        get() = ObserveChatsUseCase(chatRepository)

    private val getMessagesUseCase: GetMessagesUseCase
        get() = GetMessagesUseCase(messageRepository)

    private val sendMessageUseCase: SendMessageUseCase
        get() = SendMessageUseCase(messageRepository)

    private val observeMessagesUseCase: ObserveMessagesUseCase
        get() = ObserveMessagesUseCase(messageRepository)

    private val markAsReadUseCase: MarkAsReadUseCase
        get() = MarkAsReadUseCase(messageRepository)

    fun provideMainViewModel(): MainViewModel {
        return MainViewModel(
            getProfileUseCase,
            updateProfileUseCase,
            getChatsUseCase,
            getChatUseCase,
            observeChatsUseCase,
            getMessagesUseCase,
            sendMessageUseCase,
            observeMessagesUseCase,
            markAsReadUseCase
        )
    }

    fun provideApi(): IwawkaApi = getApi()
    
    fun provideTokenStorage(): TokenStorage = getTokenStorage()

    fun isLoggedIn(): Boolean = provideTokenStorage().hasTokens()

    fun logout(){provideTokenStorage().clearTokens()}
}