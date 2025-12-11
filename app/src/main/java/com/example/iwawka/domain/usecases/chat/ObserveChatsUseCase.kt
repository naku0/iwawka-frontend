package com.example.iwawka.domain.usecases.chat
import com.example.iwawka.domain.models.Chat
import com.example.iwawka.domain.repositories.interfaces.ChatRepository
import kotlinx.coroutines.flow.Flow

class ObserveChatsUseCase(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(): Flow<List<Chat>> {
        return chatRepository.observeChats()
    }
}
