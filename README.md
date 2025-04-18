# LostInTravel - Android Travel App

A curated travel inspiration app built with modern Android development practices. This app allows users to discover travel destinations, sign in/sign up, and explore recommended places.

## Features

- **Authentication**: User registration and login with GraphQL integration
- **Home Screen**: Displays recommended travel destinations
- **Responsive Design**: Adapts to different screen sizes
- **Offline Support**: Caches data for offline viewing and provides fallback UI when API is unavailable
- **Modern UI**: Built with Jetpack Compose

## Setup Instructions

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 11 or higher
- Android SDK 35 (compileSdk)
- Minimum SDK 23 (Android 6.0)

### Installation

1. Clone the repository:
   ```
   git clone https://github.com/abdul977/lostintravel.git
   ```

2. Open the project in Android Studio

3. Sync Gradle files

4. Run the app on an emulator or physical device

## Architecture and Design Choices

### Architecture

The app follows the MVVM (Model-View-ViewModel) architecture pattern with clean architecture principles:

- **UI Layer**: Jetpack Compose UI components and ViewModels
- **Domain Layer**: Use cases and business logic
- **Data Layer**: Repositories, data sources, and models

### Key Technologies

- **Jetpack Compose**: For modern, declarative UI
- **Kotlin Coroutines & Flow**: For asynchronous operations
- **Retrofit & OkHttp**: For network requests
- **Kotlinx Serialization**: For JSON parsing
- **DataStore**: For persistent data storage
- **Coil**: For image loading
- **Navigation Compose**: For in-app navigation

### GraphQL Integration

The app uses Retrofit with custom interceptors to handle GraphQL queries and mutations. Authentication is managed through bearer tokens in request headers.

### Fallback Data System

The app implements a robust fallback mechanism that activates when:
- The API is unavailable or returns errors
- Network connectivity is lost
- Authentication issues occur

This ensures users always have content to view, even in offline scenarios. The fallback system:
- Provides pre-defined travel destinations with Unsplash images
- Maintains the same UI structure as the online experience
- Offers users an explicit option to "Use Offline Data" when network errors occur

### Responsive Design

The UI is designed to be responsive across different screen sizes using:
- Adaptive layouts
- Window size classes
- Flexible components

## Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/hotel/
│   │   │   ├── data/
│   │   │   │   ├── auth/         # Authentication management
│   │   │   │   ├── model/        # Data models and fallback data
│   │   │   │   ├── network/      # API and network components
│   │   │   │   └── repository/   # Data repositories
│   │   │   ├── ui/
│   │   │   │   ├── components/   # Reusable UI components
│   │   │   │   ├── navigation/   # Navigation setup
│   │   │   │   ├── screens/      # App screens
│   │   │   │   ├── theme/        # App theme and styling
│   │   │   │   └── util/         # UI utilities
│   │   │   ├── HotelApplication.kt
│   │   │   └── MainActivity.kt
│   │   └── res/                  # Resources
│   └── test/                     # Unit tests
└── build.gradle.kts              # App-level build configuration
```

## API Integration

The app integrates with a GraphQL API at `https://lostapi.frontendlabs.co.uk/graphql` for:
- User authentication
- Fetching recommended places
- Exploring destinations

## Key Screens

### Home Screen

The home screen is the central hub of the app, displaying:

- **User Profile Header**: Shows the user's name and profile picture
- **Search Bar**: Allows users to search for destinations (UI only in current version)
- **Frequently Visited**: Horizontal scrollable cards showing popular destinations
- **Recommended Places**: Vertical list of recommended travel destinations

The home screen implements a robust fallback mechanism that automatically activates when the API is unavailable. This ensures users always have content to view and can continue exploring the app even without an internet connection.

#### Fallback UI Features:

- Seamless transition between online and offline data
- Visual consistency with the online experience
- High-quality placeholder images from Unsplash
- Clear error messaging with options to retry or use offline data

## Future Improvements

- Implement Google Authentication
- Add more comprehensive error handling
- Further enhance offline capabilities with local database caching
- Implement search functionality
- Add unit and UI tests
- Sync offline actions when connectivity is restored
