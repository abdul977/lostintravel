package com.example.hotel.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotel.data.model.PlaceDetail
import com.example.hotel.data.repository.PlaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * UI state for the place detail screen
 */
sealed class PlaceDetailUiState {
    /**
     * Initial state
     */
    object Initial : PlaceDetailUiState()

    /**
     * Loading state
     */
    object Loading : PlaceDetailUiState()

    /**
     * Success state with data
     */
    data class Success(val placeDetail: PlaceDetail) : PlaceDetailUiState()

    /**
     * Error state
     */
    data class Error(val message: String) : PlaceDetailUiState()
}

/**
 * ViewModel for the Place Detail screen
 */
class PlaceDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PlaceRepository()

    // UI state
    private val _uiState = MutableStateFlow<PlaceDetailUiState>(PlaceDetailUiState.Initial)
    val uiState: StateFlow<PlaceDetailUiState> = _uiState.asStateFlow()

    /**
     * Loads place details for the given place ID
     */
    fun loadPlaceDetails(placeId: String) {
        _uiState.value = PlaceDetailUiState.Loading

        viewModelScope.launch {
            val result = repository.getPlaceDetails(placeId)

            _uiState.value = result.fold(
                onSuccess = { placeDetail ->
                    PlaceDetailUiState.Success(placeDetail)
                },
                onFailure = { error ->
                    PlaceDetailUiState.Error(error.message ?: "Failed to load place details")
                }
            )
        }
    }
}
