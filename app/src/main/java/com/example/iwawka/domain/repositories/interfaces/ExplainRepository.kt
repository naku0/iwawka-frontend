package com.example.iwawka.domain.repositories.interfaces

import com.example.iwawka.model.dto.AiRequest

interface ExplainRepository {
    suspend fun summarize(req: AiRequest): Result<String>
}
