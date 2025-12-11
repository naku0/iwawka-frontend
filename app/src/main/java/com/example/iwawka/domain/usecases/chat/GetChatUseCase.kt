package com.example.iwawka.domain.usecases.chat

import com.example.iwawka.domain.models.Chat
import com.example.iwawka.domain.repositories.interfaces.ChatRepository

class GetChatsUseCase(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(): List<Chat> {
        return chatRepository.getChats()
    }
}
