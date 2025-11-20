package com.example.iwawka.ui.extensions

import com.example.iwawka.domain.models.Message
import com.example.iwawka.ui.states.UiState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update


fun MutableStateFlow<UiState>.updateInputText(text: String) =
    update { it.copy(inputText = text) }

fun MutableStateFlow<UiState>.setLoading(loading: Boolean) =
    update { it.copy(isLoading = loading) }

fun MutableStateFlow<UiState>.setError(error: String?) =
    update { it.copy(error = error) }

fun MutableStateFlow<UiState>.clearError() =
    update { it.copy(error = null) }

fun MutableStateFlow<UiState>.clearReply() =
    update { it.copy(replyingToMessage = null) }

fun MutableStateFlow<UiState>.addMessage(message: Message) =
    update { current ->
        current.copy(messages = (current.messages + message).toImmutableList())
    }

fun MutableStateFlow<UiState>.setReplyingTo(message: Message) =
    update { it.copy(replyingToMessage = message) }

fun MutableStateFlow<UiState>.clearForNewMessage() =
    update { it.copy(inputText = "", replyingToMessage = null) }

fun MutableStateFlow<UiState>.startLoading() =
    update { it.copy(isLoading = true, error = null) }

fun MutableStateFlow<UiState>.stopLoading() =
    update { it.copy(isLoading = false) }

