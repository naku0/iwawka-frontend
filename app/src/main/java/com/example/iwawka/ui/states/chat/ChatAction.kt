package com.example.iwawka.ui.states.chat

import com.example.iwawka.domain.models.Chat

sealed class ChatAction {
    object LoadChats : ChatAction()
    data class ChatsLoaded(val chats: List<Chat>) : ChatAction()
    data class ChatLoadError(val error: String) : ChatAction()
    data class SelectChat(val chat: Chat?) : ChatAction()
    object ClearError : ChatAction()
}