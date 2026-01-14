package com.example.iwawka.domain.usecases.message

import com.example.iwawka.domain.repositories.interfaces.MessageRepository

class MarkAsReadUseCase (
    private val repo: MessageRepository
){
    suspend operator fun invoke(mId: Int): Result<Unit>{
        return repo.markAsRead(mId.toString())
    }
}