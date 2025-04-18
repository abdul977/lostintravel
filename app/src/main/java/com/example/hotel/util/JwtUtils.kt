package com.example.hotel.util

import android.util.Base64
import android.util.Log
import org.json.JSONObject
import java.nio.charset.StandardCharsets

/**
 * Utility class for JWT token operations
 */
object JwtUtils {
    private const val TAG = "JwtUtils"

    /**
     * Checks if a JWT token is valid
     *
     * @param token The JWT token to validate
     * @return True if the token is valid, false otherwise
     */
    fun isTokenValid(token: String?): Boolean {
        if (token.isNullOrEmpty()) {
            return false
        }

        try {
            // Split the token into its parts
            val parts = token.split(".")
            if (parts.size != 3) {
                Log.w(TAG, "Token format is invalid: doesn't have 3 parts")
                return false
            }

            // Decode the payload
            val payload = decodeBase64(parts[1])
            val jsonObject = JSONObject(payload)

            // Check if the token has an expiration claim
            if (jsonObject.has("exp")) {
                val exp = jsonObject.getLong("exp")
                val currentTime = System.currentTimeMillis() / 1000
                
                // Check if the token is expired
                if (exp < currentTime) {
                    Log.w(TAG, "Token is expired")
                    return false
                }
            }

            return true
        } catch (e: Exception) {
            Log.e(TAG, "Error validating token", e)
            return false
        }
    }

    /**
     * Decodes a Base64 string
     *
     * @param base64String The Base64 string to decode
     * @return The decoded string
     */
    private fun decodeBase64(base64String: String): String {
        // JWT uses URL-safe Base64, so we need to add padding if necessary
        val paddedBase64 = when (base64String.length % 4) {
            0 -> base64String
            2 -> "$base64String=="
            3 -> "$base64String="
            else -> base64String
        }

        val decodedBytes = Base64.decode(paddedBase64, Base64.URL_SAFE)
        return String(decodedBytes, StandardCharsets.UTF_8)
    }
}
