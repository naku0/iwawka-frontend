package com.example.iwawka.ui.states.message

import com.example.iwawka.domain.models.Message

sealed class MessageAction {
    object LoadMessages : MessageAction()
    data class MessagesLoaded(val messages: List<Message>) : MessageAction()
    data class MessageLoadError(val error: String) : MessageAction()
    object SendMessage : MessageAction()
    data class MessageSent(val message: Message) : MessageAction()
    data class SendMessageError(val error: String) : MessageAction()
    object ClearError : MessageAction()
    object Clear : MessageAction()
}
