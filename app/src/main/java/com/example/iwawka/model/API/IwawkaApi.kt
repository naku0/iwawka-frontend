package com.example.iwawka.model.API

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json

class IwawkaApi(
    private val baseUrl: String = DEFAULT_BASE_URL,
    httpClient: HttpClient? = null
) {

    private val client: HttpClient = httpClient ?: HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun sendMessage(request: SendMessageRequest): ApiResponse<EmptyData> =
        client.post("$baseUrl/api/message") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

    suspend fun getMessages(chatId: Int): ApiResponse<List<MessageDto>> =
        client.get("$baseUrl/api/message/$chatId")
            .body()

    suspend fun deleteMessage(messageId: Int): ApiResponse<EmptyData> =
        client.delete("$baseUrl/api/message/$messageId")
            .body()

    suspend fun createChat(request: CreateChatRequest): ApiResponse<CreateChatResponse> =
        client.post("$baseUrl/api/chat") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

    suspend fun updateUser(
        userId: Int,
        request: UpdateUserRequest
    ): ApiResponse<EmptyData> =
        client.post("$baseUrl/api/user/$userId") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

    companion object {
        private const val DEFAULT_BASE_URL = "https://api.example.com"
    }
}

