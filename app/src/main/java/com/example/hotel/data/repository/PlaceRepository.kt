package com.example.hotel.data.repository

import android.util.Log
import com.example.hotel.data.exception.InvalidTokenException
import com.example.hotel.data.model.Place
import com.example.hotel.data.model.PlaceRequest
import com.example.hotel.data.network.ApiClient
import kotlinx.serialization.json.Json

/**
 * Repository for place-related operations
 */
class PlaceRepository {
    private val TAG = "PlaceRepository"
    private val apiService = ApiClient.apiService

    /**
     * Fetches recommended places from the API
     *
     * @return Result containing a list of Place objects on success or Exception on failure
     */
    suspend fun getRecommendedPlaces(): Result<List<Place>> {
        return try {
            // Get the token from AuthManager
            val token = com.example.hotel.data.auth.AuthManager.getInstance(com.example.hotel.HotelApplication.instance).getCurrentToken()
            Log.d(TAG, "Using token for query: ${token?.take(10)}${if (token?.length ?: 0 > 10) "..." else ""} (${token?.length ?: 0} chars)")

            // Create GraphQL query - no need for authorization directive as token is sent in header
            val query = """
                query {
                  RecommendedPlaces {
                    _id
                    leadingDestinationTitle
                    subDestinationTitle
                    description
                    price
                    iataCode
                    geoPoint {
                      latitude
                      longitude
                    }
                    imageUrl
                  }
                }
            """.trimIndent()

            // No need for variables or token in body as token is sent in header
            val variables = null

            // Make API call
            Log.d(TAG, "Fetching recommended places with query: $query")

            // Create request without token in body
            val placeRequest = PlaceRequest(
                query = query,
                variables = variables
            )

            Log.d(TAG, "Using token in Authorization header instead of request body")

            // Use the regular method - token is added by AuthInterceptor
            val response = apiService.getPlaces(placeRequest)

            Log.d(TAG, "Using token directly in API call")
            Log.d(TAG, "Trying with Bearer prefix in Authorization header")

            Log.d(TAG, "Response code: ${response.code()}, message: ${response.message()}")
            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d(TAG, "Response body: $responseBody")

                val places = responseBody?.data?.recommendedPlaces
                if (places != null) {
                    Log.d(TAG, "Successfully retrieved ${places.size} places")
                    Result.success(places)
                } else {
                    val errorMessage = responseBody?.errors?.firstOrNull()?.message ?: "No places found"
                    Log.e(TAG, "Error fetching places: $errorMessage")

                    // Check if it's an invalid token error
                    if (errorMessage == "invalid token") {
                        Result.failure(InvalidTokenException())
                    } else {
                        Result.failure(Exception(errorMessage))
                    }
                }
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Fetches places to explore from the API
     *
     * @param page Page number for pagination
     * @param limit Number of items per page
     * @param query Optional search query
     * @return Result containing a list of Place objects on success or Exception on failure
     */
    suspend fun getExplorePlaces(page: Int = 1, limit: Int = 10, query: String? = null): Result<List<Place>> {
        return try {
            // Create GraphQL query
            val queryString = if (query != null) {
                """
                query {
                  ExplorePlaces(input: {
                    page: $page,
                    limit: $limit,
                    query: "$query"
                  }) {
                    _id
                    leadingDestinationTitle
                    subDestinationTitle
                    description
                    price
                    iataCode
                    geoPoint {
                      latitude
                      longitude
                    }
                    imageUrl
                  }
                }
                """.trimIndent()
            } else {
                """
                query {
                  ExplorePlaces(input: {
                    page: $page,
                    limit: $limit
                  }) {
                    _id
                    leadingDestinationTitle
                    subDestinationTitle
                    description
                    price
                    iataCode
                    geoPoint {
                      latitude
                      longitude
                    }
                    imageUrl
                  }
                }
                """.trimIndent()
            }

            // Make API call
            val response = apiService.getPlaces(PlaceRequest(queryString))

            if (response.isSuccessful) {
                val places = response.body()?.data?.explorePlaces
                if (places != null) {
                    Result.success(places)
                } else {
                    val errorMessage = response.body()?.errors?.firstOrNull()?.message ?: "No places found"
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
