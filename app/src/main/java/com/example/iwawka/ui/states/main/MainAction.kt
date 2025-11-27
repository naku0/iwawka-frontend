package com.example.iwawka.ui.states.main

import com.example.iwawka.domain.models.Message

sealed class MainAction {
    data class MessageSent(val message: Message) : MainAction()
    data class ReplyToMessage(val messageId: String) : MainAction()
    data object TypingStarted : MainAction()
    data object CancelActions : MainAction()
    data class UpdateInputText(val text: String) : MainAction()
    data object LoadMessages : MainAction()
    data class MessagesLoaded(val messages: List<Message>) : MainAction()
    data class LoadMessagesError(val error: String) : MainAction()
}