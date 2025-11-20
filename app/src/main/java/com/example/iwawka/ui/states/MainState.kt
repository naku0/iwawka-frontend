package com.example.iwawka.ui.states

data class MainState(
    val messages: MessageState = MessageState(),
    val input: InputState = InputState(),
    val ui: UiState = UiState(),
    val network: NetworkState = NetworkState(),
    val media: MediaState = MediaState(),
    val attachments: AttachmentState = AttachmentState()
)