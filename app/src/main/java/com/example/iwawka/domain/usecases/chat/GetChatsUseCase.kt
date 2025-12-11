package com.example.iwawka.domain.usecases.chat
import com.example.iwawka.domain.models.Chat
import com.example.iwawka.domain.repositories.interfaces.ChatRepository

class GetChatUseCase(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chatId: String): Chat? {
        return chatRepository.getChat(chatId)
    }
}
