package com.example.iwawka.domain.usecases.message

import com.example.iwawka.domain.models.Message
import com.example.iwawka.domain.repositories.interfaces.MessageRepository

class GetMessagesUseCase(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(chatId: String): List<Message> {
        return messageRepository.getMessages(chatId)
    }
}