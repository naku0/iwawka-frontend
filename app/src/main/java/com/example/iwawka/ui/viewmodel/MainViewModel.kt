package com.example.iwawka.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iwawka.domain.models.Message
import com.example.iwawka.domain.usecases.chat.GetChatUseCase
import com.example.iwawka.domain.usecases.chat.GetChatsUseCase
import com.example.iwawka.domain.usecases.chat.ObserveChatsUseCase
import com.example.iwawka.domain.usecases.explanation.ExplainUseCase
import com.example.iwawka.domain.usecases.message.GetMessagesUseCase
import com.example.iwawka.domain.usecases.message.MarkAsReadUseCase
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
import com.example.iwawka.ui.states.explain.ExplanationState
import com.example.iwawka.ui.states.message.MessageAction
import com.example.iwawka.ui.states.message.MessageReducer
import com.example.iwawka.ui.states.message.MessageState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.update

class MainViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val getChatsUseCase: GetChatsUseCase,
    private val getChatUseCase: GetChatUseCase,
    private val observeChatsUseCase: ObserveChatsUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val observeMessagesUseCase: ObserveMessagesUseCase,
    private val markAsReadUseCase: MarkAsReadUseCase,
    private val explainUseCase: ExplainUseCase

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

    private val _explanationState = MutableStateFlow(ExplanationState(showDialog = false))
    val explanationState: StateFlow<ExplanationState> = _explanationState.asStateFlow()

    private var observeMessagesJob: Job? = null
    private var observingChatId: String? = null

    private val lastReadMessageByChat = mutableMapOf<String, Int>()
    private val inFlightRead = mutableSetOf<String>()

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
        dispatchMessageAction(MessageAction.Clear)
        observeMessagesJob?.cancel()
        observingChatId = chatId
        observeMessagesJob = viewModelScope.launch {
            observeMessagesUseCase(chatId).collect { messages ->
                if (observingChatId == chatId) {
                    dispatchMessageAction(MessageAction.MessagesLoaded(messages))
                }
            }
        }
    }

    fun stopObservingMessages(){
        observingChatId = null
        observeMessagesJob?.cancel()
        observeMessagesJob = null
        dispatchMessageAction(MessageAction.Clear)
    }

    fun selectChat(chat: com.example.iwawka.domain.models.Chat) {
        dispatchChatAction(ChatAction.SelectChat(chat))
    }

    fun clearError() {
        dispatchProfileAction(ProfileAction.ClearError)
    }

    fun markChatAsRead(chatId: String){
        _chatsState.update { st ->
            st.copy(
                chats = st.chats.map { c ->
                    if (c.id == chatId) c.copy(unreadCount =  0) else c
                },
                selectedChat = st.selectedChat?.let{ c ->
                    if(c.id == chatId) c.copy(unreadCount = 0) else c
                }
            )
        }
    }
    fun markMessageAsRead(chatId: String, messageId: String) {
        val mId = messageId.toIntOrNull() ?: return

        _messagesState.update { state ->
            state.copy(
                messages = state.messages.map { message ->
                    if(message.id == messageId){
                        message.copy(isRead = true)
                    } else {
                        message
                    }
                }
            )
        }

        val prev = lastReadMessageByChat[chatId]
        if (prev != null && mId <= prev) return

        viewModelScope.launch {
            markAsReadUseCase(mId).onSuccess {
                markChatAsRead(chatId)
            }.onFailure {
                _messagesState.update { state ->
                    state.copy(
                        messages = state.messages.map { message ->
                            if(message.id == messageId){
                                message.copy(isRead = false)
                            } else {
                                message
                            }
                        }
                    )
                }
            }
        }
    }

    fun deleteMessages(){}

    fun showExplanation(){
        _explanationState.update { it.copy(isDialogVisible = true) }
    }

    fun hideExplanationDialog() {
        _explanationState.update { it.copy(
            isDialogVisible = false,
            explanation = "",
            error = null
        ) }
    }


    fun explain(messages: List<Message>) {
        viewModelScope.launch {
            _explanationState.value = _explanationState.value.copy(
                isLoading = true,
                error = null
            )

            val result = explainUseCase.summarize(messages)

            result.fold(
                onSuccess = { explanation ->
                    _explanationState.value = _explanationState.value.copy(
                        explanation = explanation,
                        isLoading = false,
                        showDialog = true
                    )
                },
                onFailure = { error ->
                    _explanationState.value = _explanationState.value.copy(
                        error = error.message ?: "Ошибка",
                        isLoading = false,
                        showDialog = true
                    )
                }
            )
        }
    }



    init {
        viewModelScope.launch {
            observeChatsUseCase().collect { chats ->
                dispatchChatAction(ChatAction.ChatsLoaded(chats))
            }
        }
    }
}