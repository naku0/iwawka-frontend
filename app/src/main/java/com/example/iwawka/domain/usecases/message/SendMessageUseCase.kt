package com.example.iwawka.domain.usecases.message

import com.example.iwawka.domain.models.Message
import com.example.iwawka.domain.repositories.interfaces.MessageRepository

class SendMessageUseCase(
    private val messageRepository: MessageRepository
) {

    suspend operator fun invoke(chatId: String, text: String): Result<Message> {
        return messageRepository.sendMessage(chatId, text)
    }
}
