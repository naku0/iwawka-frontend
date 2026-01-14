package com.example.iwawka.domain.usecases.explanation

import com.example.iwawka.domain.models.Message
import com.example.iwawka.domain.repositories.interfaces.ExplainRepository
import com.example.iwawka.model.dto.AiRequest
import com.example.iwawka.model.dto.MessageItem

class ExplainUseCase(
    private val explainRepository: ExplainRepository
) {
    suspend fun summarize(messages: List<Message>): Result<String> {
        val request = AiRequest(
            messages = messages.map { message ->
                MessageItem(
                    author = if (message.isFromMe) "user" else "assistant",
                    message = message.text,
                )
            }
        )

        return explainRepository.summarize(request)
    }
}