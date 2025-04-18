package com.example.hotel.data.repository

import com.example.hotel.data.model.CreateUserRequest
import com.example.hotel.data.model.LoginRequest
import com.example.hotel.data.model.LoginResponse
import com.example.hotel.data.model.User
import com.example.hotel.data.network.ApiClient
import kotlinx.serialization.json.Json

/**
 * Repository for user-related operations
 */
class UserRepository {
    private val apiService = ApiClient.apiService

    /**
     * Creates a new user with the provided information
     *
     * @param email User's email
     * @param fullName User's full name
     * @param latitude User's latitude
     * @param longitude User's longitude
     * @param password User's password
     * @return Result containing User on success or Exception on failure
     */
    suspend fun createUser(
        email: String,
        fullName: String,
        latitude: String,
        longitude: String,
        password: String
    ): Result<User> {
        return try {
            // Create GraphQL mutation query
            val query = """
                mutation {
                  CreateNewUser(input: {
                    email: "$email",
                    full_name: "$fullName",
                    location: {
                      latitude: "$latitude",
                      longitude: "$longitude"
                    },
                    password: "$password"
                  }) {
                    _id
                    email
                    full_name
                    created_at
                  }
                }
            """.trimIndent()

            // Make API call
            val response = apiService.createUser(CreateUserRequest(query))

            if (response.isSuccessful) {
                val userData = response.body()?.data?.createNewUser
                if (userData != null) {
                    Result.success(userData)
                } else {
                    val errorMessage = response.body()?.errors?.firstOrNull()?.message ?: "Unknown error"
                    Result.failure(Exception(errorMessage))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Logs in a user with the provided credentials
     *
     * @param email User's email
     * @param password User's password
     * @return Result containing LoginResponse on success or Exception on failure
     */
    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            // Create GraphQL mutation query
            val query = """
                mutation {
                  Login(input: {
                    email: "$email",
                    password: "$password"
                  }) {
                    token
                  }
                }
            """.trimIndent()

            // Make API call
            val response = apiService.login(LoginRequest(query))

            if (response.isSuccessful) {
                val loginData = response.body()?.data?.login
                if (loginData != null) {
                    Result.success(loginData)
                } else {
                    val errorMessage = response.body()?.errors?.firstOrNull()?.message ?: "Unknown error"
                    Result.failure(Exception(errorMessage))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
