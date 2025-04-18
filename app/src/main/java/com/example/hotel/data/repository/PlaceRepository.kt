package com.example.hotel.data.repository

import android.util.Log
import com.example.hotel.data.exception.InvalidTokenException
import com.example.hotel.data.model.Place
import com.example.hotel.data.model.PlaceDetail
import com.example.hotel.data.model.PlaceRequest
import com.example.hotel.data.model.WeatherInfo
import com.example.hotel.data.model.WeatherType
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

    /**
     * Gets detailed information about a specific place
     *
     * @param placeId The ID of the place to get details for
     * @return Result containing PlaceDetail on success or Exception on failure
     */
    suspend fun getPlaceDetails(placeId: String): Result<PlaceDetail> {
        // In a real app, this would make an API call to get the details
        // For now, we'll create mock data based on the place ID
        return try {
            // Simulate network delay
            kotlinx.coroutines.delay(500)

            // Create mock weather data
            val weatherData = listOf(
                WeatherInfo("Now", 24, WeatherType.SUNNY, true),
                WeatherInfo("1:00PM", 25, WeatherType.SUNNY, false),
                WeatherInfo("2:00PM", 23, WeatherType.PARTLY_CLOUDY, false),
                WeatherInfo("3:00PM", 20, WeatherType.RAINY, false)
            )

            // Create mock gallery images
            val galleryImages = listOf(
                "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?q=80&w=1000&auto=format&fit=crop",
                "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?q=80&w=1000&auto=format&fit=crop",
                "https://images.unsplash.com/photo-1486870591958-9b9d0d1dda99?q=80&w=1000&auto=format&fit=crop",
                "https://images.unsplash.com/photo-1540390769625-2fc3f8b1d50c?q=80&w=1000&auto=format&fit=crop"
            )

            // Create mock place detail based on place ID
            val placeDetail = when {
                placeId.contains("sumbing") -> {
                    PlaceDetail(
                        id = placeId,
                        name = "Sumbing Mount",
                        location = "Chora, Greece",
                        price = 1350f,
                        description = "Mount Sumbing or Gunung Sumbing is an active, stratovolcano in Central Java, Indonesia that is symmetrical like its neighbour, Mount Sindoro. The mountain is a popular hiking destination with breathtaking views from the summit.",
                        imageUrl = "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?q=80&w=2070&auto=format&fit=crop",
                        galleryImages = galleryImages,
                        frequentlyVisited = true,
                        peopleExplored = "100k+",
                        weather = weatherData
                    )
                }
                placeId.contains("maldives") -> {
                    PlaceDetail(
                        id = placeId,
                        name = "Maldives",
                        location = "Maldives, Rep of Maldives",
                        price = 1350f,
                        description = "The Maldives is a tropical paradise known for its pristine white beaches, blue lagoons, and extensive reefs. The archipelago consists of 26 atolls with over 1,000 coral islands, offering some of the world's most luxurious overwater bungalows and underwater experiences.",
                        imageUrl = "https://images.unsplash.com/photo-1514282401047-d79a71a590e8?q=80&w=1965&auto=format&fit=crop",
                        galleryImages = listOf(
                            "https://images.unsplash.com/photo-1573843981267-be1999ff37cd?q=80&w=1000&auto=format&fit=crop",
                            "https://images.unsplash.com/photo-1540202404-a2f29016b523?q=80&w=1000&auto=format&fit=crop",
                            "https://images.unsplash.com/photo-1602002418816-5c0aeef426aa?q=80&w=1000&auto=format&fit=crop",
                            "https://images.unsplash.com/photo-1589979481223-deb893043163?q=80&w=1000&auto=format&fit=crop"
                        ),
                        frequentlyVisited = true,
                        peopleExplored = "250k+",
                        weather = weatherData.map { it.copy(temperature = it.temperature + 2) } // Warmer in Maldives
                    )
                }
                placeId.contains("kigali") -> {
                    PlaceDetail(
                        id = placeId,
                        name = "Kigali Resort",
                        location = "Kigali, Rwanda",
                        price = 1350f,
                        description = "Kigali Resort offers a luxurious retreat in the heart of Rwanda's capital. With stunning views of the surrounding hills, the resort combines modern amenities with traditional Rwandan hospitality. Guests can enjoy the spa, infinity pool, and gourmet dining experiences.",
                        imageUrl = "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?q=80&w=2070&auto=format&fit=crop",
                        galleryImages = listOf(
                            "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?q=80&w=1000&auto=format&fit=crop",
                            "https://images.unsplash.com/photo-1566073771259-6a8506099945?q=80&w=1000&auto=format&fit=crop",
                            "https://images.unsplash.com/photo-1582719508461-905c673771fd?q=80&w=1000&auto=format&fit=crop",
                            "https://images.unsplash.com/photo-1564501049412-61c2a3083791?q=80&w=1000&auto=format&fit=crop"
                        ),
                        frequentlyVisited = false,
                        peopleExplored = "50k+",
                        weather = weatherData.map { it.copy(temperature = it.temperature - 1) } // Cooler in Rwanda highlands
                    )
                }
                placeId.contains("mykonos") -> {
                    PlaceDetail(
                        id = placeId,
                        name = "Mykonos",
                        location = "Chora, Greece",
                        price = 1800f,
                        description = "Mykonos is a stunning Greek island known for its whitewashed buildings, vibrant nightlife, and beautiful beaches. The island offers a perfect blend of traditional Greek charm and cosmopolitan atmosphere, with iconic windmills, narrow winding streets, and crystal-clear waters.",
                        imageUrl = "https://images.unsplash.com/photo-1601581875039-e899893d520c?q=80&w=1974&auto=format&fit=crop",
                        galleryImages = listOf(
                            "https://images.unsplash.com/photo-1570077188670-e3a8d69ac5ff?q=80&w=1000&auto=format&fit=crop",
                            "https://images.unsplash.com/photo-1555400038-63f5ba517a47?q=80&w=1000&auto=format&fit=crop",
                            "https://images.unsplash.com/photo-1533104816931-20fa691ff6ca?q=80&w=1000&auto=format&fit=crop",
                            "https://images.unsplash.com/photo-1566478989037-eec170784d0b?q=80&w=1000&auto=format&fit=crop"
                        ),
                        frequentlyVisited = true,
                        peopleExplored = "300k+",
                        weather = weatherData.map { it.copy(weatherType = WeatherType.SUNNY) } // Always sunny in Mykonos
                    )
                }
                else -> {
                    // Default fallback for any other ID
                    PlaceDetail(
                        id = placeId,
                        name = "Destination",
                        location = "Location",
                        price = 1500f,
                        description = "This beautiful destination offers a unique travel experience with stunning landscapes and rich cultural heritage.",
                        imageUrl = "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?q=80&w=2070&auto=format&fit=crop",
                        galleryImages = galleryImages,
                        frequentlyVisited = false,
                        peopleExplored = "10k+",
                        weather = weatherData
                    )
                }
            }

            Result.success(placeDetail)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
