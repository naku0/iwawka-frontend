package com.example.iwawka.ui.states.main

import com.example.iwawka.domain.models.Message
import com.example.iwawka.ui.states.message.MessageState

data class MainState(
    val messageState: MessageState = MessageState(),
    val inputText: String = "",
    val replyingTo: String? = null,
    val isTyping: Boolean = false
) {
    fun withMessagesLoaded(messages: List<Message>) = copy(
        messageState = messageState.copy(
            messages = messages,
            isLoading = false,
            error = null
        )
    )

    fun withLoadingError(error: String) = copy(
        messageState = messageState.copy(
            isLoading = false,
            error = error
        )
    )

    fun loadMessages() = copy(
        messageState = messageState.copy(
            isLoading = true,
            error = null
        )
    )

    fun withMessageSent(message: Message) = copy(
        messageState = messageState.copy(
            messages = messageState.messages + message,
            isSending = false
        ),
        inputText = ""
    )

    // Добавляем недостающие методы
    fun withReplyToMessage(messageId: String) = copy(
        replyingTo = messageId
    )

    fun withTypingStarted() = copy(
        isTyping = true
    )

    fun cancelAllActions() = copy(
        replyingTo = null,
        isTyping = false
    )

    fun withInputText(text: String) = copy(
        inputText = text
    )
}