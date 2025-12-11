package com.example.iwawka.domain.usecases.message

import com.example.iwawka.domain.models.Message
import com.example.iwawka.domain.repositories.interfaces.MessageRepository
import kotlinx.coroutines.flow.Flow

class ObserveMessagesUseCase(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(chatId: String): Flow<List<Message>> {
        return messageRepository.observeMessages(chatId)
    }
}
