package com.example.iwawka.domain.repositories.impl

import com.example.iwawka.domain.repositories.interfaces.ExplainRepository
import com.example.iwawka.model.api.IwawkaApi
import com.example.iwawka.model.dto.AiRequest

class ExplainRepositoryImpl(
    private val apiService: IwawkaApi
) : ExplainRepository {
    override suspend fun summarize(req: AiRequest): Result<String> {
        return try {
            val apiResponse = apiService.summarize(req)

            if (apiResponse.success) {
                Result.success(apiResponse.data.text)
            } else {
                val errorMessage = apiResponse.message ?: "Ошибка от сервера"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}