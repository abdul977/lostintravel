package com.example.hotel.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Utility class for responsive dimensions
 * This helps scale UI elements based on screen size
 */
object ResponsiveDimensions {
    // Reference screen dimensions (based on a standard phone size)
    private const val REFERENCE_WIDTH = 390
    private const val REFERENCE_HEIGHT = 844

    /**
     * Calculate responsive dimension based on screen width
     * @param size The base size in dp for the reference screen width
     * @return The scaled size in dp for the current screen width
     */
    @Composable
    fun calculateWidthDimension(size: Int): Dp {
        val screenWidth = LocalConfiguration.current.screenWidthDp
        val scaleFactor = screenWidth.toFloat() / REFERENCE_WIDTH
        return (size * scaleFactor).dp
    }

    /**
     * Calculate responsive dimension based on screen height
     * @param size The base size in dp for the reference screen height
     * @return The scaled size in dp for the current screen height
     */
    @Composable
    fun calculateHeightDimension(size: Int): Dp {
        val screenHeight = LocalConfiguration.current.screenHeightDp
        val scaleFactor = screenHeight.toFloat() / REFERENCE_HEIGHT
        return (size * scaleFactor).dp
    }

    /**
     * Calculate responsive text size based on screen width
     * @param size The base size in sp for the reference screen width
     * @return The scaled size in sp for the current screen width
     */
    @Composable
    fun calculateTextSize(size: Int): TextUnit {
        val screenWidth = LocalConfiguration.current.screenWidthDp
        val scaleFactor = screenWidth.toFloat() / REFERENCE_WIDTH
        return (size * scaleFactor).sp
    }

    /**
     * Calculate responsive padding based on screen dimensions
     * @param horizontal The base horizontal padding in dp for the reference screen
     * @param vertical The base vertical padding in dp for the reference screen
     * @return A pair of scaled horizontal and vertical padding in dp
     */
    @Composable
    fun calculatePadding(horizontal: Int, vertical: Int): Pair<Dp, Dp> {
        val horizontalPadding = calculateWidthDimension(horizontal)
        val verticalPadding = calculateHeightDimension(vertical)
        return Pair(horizontalPadding, verticalPadding)
    }

    /**
     * Extension function for responsive width dimension
     */
    @Composable
    fun Int.responsiveWidth(): Dp = calculateWidthDimension(this)

    /**
     * Extension function for responsive height dimension
     */
    @Composable
    fun Int.responsiveHeight(): Dp = calculateHeightDimension(this)

    /**
     * Extension function for responsive text size
     */
    @Composable
    fun Int.responsiveTextSize(): TextUnit = calculateTextSize(this)
}
