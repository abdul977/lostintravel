package com.example.hotel.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Place model representing a travel destination
 */
@Serializable
data class Place(
    @SerialName("_id")
    val id: String,

    @SerialName("leadingDestinationTitle")
    val leadingDestinationTitle: String,

    @SerialName("subDestinationTitle")
    val subDestinationTitle: String,

    @SerialName("description")
    val description: String,

    @SerialName("price")
    val price: Float,

    @SerialName("iataCode")
    val iataCode: String,

    @SerialName("geoPoint")
    val geoPoint: PlaceGeoPoint,

    @SerialName("imageUrl")
    val imageUrl: String
)

/**
 * Geographic point model for a place
 */
@Serializable
data class PlaceGeoPoint(
    @SerialName("latitude")
    val latitude: Float,

    @SerialName("longitude")
    val longitude: Float
)

/**
 * GraphQL response wrapper for Place data
 */
@Serializable
data class PlaceResponse(
    @SerialName("data")
    val data: PlaceData? = null,

    @SerialName("errors")
    val errors: List<GraphQLError>? = null
)

/**
 * Data container for Place queries
 */
@Serializable
data class PlaceData(
    @SerialName("RecommendedPlaces")
    val recommendedPlaces: List<Place>? = null,

    @SerialName("ExplorePlaces")
    val explorePlaces: List<Place>? = null
)

/**
 * Request model for place queries
 */
@Serializable
data class PlaceRequest(
    @SerialName("query")
    val query: String,

    @SerialName("variables")
    val variables: Map<String, String>? = null
)
