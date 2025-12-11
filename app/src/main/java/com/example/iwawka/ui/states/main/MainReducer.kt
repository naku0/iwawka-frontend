package com.example.iwawka.ui.states.main

class MainReducer {
    fun reduce(state: MainState, action: MainAction): MainState = when (action) {
        is MainAction.MessageSent -> state.withMessageSent(action.message)
        is MainAction.ReplyToMessage -> state.withReplyToMessage(action.messageId)
        is MainAction.TypingStarted -> state.withTypingStarted()
        is MainAction.CancelActions -> state.cancelAllActions()
        is MainAction.UpdateInputText -> state.withInputText(action.text)
        is MainAction.MessagesLoaded -> state.withMessagesLoaded(action.messages)
        is MainAction.LoadMessagesError -> state.withLoadingError(action.error)
        is MainAction.LoadMessages -> state.loadMessages()
    }
}