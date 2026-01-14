package com.example.iwawka.model.auth

import android.util.Base64
import org.json.JSONObject

object JwtDecoder {

    fun extractUserId(token: String): String? {
        return try {
            if (token.isEmpty() || !token.contains(".")) {
                return null
            }

            val parts = token.split(".")
            if (parts.size < 2) return null

            val payloadJson = decodeJwtPayload(parts[1])
            val jsonObject = JSONObject(payloadJson)

            jsonObject.optString("user_id").takeIf { it.isNotEmpty() }
                ?: jsonObject.optString("userId").takeIf { it.isNotEmpty() }
                ?: jsonObject.optString("sub").takeIf { it.isNotEmpty() }
                ?: jsonObject.optString("id").takeIf { it.isNotEmpty() }
                ?: jsonObject.optString("uid").takeIf { it.isNotEmpty() }
        } catch (e: Exception) {
            null
        }
    }

    fun extractClaim(token: String, claimName: String): String? {
        return try {
            if (token.isEmpty() || !token.contains(".")) return null

            val parts = token.split(".")
            if (parts.size < 2) return null

            val payloadJson = decodeJwtPayload(parts[1])
            val jsonObject = JSONObject(payloadJson)
            jsonObject.optString(claimName).takeIf { it.isNotEmpty() }
        } catch (e: Exception) {
            null
        }
    }

    private fun decodeJwtPayload(encodedPayload: String): String {
        val paddedPayload = when (encodedPayload.length % 4) {
            2 -> "$encodedPayload=="
            3 -> "$encodedPayload="
            else -> encodedPayload
        }

        return String(
            Base64.decode(paddedPayload, Base64.URL_SAFE),
            Charsets.UTF_8
        )
    }
}