package com.example.hotel.data.exception

/**
 * Exception thrown when the authentication token is invalid or expired
 */
class InvalidTokenException : Exception("Authentication token is invalid or expired. Please sign in again.")
