package com.example.hotel.data.network

import android.util.Log
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor to add authentication token to requests
 */
class AuthInterceptor(private val tokenProvider: suspend () -> String?) : Interceptor {

    companion object {
        private const val TAG = "AuthInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Get token synchronously (not ideal but necessary for OkHttp interceptor)
        val token = runBlocking { tokenProvider() }

        Log.d(TAG, "Token retrieved: ${token?.take(10)}${if (token?.length ?: 0 > 10) "..." else ""} (${token?.length ?: 0} chars)")

        // If token is null or empty, proceed with the original request
        if (token.isNullOrEmpty()) {
            Log.w(TAG, "No token available, proceeding without authentication")
            return chain.proceed(originalRequest)
        }

        // Add token to request
        // Try different authorization header formats
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .header("x-access-token", token) // Some APIs use this format
            .build()

        Log.d(TAG, "Request headers: ${newRequest.headers}")

        Log.d(TAG, "Added token to request: ${originalRequest.url}")
        return chain.proceed(newRequest)
    }
}
