package com.example.hotel.data.network

import android.content.Context
import com.example.hotel.data.auth.AuthManager
import com.example.hotel.data.model.CreateUserRequest
import com.example.hotel.data.model.CreateUserResponse
import com.example.hotel.data.model.LoginRequest
import com.example.hotel.data.model.PlaceRequest
import com.example.hotel.data.model.PlaceResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * API interface for GraphQL requests
 */
interface ApiService {
    @POST("graphql")
    suspend fun createUser(@Body request: CreateUserRequest): Response<CreateUserResponse>

    @POST("graphql")
    suspend fun login(@Body request: LoginRequest): Response<CreateUserResponse>

    @POST("graphql")
    suspend fun getPlaces(@Body request: PlaceRequest): Response<PlaceResponse>

    @POST("graphql")
    suspend fun getPlacesWithToken(
        @retrofit2.http.Header("Authorization") token: String,
        @Body request: PlaceRequest
    ): Response<PlaceResponse>

    @POST("graphql")
    suspend fun getPlacesWithCustomToken(
        @retrofit2.http.Header("x-access-token") token: String,
        @Body request: PlaceRequest
    ): Response<PlaceResponse>
}

/**
 * Singleton object to provide API service instance
 */
object ApiClient {
    private const val BASE_URL = "https://lostapi.frontendlabs.co.uk/"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private var authManager: AuthManager? = null

    /**
     * Initializes the ApiClient with a context for authentication
     *
     * @param context Application context
     */
    fun initialize(context: Context) {
        authManager = AuthManager.getInstance(context)
    }

    /**
     * Creates an OkHttpClient with authentication
     *
     * @return OkHttpClient instance
     */
    private fun createOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)

        // Add auth interceptor if auth manager is available
        authManager?.let { auth ->
            val authInterceptor = AuthInterceptor {
                runBlocking { auth.getTokenFlow().first() }
            }
            builder.addInterceptor(authInterceptor)
        }

        return builder.build()
    }

    // Cached Retrofit instance
    private var retrofit: Retrofit? = null

    /**
     * Gets the API service
     *
     * @return ApiService instance
     */
    val apiService: ApiService by lazy {
        createRetrofit().create(ApiService::class.java)
    }

    /**
     * Recreates the API service with the latest token
     * Call this after login to ensure the token is used
     */
    fun refreshApiService() {
        // This will force a new OkHttpClient to be created with the latest token
        // next time apiService is accessed
        retrofit = null
    }

    /**
     * Creates a Retrofit instance or returns the cached one
     */
    private fun createRetrofit(): Retrofit {
        return retrofit ?: Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build().also { retrofit = it }
    }
}
