package com.example.iwawka.model.API

import com.example.iwawka.model.auth.TokenStorage
import com.example.iwawka.model.dto.LoginRequest
import com.example.iwawka.model.dto.LoginResponse
import com.example.iwawka.model.dto.LogoutRequest
import com.example.iwawka.model.dto.LogoutResponse
import com.example.iwawka.model.dto.RefreshTokenRequest
import com.example.iwawka.model.dto.RegisterRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultrequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.json.Json

class IwawkaApi(
    private val baseUrl: String = DEFAULT_BASE_URL,
    private val tokenStorage: TokenStorage? = null,
    httpClient: HttpClient? = null
) {

    private val client: HttpClient = httpClient ?: HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        
        install(HttpTimeout) {
            requestTimeoutMillis = 30000
        }
        
        defaultRequest {
            // Add Authorization header dynamically for protected endpoints
            if (tokenStorage != null && !url.encodedPath.startsWith("/api/auth/")) {
                tokenStorage.getAccessToken()?.let { token ->
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            }
        }
    }

    private suspend fun HttpRequestBuilder.addAuthHeader() {
        if (tokenStorage != null && !url.encodedPath.startsWith("/api/auth/")) {
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
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

    suspend fun refreshToken(refreshToken: String): ApiResponse<LoginResponse> {
        val request = RefreshTokenRequest(refreshToken)
        return client.post("$baseUrl/api/auth/refresh") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun logout(refreshToken: String): ApiResponse<LogoutResponse> {
        val request = LogoutRequest(refreshToken)
        return client.post("$baseUrl/api/auth/logout") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    // Protected endpoints with automatic token refresh on 401
    suspend fun sendMessage(request: SendMessageRequest): ApiResponse<EmptyData> =
        executeWithAuthRetry {
            client.post("$baseUrl/api/message") {
                contentType(ContentType.Application.Json)
                addAuthHeader()
                setBody(request)
            }.body()
        }

    suspend fun getMessages(chatId: Int): ApiResponse<List<MessageDto>> =
        executeWithAuthRetry {
            client.get("$baseUrl/api/message/$chatId") {
                addAuthHeader()
            }.body()
        }

    suspend fun deleteMessage(messageId: Int): ApiResponse<EmptyData> =
        executeWithAuthRetry {
            client.delete("$baseUrl/api/message/$messageId") {
                addAuthHeader()
            }.body()
        }

    suspend fun createChat(request: CreateChatRequest): ApiResponse<CreateChatResponse> =
        executeWithAuthRetry {
            client.post("$baseUrl/api/chat") {
                contentType(ContentType.Application.Json)
                addAuthHeader()
                setBody(request)
            }.body()
        }

    suspend fun updateUser(
        userId: Int,
        request: UpdateUserRequest
    ): ApiResponse<EmptyData> =
        executeWithAuthRetry {
            client.post("$baseUrl/api/user/$userId") {
                contentType(ContentType.Application.Json)
                addAuthHeader()
                setBody(request)
            }.body()
        }

    private suspend fun <T> executeWithAuthRetry(block: suspend () -> T): T {
        val response = try {
            block()
        } catch (e: Exception) {
            // Check if it's an HTTP error with 401 status
            val httpResponse = (e as? io.ktor.client.call.HttpClientCallException)?.response
            if (httpResponse?.status == HttpStatusCode.Unauthorized) {
                // Try to refresh token and retry once
                if (refreshAccessTokenIfNeeded()) {
                    return block() // Retry with new token
                } else {
                    throw e // Re-throw if refresh failed
                }
            } else {
                throw e
            }
        }
        return response
    }

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

    companion object {
        private const val DEFAULT_BASE_URL = "https://api.example.com"
    }
}

