package com.example.hotel.ui.screens

// Import necessary Compose libraries
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.draw.scale
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hotel.R
import com.example.hotel.ui.theme.Blue
import com.example.hotel.ui.theme.Black
import com.example.hotel.ui.theme.BlackTransparent
import com.example.hotel.ui.util.LocalWindowSizeClass
import com.example.hotel.ui.util.WindowWidthSizeClass
import com.example.hotel.ui.util.WindowHeightSizeClass
import com.example.hotel.ui.util.ResponsiveDimensions.responsiveWidth
import com.example.hotel.ui.util.ResponsiveDimensions.responsiveHeight
import com.example.hotel.ui.util.ResponsiveDimensions.responsiveTextSize

@Composable
fun WelcomeScreen(
    // Callback functions for button clicks
    onGetStartedClick: () -> Unit = {},
    onSignInClick: () -> Unit = {}
) {
    // Get window size class for responsive layout
    val windowSizeClass = LocalWindowSizeClass.current

    // Main container that fills the entire screen
    Box(
        // This modifier makes the Box fill the entire screen
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image collage
        // This is the main background with travel destination images
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Using the background image from downloads
            Image(
                painter = painterResource(id = R.drawable.background_image_main),
                // Description of the image for accessibility
                contentDescription = "Main background image",
                // FillWidth makes the image fill the width while maintaining aspect ratio
                contentScale = ContentScale.Crop,
                // This modifier makes the image much larger than the screen and positions it
                // to expand in the directions shown by the arrows
                modifier = Modifier
                    .fillMaxSize()
                    // Scale up the image significantly (1.5x) to make it expanded in all directions
                    .scale(1.5f)
                    // Shift the image to position it correctly - move it up and to the left
                    // This creates the expansion effect in the top-left and top-right directions
                    .offset(x = (-30).dp, y = (-80).dp)
            )
        }

        // Main content column
        // This contains all the text and buttons at the bottom of the screen
        Column(
            // This modifier makes the Column fill the entire screen and adds scrolling capability
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Add scrolling capability
                // Add responsive padding based on screen size
                .padding(horizontal = when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.COMPACT -> 45.responsiveWidth()
                    WindowWidthSizeClass.MEDIUM -> 60.responsiveWidth()
                    WindowWidthSizeClass.EXPANDED -> 80.responsiveWidth()
                }),
            // Place content at the bottom of the screen
            verticalArrangement = Arrangement.Bottom,
            // Center align content horizontally
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title text - "Discover Best Place To Vacation"
            Text(
                // Get text from string resources
                text = stringResource(R.string.welcome_title),
                // Center align the text
                textAlign = TextAlign.Center,
                // Black color for the text
                color = Black,
                // Responsive font size based on screen width
                fontSize = when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.COMPACT -> 30.responsiveTextSize()
                    WindowWidthSizeClass.MEDIUM -> 36.responsiveTextSize()
                    WindowWidthSizeClass.EXPANDED -> 42.responsiveTextSize()
                },
                // Bold font weight
                fontWeight = FontWeight.Bold,
                // Responsive line height
                lineHeight = when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.COMPACT -> 38.responsiveTextSize()
                    WindowWidthSizeClass.MEDIUM -> 44.responsiveTextSize()
                    WindowWidthSizeClass.EXPANDED -> 50.responsiveTextSize()
                }
            )

            // Add responsive space between title and subtitle
            Spacer(modifier = Modifier.height(21.responsiveHeight()))

            // Subtitle text
            Text(
                // Get text from string resources
                text = stringResource(R.string.welcome_subtitle),
                // Center align the text
                textAlign = TextAlign.Center,
                // Semi-transparent black color for the text
                color = BlackTransparent,
                // Responsive font size based on screen width
                fontSize = when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.COMPACT -> 16.responsiveTextSize()
                    WindowWidthSizeClass.MEDIUM -> 18.responsiveTextSize()
                    WindowWidthSizeClass.EXPANDED -> 20.responsiveTextSize()
                },
                // Responsive line height
                lineHeight = when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.COMPACT -> 20.responsiveTextSize()
                    WindowWidthSizeClass.MEDIUM -> 24.responsiveTextSize()
                    WindowWidthSizeClass.EXPANDED -> 28.responsiveTextSize()
                }
            )

            // Add responsive space between subtitle and button
            Spacer(modifier = Modifier.height(50.responsiveHeight()))

            // Get Started Button
            Button(
                // Callback function when button is clicked
                onClick = onGetStartedClick,
                // This modifier sets the width and height of the button with responsive dimensions
                modifier = Modifier
                    .width(when (windowSizeClass.widthSizeClass) {
                        WindowWidthSizeClass.COMPACT -> 300.responsiveWidth()
                        WindowWidthSizeClass.MEDIUM -> 350.responsiveWidth()
                        WindowWidthSizeClass.EXPANDED -> 400.responsiveWidth()
                    })
                    .height(45.responsiveHeight()),
                // Set button colors - blue background with white text
                colors = ButtonDefaults.buttonColors(
                    // Blue color from our theme
                    containerColor = Blue,
                    // White text color
                    contentColor = Color.White
                ),
                // Rounded corners with responsive radius
                shape = RoundedCornerShape(10.responsiveWidth())
            ) {
                // Button text
                Text(
                    // Get text from string resources
                    text = stringResource(R.string.get_started),
                    // White text color
                    color = Color.White,
                    // Medium font weight
                    fontWeight = FontWeight.Medium,
                    // Responsive font size
                    fontSize = when (windowSizeClass.widthSizeClass) {
                        WindowWidthSizeClass.COMPACT -> 16.responsiveTextSize()
                        WindowWidthSizeClass.MEDIUM -> 18.responsiveTextSize()
                        WindowWidthSizeClass.EXPANDED -> 20.responsiveTextSize()
                    }
                )
            }

            // Add responsive space between button and sign in text
            Spacer(modifier = Modifier.height(36.responsiveHeight()))

            // Already have an account text with Sign in button
            Row(
                // Center align content horizontally
                horizontalArrangement = Arrangement.Center,
                // Center align content vertically
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Regular text part
                Text(
                    // Regular text part
                    text = "Already have an account? ",
                    // Black color for the text
                    color = Black,
                    // Responsive font size
                    fontSize = when (windowSizeClass.widthSizeClass) {
                        WindowWidthSizeClass.COMPACT -> 14.responsiveTextSize()
                        WindowWidthSizeClass.MEDIUM -> 16.responsiveTextSize()
                        WindowWidthSizeClass.EXPANDED -> 18.responsiveTextSize()
                    }
                )

                // Clickable Sign in text
                TextButton(
                    // Callback function when Sign in is clicked
                    onClick = onSignInClick,
                    // Remove default padding
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                ) {
                    Text(
                        // Sign in text
                        text = "Sign in",
                        // Blue color for the text
                        color = Blue,
                        // Medium font weight
                        fontWeight = FontWeight.Medium,
                        // Responsive font size
                        fontSize = when (windowSizeClass.widthSizeClass) {
                            WindowWidthSizeClass.COMPACT -> 14.responsiveTextSize()
                            WindowWidthSizeClass.MEDIUM -> 16.responsiveTextSize()
                            WindowWidthSizeClass.EXPANDED -> 18.responsiveTextSize()
                        }
                    )
                }

                // Period at the end
                Text(
                    // Period text
                    text = ".",
                    // Black color for the text
                    color = Black,
                    // Responsive font size
                    fontSize = when (windowSizeClass.widthSizeClass) {
                        WindowWidthSizeClass.COMPACT -> 14.responsiveTextSize()
                        WindowWidthSizeClass.MEDIUM -> 16.responsiveTextSize()
                        WindowWidthSizeClass.EXPANDED -> 18.responsiveTextSize()
                    }
                )
            }

            // Add responsive space before home indicator
            Spacer(modifier = Modifier.height(34.responsiveHeight()))

            // Home indicator bar at the bottom
            Box(
                // This modifier sets the width, height, and appearance of the indicator with responsive dimensions
                modifier = Modifier
                    .width(when (windowSizeClass.widthSizeClass) {
                        WindowWidthSizeClass.COMPACT -> 135.responsiveWidth()
                        WindowWidthSizeClass.MEDIUM -> 150.responsiveWidth()
                        WindowWidthSizeClass.EXPANDED -> 180.responsiveWidth()
                    })
                    .height(5.responsiveHeight())
                    // Rounded corners for the indicator
                    .clip(RoundedCornerShape(100.dp))
                    // Black background color
                    .background(Black),
                // Center align content
                contentAlignment = Alignment.Center
            ) {}

            // Add a small responsive space at the bottom
            Spacer(modifier = Modifier.height(8.responsiveHeight()))
        }
    }
}
