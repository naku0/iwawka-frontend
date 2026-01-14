package com.example.iwawka.model.api

import com.example.iwawka.model.auth.TokenStorage
import com.example.iwawka.model.dto.AiRequest
import com.example.iwawka.model.dto.AiResponse
import com.example.iwawka.model.dto.LoginRequest
import com.example.iwawka.model.dto.LoginResponse
import com.example.iwawka.model.dto.LogoutRequest
import com.example.iwawka.model.dto.LogoutResponse
import com.example.iwawka.model.dto.RefreshTokenRequest
import com.example.iwawka.model.dto.RegisterRequest
import com.example.iwawka.model.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class IwawkaApi(
    private val tokenStorage: TokenStorage?,
    private val baseUrl: String
) {
    private val client = HttpClient(CIO){
            install(HttpTimeout) {
                requestTimeoutMillis = 30000
            }
            install(ContentNegotiation){
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        encodeDefaults = true
                    }
                )
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
                if (tokenStorage != null) {
                    tokenStorage.getAccessToken()?.let { token ->
                        header(HttpHeaders.Authorization, "Bearer $token")
                    }
                }
            }
    }

    private suspend fun HttpRequestBuilder.addAuthHeader() {
        if (tokenStorage != null) {
            tokenStorage.getAccessToken()?.let { token ->
                header(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }

    // Auth endpoints
    suspend fun register(request: RegisterRequest): ApiResponse<LoginResponse> =
        client.post("$baseUrl/api/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

    suspend fun login(request: LoginRequest): ApiResponse<LoginResponse> =
        client.post("$baseUrl/api/auth/login") {
            setBody(request)
        }.body()

    suspend fun refreshToken(refreshToken: String): ApiResponse<LoginResponse> {
        val request = RefreshTokenRequest(refreshToken)
        return client.post("$baseUrl/api/auth/refresh") {
            setBody(request)
        }.body()
    }

    suspend fun logout(refreshToken: String): ApiResponse<LogoutResponse> {
        val request = LogoutRequest(refreshToken)
        return client.post("$baseUrl/api/auth/logout") {
            setBody(request)
        }.body()
    }

    // Protected endpoints

    suspend fun getMe(): ApiResponse<UserDto> =
        client.get("$baseUrl/api/user/me") {
            addAuthHeader()
        }.body()
    suspend fun sendMessage(request: SendMessageRequest): ApiResponse<EmptyData> =
        client.post("$baseUrl/api/message") {
            setBody(request)
        }.body()

    suspend fun getMessages(chatId: Int): ApiResponse<List<MessageDto>> =
        client.get("$baseUrl/api/message/$chatId") {
            addAuthHeader()
        }.body()

    suspend fun deleteMessage(messageId: Int): ApiResponse<EmptyData> =
        client.delete("$baseUrl/api/message/$messageId") {
            addAuthHeader()
        }.body()

    suspend fun getChats(): ApiResponse<List<ChatDto>> =
        client.get("$baseUrl/api/chat"){
            addAuthHeader()
        }.body()

    suspend fun markAsRead(messageId: Int): ApiResponse<EmptyData> =
        client.post("$baseUrl/api/message/$messageId/read") {
            addAuthHeader()
        }.body()

    suspend fun createChat(request: CreateChatRequest): ApiResponse<CreateChatResponse> =
        client.post("$baseUrl/api/chat") {
            setBody(request)
        }.body()

    suspend fun summarize(req: AiRequest): ApiResponse<AiResponse> =
        client.post("$baseUrl/ai/summarize"){
            setBody(req)
        }.body()
    suspend fun updateUser(
        userId: Int,
        request: UpdateUserRequest
    ): ApiResponse<EmptyData> =
        client.post("$baseUrl/api/user/$userId") {
            setBody(request)
        }.body()

    // Token management
    suspend fun refreshAccessTokenIfNeeded(): Boolean {
        val storedRefreshToken = tokenStorage?.getRefreshToken() ?: return false
        return try {
            val response = refreshToken(storedRefreshToken)
            if (response.success) {
                tokenStorage?.saveTokens(
                    response.data.accessToken,
                    response.data.refreshToken,
                    response.data.expiresIn
                )
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}
