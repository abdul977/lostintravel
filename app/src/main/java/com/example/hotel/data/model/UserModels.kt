package com.example.hotel.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request model for creating a new user
 */
@Serializable
data class CreateUserRequest(
    @SerialName("query")
    val query: String
)

/**
 * Response model for user creation
 */
@Serializable
data class CreateUserResponse(
    @SerialName("data")
    val data: UserData? = null,
    @SerialName("errors")
    val errors: List<GraphQLError>? = null
)

/**
 * User data model
 */
@Serializable
data class UserData(
    @SerialName("CreateNewUser")
    val createNewUser: User? = null,
    @SerialName("Login")
    val login: LoginResponse? = null
)

/**
 * User model
 */
@Serializable
data class User(
    @SerialName("_id")
    val id: String,
    @SerialName("email")
    val email: String,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("created_at")
    val createdAt: String
)

/**
 * Login request model
 */
@Serializable
data class LoginRequest(
    @SerialName("query")
    val query: String
)

/**
 * Login response model
 */
@Serializable
data class LoginResponse(
    @SerialName("token")
    val token: String
)

/**
 * GraphQL error model
 */
@Serializable
data class GraphQLError(
    @SerialName("message")
    val message: String,
    @SerialName("locations")
    val locations: List<ErrorLocation>? = null,
    @SerialName("path")
    val path: List<String>? = null
)

/**
 * Error location model
 */
@Serializable
data class ErrorLocation(
    @SerialName("line")
    val line: Int,
    @SerialName("column")
    val column: Int
)

/**
 * Location model for user
 */
@Serializable
data class Location(
    @SerialName("latitude")
    val latitude: String,
    @SerialName("longitude")
    val longitude: String
)
