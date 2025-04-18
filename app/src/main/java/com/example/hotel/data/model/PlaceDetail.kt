package com.example.hotel.data.model

/**
 * Detailed information about a place
 */
data class PlaceDetail(
    val id: String,
    val name: String,
    val location: String,
    val price: Float,
    val description: String,
    val imageUrl: String,
    val galleryImages: List<String>,
    val frequentlyVisited: Boolean,
    val peopleExplored: String,
    val weather: List<WeatherInfo>
)

/**
 * Weather information for a place
 */
data class WeatherInfo(
    val time: String,
    val temperature: Int,
    val weatherType: WeatherType,
    val isNow: Boolean = false
)

/**
 * Types of weather
 */
enum class WeatherType {
    SUNNY,
    PARTLY_CLOUDY,
    CLOUDY,
    RAINY,
    STORMY
}
