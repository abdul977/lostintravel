package com.example.hotel.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

/**
 * Window size classes to determine layout based on screen width
 */
enum class WindowWidthSizeClass {
    COMPACT, // Phone portrait
    MEDIUM,  // Phone landscape or small tablet
    EXPANDED // Tablet or larger
}

/**
 * Window size classes to determine layout based on screen height
 */
enum class WindowHeightSizeClass {
    COMPACT, // Phone landscape
    MEDIUM,  // Phone portrait or small tablet
    EXPANDED // Tablet or larger
}

/**
 * Data class to hold window size information
 */
data class WindowSizeClass(
    val widthSizeClass: WindowWidthSizeClass,
    val heightSizeClass: WindowHeightSizeClass
)

/**
 * Composable to calculate window size class based on screen dimensions
 * @return WindowSizeClass containing width and height size classes
 */
@Composable
fun rememberWindowSizeClass(): WindowSizeClass {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    return remember(screenWidth, screenHeight) {
        val widthSizeClass = when {
            screenWidth < 600.dp -> WindowWidthSizeClass.COMPACT
            screenWidth < 840.dp -> WindowWidthSizeClass.MEDIUM
            else -> WindowWidthSizeClass.EXPANDED
        }

        val heightSizeClass = when {
            screenHeight < 480.dp -> WindowHeightSizeClass.COMPACT
            screenHeight < 900.dp -> WindowHeightSizeClass.MEDIUM
            else -> WindowHeightSizeClass.EXPANDED
        }

        WindowSizeClass(widthSizeClass, heightSizeClass)
    }
}

/**
 * CompositionLocal to provide window size class to the entire app
 */
val LocalWindowSizeClass = compositionLocalOf {
    WindowSizeClass(
        widthSizeClass = WindowWidthSizeClass.COMPACT,
        heightSizeClass = WindowHeightSizeClass.MEDIUM
    )
}
