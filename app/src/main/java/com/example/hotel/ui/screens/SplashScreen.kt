package com.example.hotel.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.hotel.R
import com.example.hotel.ui.theme.Blue
import com.example.hotel.ui.util.LocalWindowSizeClass
import com.example.hotel.ui.util.WindowWidthSizeClass
import com.example.hotel.ui.util.WindowHeightSizeClass
import com.example.hotel.ui.util.ResponsiveDimensions.responsiveWidth
import com.example.hotel.ui.util.ResponsiveDimensions.responsiveHeight
import kotlinx.coroutines.delay

/**
 * Splash screen with rotating logo animation
 *
 * @param onSplashFinished Callback for when the splash animation is complete
 */
@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit = {}
) {
    // Get window size class for responsive layout
    val windowSizeClass = LocalWindowSizeClass.current
    // State to track if animation has started
    var startAnimation by remember { mutableStateOf(false) }

    // State to track animation completion
    var animationCompleted by remember { mutableStateOf(false) }

    // Rotation animation
    val rotation by animateFloatAsState(
        targetValue = if (startAnimation) -720f else 0f, // 2 full rotations counterclockwise
        animationSpec = tween(
            durationMillis = 2000, // 2 seconds for 2 rotations
            easing = LinearEasing
        ),
        label = "rotation",
        finishedListener = {
            animationCompleted = true
        }
    )

    // Start animation after a short delay
    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2500) // Wait for animation to complete plus a small buffer
        onSplashFinished()
    }

    // Gradient background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Blue.copy(alpha = 0.8f),
                        Blue
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Top map image - moved higher as per arrow direction and flipped upside down
        Image(
            painter = painterResource(id = R.drawable.map),
            contentDescription = "Map top",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth(1.2f) // Slightly wider to ensure full coverage
                .padding(top = when (windowSizeClass.heightSizeClass) {
                    WindowHeightSizeClass.COMPACT -> 0.responsiveHeight() // Reduced padding to move up
                    WindowHeightSizeClass.MEDIUM -> 0.responsiveHeight() // Reduced padding to move up
                    WindowHeightSizeClass.EXPANDED -> 0.responsiveHeight() // Reduced padding to move up
                })
                .rotate(180f) // Rotate 180 degrees to flip upside down
                .align(Alignment.TopCenter)
        )

        // Bottom map image - moved lower as per arrow direction
        Image(
            painter = painterResource(id = R.drawable.map),
            contentDescription = "Map bottom",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth(1.2f) // Slightly wider to ensure full coverage
                .padding(bottom = when (windowSizeClass.heightSizeClass) {
                    WindowHeightSizeClass.COMPACT -> 0.responsiveHeight() // Reduced padding to move down
                    WindowHeightSizeClass.MEDIUM -> 0.responsiveHeight() // Reduced padding to move down
                    WindowHeightSizeClass.EXPANDED -> 0.responsiveHeight() // Reduced padding to move down
                })
                .align(Alignment.BottomCenter)
        )

        // Centered rotating logo with shadow
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.COMPACT -> 160.responsiveWidth()
                WindowWidthSizeClass.MEDIUM -> 200.responsiveWidth()
                WindowWidthSizeClass.EXPANDED -> 240.responsiveWidth()
            })
        ) {
            // Shadow effect
            Image(
                painter = painterResource(id = R.drawable.ic_hotel_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(when (windowSizeClass.widthSizeClass) {
                        WindowWidthSizeClass.COMPACT -> 155.responsiveWidth()
                        WindowWidthSizeClass.MEDIUM -> 195.responsiveWidth()
                        WindowWidthSizeClass.EXPANDED -> 235.responsiveWidth()
                    })
                    .rotate(rotation) // Apply rotation animation
                    .padding(8.responsiveWidth()),
                alpha = 0.3f
            )

            // Main logo
            Image(
                painter = painterResource(id = R.drawable.ic_hotel_logo),
                contentDescription = "Hotel Logo",
                modifier = Modifier
                    .size(when (windowSizeClass.widthSizeClass) {
                        WindowWidthSizeClass.COMPACT -> 150.responsiveWidth()
                        WindowWidthSizeClass.MEDIUM -> 190.responsiveWidth()
                        WindowWidthSizeClass.EXPANDED -> 230.responsiveWidth()
                    })
                    .rotate(rotation) // Apply rotation animation
            )
        }
    }
}
