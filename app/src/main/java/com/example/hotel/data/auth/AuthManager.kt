package com.example.hotel.data.auth

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.hotel.util.JwtUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

/**
 * Manager for authentication-related operations
 */
class AuthManager(private val context: Context) {

    companion object {
        private const val TAG = "AuthManager"
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")
        private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")

        // Singleton instance
        @Volatile
        private var INSTANCE: AuthManager? = null

        fun getInstance(context: Context): AuthManager {
            return INSTANCE ?: synchronized(this) {
                val instance = AuthManager(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }

    /**
     * Saves the authentication token
     *
     * @param token The authentication token to save
     */
    suspend fun saveToken(token: String) {
        Log.d(TAG, "Saving token to DataStore: ${token.take(10)}${if (token.length > 10) "..." else ""} (${token.length} chars)")
        context.dataStore.edit { preferences ->
            preferences[AUTH_TOKEN_KEY] = token
        }
    }

    /**
     * Gets the authentication token as a Flow
     *
     * @return Flow of the authentication token
     */
    fun getTokenFlow(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[AUTH_TOKEN_KEY]
        }.onEach { token ->
            Log.d(TAG, "Retrieved token from DataStore: ${token?.take(10)}${if (token?.length ?: 0 > 10) "..." else ""} (${token?.length ?: 0} chars)")
        }
    }

    /**
     * Clears the authentication token
     */
    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN_KEY)
        }
    }

    /**
     * Checks if the user is authenticated
     *
     * @return Flow of boolean indicating if the user is authenticated
     */
    fun isAuthenticated(): Flow<Boolean> {
        return getTokenFlow().map { token ->
            !token.isNullOrEmpty()
        }
    }

    /**
     * Gets the current token synchronously (for debugging)
     *
     * @return The current token or null if not available
     */
    suspend fun getCurrentToken(): String? {
        return context.dataStore.data.map { preferences ->
            preferences[AUTH_TOKEN_KEY]
        }.first()
    }

    /**
     * Validates if the token is in a valid JWT format and not expired
     *
     * @return True if the token appears to be valid, false otherwise
     */
    suspend fun isTokenValid(): Boolean {
        val token = getCurrentToken() ?: return false
        Log.d(TAG, "Validating token: ${token.take(10)}${if (token.length > 10) "..." else ""} (${token.length} chars)")
        return JwtUtils.isTokenValid(token)
    }

    /**
     * Attempts to refresh the authentication token
     * In a real app, this would call a refresh token endpoint
     *
     * @return True if the token was refreshed successfully, false otherwise
     */
    suspend fun refreshToken(): Boolean {
        // In a real app, you would have a refresh token stored and use it to get a new access token
        // For this example, we'll just return false to indicate that the token couldn't be refreshed
        Log.d(TAG, "Token refresh attempted but not implemented")
        return false
    }
}
