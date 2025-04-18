package com.example.hotel

import android.app.Application
import com.example.hotel.data.network.ApiClient

/**
 * Application class for the Hotel app
 */
class HotelApplication : Application() {
    
    companion object {
        lateinit var instance: HotelApplication
            private set
    }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        
        // Initialize ApiClient
        ApiClient.initialize(this)
    }
}
