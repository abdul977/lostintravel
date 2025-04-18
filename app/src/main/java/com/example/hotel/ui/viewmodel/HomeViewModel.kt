package com.example.hotel.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotel.data.auth.AuthManager
import com.example.hotel.data.exception.InvalidTokenException
import com.example.hotel.data.model.FallbackData
import com.example.hotel.data.model.Place
import com.example.hotel.data.repository.PlaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * UI state for the home screen
 */
sealed class HomeUiState {
    /**
     * Initial state
     */
    object Initial : HomeUiState()

    /**
     * Loading state
     */
    object Loading : HomeUiState()

    /**
     * Success state with data
     */
    data class Success(
        val recommendedPlaces: List<Place>,
        val frequentlyVisited: List<Place> = emptyList()
    ) : HomeUiState()

    /**
     * Error state
     */
    data class Error(val message: String, val isAuthError: Boolean = false) : HomeUiState()
}

/**
 * ViewModel for the Home screen
 */
class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PlaceRepository()
    private val authManager = AuthManager.getInstance(application)

    // UI state
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Initial)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // User name - would normally come from user preferences or session
    private val _userName = MutableStateFlow("Samira")
    val userName: StateFlow<String> = _userName.asStateFlow()

    init {
        // Check if user is authenticated before loading data
        viewModelScope.launch {
            authManager.isAuthenticated().collect { isAuthenticated ->
                android.util.Log.d("HomeViewModel", "User is authenticated: $isAuthenticated")
                if (isAuthenticated) {
                    loadRecommendedPlaces()
                } else {
                    _uiState.value = HomeUiState.Error("Please sign in to view recommended places")
                }
            }
        }
    }

    /**
     * Loads recommended places from the repository
     */
    fun loadRecommendedPlaces() {
        _uiState.value = HomeUiState.Loading

        viewModelScope.launch {
            // Check if token exists and is valid
            val token = authManager.getCurrentToken()
            android.util.Log.d("HomeViewModel", "Current token before API call: ${token?.take(10)}${if (token?.length ?: 0 > 10) "..." else ""} (${token?.length ?: 0} chars)")

            if (token.isNullOrEmpty()) {
                _uiState.value = HomeUiState.Error(
                    message = "Authentication token is missing. Please sign in again.",
                    isAuthError = true
                )
                return@launch
            }

            // Validate token format
            if (!authManager.isTokenValid()) {
                android.util.Log.w("HomeViewModel", "Token validation failed")

                // Try to refresh the token
                val refreshed = authManager.refreshToken()
                if (refreshed) {
                    android.util.Log.d("HomeViewModel", "Token refreshed successfully")
                    com.example.hotel.data.network.ApiClient.refreshApiService()
                    // Continue with the request since we have a new token
                } else {
                    // Clear the invalid token
                    android.util.Log.w("HomeViewModel", "Token refresh failed, clearing token")
                    authManager.clearToken()
                    com.example.hotel.data.network.ApiClient.refreshApiService()

                    _uiState.value = HomeUiState.Error(
                        message = "Your session has expired. Please sign in again.",
                        isAuthError = true
                    )
                    return@launch
                }
            }

            try {
                val result = repository.getRecommendedPlaces()

                _uiState.value = result.fold(
                    onSuccess = { places ->
                        // For now, we'll use the same places for frequently visited
                        // In a real app, this would come from user history or preferences
                        val frequentlyVisited = places.take(3)
                        HomeUiState.Success(places, frequentlyVisited)
                    },
                    onFailure = { error ->
                        android.util.Log.e("HomeViewModel", "Failed to load places: ${error.message}")
                        when (error) {
                            is InvalidTokenException -> {
                                // Clear the token since it's invalid
                                viewModelScope.launch {
                                    authManager.clearToken()
                                    com.example.hotel.data.network.ApiClient.refreshApiService()
                                }
                                // Use fallback data instead of showing an error
                                android.util.Log.d("HomeViewModel", "Using fallback data due to authentication error")
                                HomeUiState.Success(
                                    recommendedPlaces = FallbackData.recommendedPlaces,
                                    frequentlyVisited = FallbackData.frequentlyVisitedPlaces
                                )
                            }
                            else -> {
                                // Use fallback data instead of showing an error
                                android.util.Log.d("HomeViewModel", "Using fallback data due to general error: ${error.message}")
                                HomeUiState.Success(
                                    recommendedPlaces = FallbackData.recommendedPlaces,
                                    frequentlyVisited = FallbackData.frequentlyVisitedPlaces
                                )
                            }
                        }
                    }
                )
            } catch (e: Exception) {
                android.util.Log.e("HomeViewModel", "Exception during API call: ${e.message}")
                // Use fallback data in case of any exception
                _uiState.value = HomeUiState.Success(
                    recommendedPlaces = FallbackData.recommendedPlaces,
                    frequentlyVisited = FallbackData.frequentlyVisitedPlaces
                )
            }
        }
    }

    /**
     * Refreshes the data
     */
    fun refresh() {
        loadRecommendedPlaces()
    }

    /**
     * Signs out the user by clearing the authentication token
     */
    fun signOut() {
        viewModelScope.launch {
            android.util.Log.d("HomeViewModel", "Signing out user")
            authManager.clearToken()
            // Also refresh the API client to clear any cached token
            com.example.hotel.data.network.ApiClient.refreshApiService()
            android.util.Log.d("HomeViewModel", "API client refreshed after sign out")

            // After signing out, show fallback data instead of error
            _uiState.value = HomeUiState.Success(
                recommendedPlaces = FallbackData.recommendedPlaces,
                frequentlyVisited = FallbackData.frequentlyVisitedPlaces
            )
        }
    }

    /**
     * Loads fallback data when API is unavailable
     */
    fun loadFallbackData() {
        _uiState.value = HomeUiState.Success(
            recommendedPlaces = FallbackData.recommendedPlaces,
            frequentlyVisited = FallbackData.frequentlyVisitedPlaces
        )
    }
}
