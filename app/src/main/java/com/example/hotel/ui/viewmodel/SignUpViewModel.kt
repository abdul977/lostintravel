package com.example.hotel.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotel.data.model.User
import com.example.hotel.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Sign Up screen
 */
class SignUpViewModel : ViewModel() {
    private val repository = UserRepository()

    // UI state
    private val _uiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Initial)
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    // Form fields
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _location = MutableStateFlow("")
    val location: StateFlow<String> = _location.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    // Update form fields
    fun updateName(name: String) {
        _name.value = name
    }

    fun updateEmail(email: String) {
        _email.value = email
    }

    fun updateLocation(location: String) {
        _location.value = location
    }

    fun updatePassword(password: String) {
        _password.value = password
    }

    /**
     * Validates the form fields
     *
     * @return True if all fields are valid, false otherwise
     */
    private fun validateForm(): Boolean {
        return _name.value.isNotBlank() &&
                _email.value.isNotBlank() && _email.value.contains("@") &&
                _location.value.isNotBlank() &&
                _password.value.isNotBlank() && _password.value.length >= 6
    }

    /**
     * Attempts to sign up the user with the provided information
     */
    fun signUp() {
        if (!validateForm()) {
            _uiState.value = SignUpUiState.Error("Please fill all fields correctly")
            return
        }

        _uiState.value = SignUpUiState.Loading

        viewModelScope.launch {
            // For simplicity, we're using fixed coordinates
            // In a real app, we would use location services to get the user's location
            val latitude = "51.5074"
            val longitude = "-0.1278"

            val result = repository.createUser(
                email = _email.value,
                fullName = _name.value,
                latitude = latitude,
                longitude = longitude,
                password = _password.value
            )

            _uiState.value = result.fold(
                onSuccess = { user ->
                    // After successful signup, we should refresh the API client
                    // Note: In a real app, we would also save the token here if returned from signup
                    android.util.Log.d("SignUpViewModel", "User created successfully: ${user.id}")
                    com.example.hotel.data.network.ApiClient.refreshApiService()
                    SignUpUiState.Success(user)
                },
                onFailure = {
                    android.util.Log.e("SignUpViewModel", "Signup failed: ${it.message}")
                    SignUpUiState.Error(it.message ?: "Unknown error")
                }
            )
        }
    }

    /**
     * Resets the UI state to Initial
     */
    fun resetState() {
        _uiState.value = SignUpUiState.Initial
    }
}

/**
 * UI state for the Sign Up screen
 */
sealed class SignUpUiState {
    object Initial : SignUpUiState()
    object Loading : SignUpUiState()
    data class Success(val user: User) : SignUpUiState()
    data class Error(val message: String) : SignUpUiState()
}
