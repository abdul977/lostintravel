package com.example.hotel.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotel.data.auth.AuthManager
import com.example.hotel.data.model.LoginResponse
import com.example.hotel.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * UI state for the sign in screen
 */
sealed class SignInUiState {
    /**
     * Initial state
     */
    object Initial : SignInUiState()

    /**
     * Loading state
     */
    object Loading : SignInUiState()

    /**
     * Success state
     */
    data class Success(val data: LoginResponse) : SignInUiState()

    /**
     * Error state
     */
    data class Error(val message: String) : SignInUiState()
}

/**
 * ViewModel for the sign in screen
 */
class SignInViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository()
    private val authManager = AuthManager.getInstance(application)

    // Email state
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    // Password state
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    // UI state
    private val _uiState = MutableStateFlow<SignInUiState>(SignInUiState.Initial)
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    /**
     * Updates the email state
     */
    fun updateEmail(email: String) {
        _email.value = email
    }

    /**
     * Updates the password state
     */
    fun updatePassword(password: String) {
        _password.value = password
    }

    /**
     * Validates the form
     */
    private fun validateForm(): Boolean {
        return _email.value.isNotBlank() && _password.value.isNotBlank()
    }

    /**
     * Attempts to sign in the user with the provided credentials
     */
    fun signIn() {
        if (!validateForm()) {
            _uiState.value = SignInUiState.Error("Please fill all fields")
            return
        }

        _uiState.value = SignInUiState.Loading

        viewModelScope.launch {
            val result = repository.login(
                email = _email.value,
                password = _password.value
            )

            result.fold(
                onSuccess = { loginResponse ->
                    // Save the token
                    val token = loginResponse.token
                    android.util.Log.d("SignInViewModel", "Saving token: ${token.take(10)}${if (token.length > 10) "..." else ""} (${token.length} chars)")
                    authManager.saveToken(token)

                    // Refresh the API client to use the new token
                    com.example.hotel.data.network.ApiClient.refreshApiService()
                    android.util.Log.d("SignInViewModel", "API client refreshed with new token")

                    _uiState.value = SignInUiState.Success(loginResponse)
                },
                onFailure = {
                    _uiState.value = SignInUiState.Error(it.message ?: "Unknown error")
                }
            )
        }
    }

    /**
     * Resets the UI state
     */
    fun resetState() {
        _uiState.value = SignInUiState.Initial
    }
}
