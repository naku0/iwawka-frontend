package com.example.iwawka.ui.states

import Attachment

data class InputState(
    val text: String = "",
    val replyToMessageId: String? = null,
    val attachment: Attachment? = null
) {
    fun updateText(newText: String): InputState =
        copy(text = newText)

    fun setReplyTo(messageId: String): InputState =
        copy(replyToMessageId = messageId)

    fun attach(attachment: Attachment): InputState =
        copy(attachment = attachment)

    fun removeAttachment(): InputState =
        copy(attachment = null)

    fun clearForNewMessage(): InputState =
        copy(text = "", attachment = null)

    fun clearAll(): InputState = InputState()
}