package com.example.iwawka.ui.states.chat

class ChatReducer {
    fun reduce(currentState: ChatsState, action: ChatAction): ChatsState {
        return when (action) {
            is ChatAction.LoadChats -> {
                currentState.copy(
                    isLoading = true,
                    error = null
                )
            }
            is ChatAction.ChatsLoaded -> {
                currentState.copy(
                    chats = action.chats,
                    isLoading = false,
                    error = null
                )
            }
            is ChatAction.ChatLoadError -> {
                currentState.copy(
                    isLoading = false,
                    error = action.error
                )
            }
            is ChatAction.SelectChat -> {
                currentState.copy(
                    selectedChat = action.chat
                )
            }
            is ChatAction.ClearError -> {
                currentState.copy(
                    error = null
                )
            }
        }
    }
}