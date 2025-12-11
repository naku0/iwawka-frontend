// ui/viewmodel/MainViewModel.kt
package com.example.iwawka.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iwawka.domain.usecases.chat.GetChatUseCase
import com.example.iwawka.domain.usecases.chat.GetChatsUseCase
import com.example.iwawka.domain.usecases.chat.ObserveChatsUseCase
import com.example.iwawka.domain.usecases.message.GetMessagesUseCase
import com.example.iwawka.domain.usecases.message.ObserveMessagesUseCase
import com.example.iwawka.domain.usecases.message.SendMessageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.iwawka.domain.usecases.profile.GetProfileUseCase
import com.example.iwawka.domain.usecases.profile.UpdateProfileUseCase
import com.example.iwawka.ui.states.chat.ChatAction
import com.example.iwawka.ui.states.chat.ChatsState
import com.example.iwawka.ui.states.profile.ProfileAction
import com.example.iwawka.ui.states.profile.ProfileReducer
import com.example.iwawka.ui.states.profile.ProfileState
import com.example.iwawka.ui.states.chat.ChatReducer
import com.example.iwawka.ui.states.message.MessageAction
import com.example.iwawka.ui.states.message.MessageReducer
import com.example.iwawka.ui.states.message.MessageState
import kotlinx.coroutines.flow.update

class MainViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val getChatsUseCase: GetChatsUseCase,
    private val getChatUseCase: GetChatUseCase,
    private val observeChatsUseCase: ObserveChatsUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val observeMessagesUseCase: ObserveMessagesUseCase

) : ViewModel() {

    private val profileReducer = ProfileReducer()
    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    private val chatReducer = ChatReducer()
    private val _chatsState = MutableStateFlow(ChatsState())
    val chatsState: StateFlow<ChatsState> = _chatsState.asStateFlow()

    private val messageReducer = MessageReducer()
    private val _messagesState = MutableStateFlow(MessageState())
    val messageState: StateFlow<MessageState> = _messagesState.asStateFlow()

    fun dispatchProfileAction(action: ProfileAction) {
        _profileState.update { currentState ->
            profileReducer.reduce(currentState, action)
        }
    }

    fun dispatchChatAction(action: ChatAction) {
        _chatsState.update { currentState ->
            chatReducer.reduce(currentState, action)
        }
    }

    fun dispatchMessageAction(action: MessageAction) {
        _messagesState.update { currentState ->
            messageReducer.reduce(currentState, action)
        }
    }


    fun loadProfile(userId: String) {
        viewModelScope.launch {
            dispatchProfileAction(ProfileAction.LoadProfile)
            try {
                val profile = getProfileUseCase(userId)
                profile?.let {
                    dispatchProfileAction(ProfileAction.ProfileLoaded(it))
                } ?: dispatchProfileAction(ProfileAction.ProfileLoadError("Профиль не найден"))
            } catch (e: Exception) {
                dispatchProfileAction(ProfileAction.ProfileLoadError(e.message ?: "Ошибка загрузки"))
            }
        }
    }


    fun loadChats() {
        viewModelScope.launch {
            dispatchChatAction(ChatAction.LoadChats)
            try {
                val result = getChatsUseCase()
                result.fold(
                    onSuccess = { chats ->
                        dispatchChatAction(ChatAction.ChatsLoaded(chats))
                    },
                    onFailure = { error ->
                        dispatchChatAction(ChatAction.ChatLoadError(error.message ?: "Ошибка загрузки чатов"))
                    }
                )
            } catch (e: Exception) {
                dispatchChatAction(ChatAction.ChatLoadError(e.message ?: "Ошибка загрузки чатов"))
            }
        }
    }


    fun loadMessages(chatId: String) {
        viewModelScope.launch {
            dispatchMessageAction(MessageAction.LoadMessages)
            try {
                val result = getMessagesUseCase(chatId)
                result.fold(
                    onSuccess = { messages ->
                        dispatchMessageAction(MessageAction.MessagesLoaded(messages))
                    },
                    onFailure = { error ->
                        dispatchMessageAction(MessageAction.MessageLoadError(error.message ?: "Ошибка загрузки сообщений"))
                    }
                )
            } catch (e: Exception) {
                dispatchMessageAction(MessageAction.MessageLoadError(e.message ?: "Ошибка загрузки сообщений"))
            }
        }
    }


    fun sendMessage(chatId: String, text: String) {
        viewModelScope.launch {
            dispatchMessageAction(MessageAction.SendMessage)
            try {
                val result = sendMessageUseCase(chatId, text)
                result.fold(
                    onSuccess = { message ->
                        dispatchMessageAction(MessageAction.MessageSent(message))
                    },
                    onFailure = { error ->
                        dispatchMessageAction(MessageAction.SendMessageError(error.message ?: "Ошибка отправки"))
                    }
                )
            } catch (e: Exception) {
                dispatchMessageAction(MessageAction.SendMessageError(e.message ?: "Ошибка отправки"))
            }
        }
    }

    fun observeMessages(chatId: String) {
        viewModelScope.launch {
            observeMessagesUseCase(chatId).collect { messages ->
                dispatchMessageAction(MessageAction.MessagesLoaded(messages))
            }
        }
    }

    fun selectChat(chat: com.example.iwawka.domain.models.Chat) {
        dispatchChatAction(ChatAction.SelectChat(chat))
    }

    fun clearError() {
        dispatchProfileAction(ProfileAction.ClearError)
    }

    init {
        viewModelScope.launch {
            observeChatsUseCase().collect { chats ->
                dispatchChatAction(ChatAction.ChatsLoaded(chats))
            }
        }
    }
}