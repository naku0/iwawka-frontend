package com.example.iwawka.model.auth

import android.content.Context
import android.content.SharedPreferences

class TokenStorage(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "iwawka_auth_prefs",
        Context.MODE_PRIVATE
    )

    private val KEY_ACCESS_TOKEN = "access_token"
    private val KEY_REFRESH_TOKEN = "refresh_token"
    private val KEY_EXPIRES_IN = "expires_in"
    private val KEY_TOKEN_TIMESTAMP = "token_timestamp"

    fun saveTokens(accessToken: String, refreshToken: String, expiresIn: Int) {
        prefs.edit().apply {
            putString(KEY_ACCESS_TOKEN, accessToken)
            putString(KEY_REFRESH_TOKEN, refreshToken)
            putInt(KEY_EXPIRES_IN, expiresIn)
            putLong(KEY_TOKEN_TIMESTAMP, System.currentTimeMillis())
            apply()
        }
    }

    fun getAccessToken(): String? = prefs.getString(KEY_ACCESS_TOKEN, null)

    fun getRefreshToken(): String? = prefs.getString(KEY_REFRESH_TOKEN, null)

    fun isTokenExpired(): Boolean {
        val timestamp = prefs.getLong(KEY_TOKEN_TIMESTAMP, 0)
        val expiresIn = prefs.getInt(KEY_EXPIRES_IN, 0)
        if (timestamp == 0L || expiresIn == 0) return true
        
        val expirationTime = timestamp + (expiresIn * 1000L)
        return System.currentTimeMillis() >= expirationTime
    }

    fun clearTokens() {
        prefs.edit().clear().apply()
    }

    fun hasTokens(): Boolean {
        return getAccessToken() != null && getRefreshToken() != null
    }

    fun getUserId(): String? {
        val token = getAccessToken() ?: return null
        return JwtDecoder.extractUserId(token)
    }

    fun getUserIdOrThrow(): String {
        return getUserId() ?: throw IllegalStateException("User ID not found in token")
    }
}

