package com.example.hotel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.example.hotel.data.network.ApiClient
import com.example.hotel.ui.navigation.AppNavigation
import com.example.hotel.ui.theme.HotelTheme
import com.example.hotel.ui.util.LocalWindowSizeClass
import com.example.hotel.ui.util.rememberWindowSizeClass

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ApiClient with context for authentication
        ApiClient.initialize(applicationContext)

        setContent {
            HotelTheme {
                // Get window size class for responsive design
                val windowSizeClass = rememberWindowSizeClass()

                // Provide window size class to the entire app
                CompositionLocalProvider(LocalWindowSizeClass provides windowSizeClass) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AppNavigation()
                    }
                }
            }
        }
    }
}
