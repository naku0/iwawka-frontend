package com.example.iwawka.ui.states.message

class MessageReducer {
    fun reduce(currentState: MessageState, action: MessageAction): MessageState {
        return when (action) {
            is MessageAction.LoadMessages -> {
                currentState.copy(
                    isLoading = true,
                    error = null
                )
            }
            is MessageAction.MessagesLoaded -> {
                currentState.copy(
                    messages = action.messages,
                    isLoading = false,
                    error = null
                )
            }
            is MessageAction.MessageLoadError -> {
                currentState.copy(
                    isLoading = false,
                    error = action.error
                )
            }
            is MessageAction.SendMessage -> {
                currentState.copy(
                    isSending = true
                )
            }
            is MessageAction.MessageSent -> {
                currentState.copy(
                    messages = currentState.messages + action.message,
                    isSending = false
                )
            }
            is MessageAction.SendMessageError -> {
                currentState.copy(
                    isSending = false,
                    error = action.error
                )
            }
            is MessageAction.ClearError -> {
                currentState.copy(
                    error = null
                )
            }
        }
    }
}