package com.example.hotel.data.model

/**
 * Provides fallback data for the app when API is unavailable
 */
object FallbackData {
    /**
     * Fallback data for frequently visited places
     */
    val frequentlyVisitedPlaces = listOf(
        Place(
            id = "fv1",
            leadingDestinationTitle = "Mykonos",
            subDestinationTitle = "Chora, Greece",
            description = "Beautiful island in Greece with stunning beaches and vibrant nightlife.",
            price = 1800f,
            iataCode = "JMK",
            geoPoint = PlaceGeoPoint(37.4467f, 25.3289f),
            imageUrl = "https://images.unsplash.com/photo-1601581875039-e899893d520c?q=80&w=1974&auto=format&fit=crop"
        ),
        Place(
            id = "fv2",
            leadingDestinationTitle = "Waterfort",
            subDestinationTitle = "Venesia, Italy",
            description = "Historic waterfort in the beautiful city of Venice, Italy.",
            price = 1800f,
            iataCode = "VCE",
            geoPoint = PlaceGeoPoint(45.4408f, 12.3155f),
            imageUrl = "https://images.unsplash.com/photo-1516483638261-f4dbaf036963?q=80&w=1986&auto=format&fit=crop"
        ),
        Place(
            id = "fv3",
            leadingDestinationTitle = "Delli",
            subDestinationTitle = "Chora, Greece",
            description = "Charming destination with rich history and culture.",
            price = 1800f,
            iataCode = "DEL",
            geoPoint = PlaceGeoPoint(28.6139f, 77.2090f),
            imageUrl = "https://images.unsplash.com/photo-1524492412937-b28074a5d7da?q=80&w=2071&auto=format&fit=crop"
        ),
        Place(
            id = "fv4",
            leadingDestinationTitle = "Baham",
            subDestinationTitle = "Venesia, Italy",
            description = "Tropical paradise with crystal clear waters and white sand beaches.",
            price = 1800f,
            iataCode = "NAS",
            geoPoint = PlaceGeoPoint(25.0343f, -77.3963f),
            imageUrl = "https://images.unsplash.com/photo-1548574505-5e239809ee19?q=80&w=1964&auto=format&fit=crop"
        )
    )

    /**
     * Fallback data for recommended places
     */
    val recommendedPlaces = listOf(
        Place(
            id = "rp1",
            leadingDestinationTitle = "Kigali Resort",
            subDestinationTitle = "Kigali, Rwanda",
            description = "Luxury resort in the heart of Rwanda with stunning views.",
            price = 1350f,
            iataCode = "KGL",
            geoPoint = PlaceGeoPoint(-1.9706f, 30.1044f),
            imageUrl = "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?q=80&w=2070&auto=format&fit=crop"
        ),
        Place(
            id = "rp2",
            leadingDestinationTitle = "Maldives",
            subDestinationTitle = "Maldives, Rep of Maldives",
            description = "Paradise island with overwater bungalows and turquoise waters.",
            price = 1350f,
            iataCode = "MLE",
            geoPoint = PlaceGeoPoint(3.2028f, 73.2207f),
            imageUrl = "https://images.unsplash.com/photo-1514282401047-d79a71a590e8?q=80&w=1965&auto=format&fit=crop"
        ),
        Place(
            id = "rp3",
            leadingDestinationTitle = "Sumbing Mount",
            subDestinationTitle = "Chora, Greece",
            description = "Mount Sumbing is an active stratovolcano in Central Java, Indonesia.",
            price = 1350f,
            iataCode = "JOG",
            geoPoint = PlaceGeoPoint(-7.3840f, 110.0708f),
            imageUrl = "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?q=80&w=2070&auto=format&fit=crop"
        ),
        Place(
            id = "rp4",
            leadingDestinationTitle = "Maldives",
            subDestinationTitle = "Maldives, Rep of Maldives",
            description = "Another beautiful resort in the Maldives with private beaches.",
            price = 1350f,
            iataCode = "MLE",
            geoPoint = PlaceGeoPoint(3.2028f, 73.2207f),
            imageUrl = "https://images.unsplash.com/photo-1573843981267-be1999ff37cd?q=80&w=1974&auto=format&fit=crop"
        ),
        Place(
            id = "rp5",
            leadingDestinationTitle = "Kigali Resort",
            subDestinationTitle = "Kigali, Rwanda",
            description = "Another luxury resort in Rwanda with excellent amenities.",
            price = 1350f,
            iataCode = "KGL",
            geoPoint = PlaceGeoPoint(-1.9706f, 30.1044f),
            imageUrl = "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?q=80&w=2070&auto=format&fit=crop"
        ),
        Place(
            id = "rp6",
            leadingDestinationTitle = "Sumbing Mount",
            subDestinationTitle = "Chora, Greece",
            description = "Another view of Mount Sumbing with its majestic peaks.",
            price = 1350f,
            iataCode = "JOG",
            geoPoint = PlaceGeoPoint(-7.3840f, 110.0708f),
            imageUrl = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?q=80&w=2070&auto=format&fit=crop"
        )
    )
}
