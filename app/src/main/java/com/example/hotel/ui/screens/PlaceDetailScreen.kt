package com.example.hotel.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.hotel.R
import com.example.hotel.data.model.PlaceDetail
import com.example.hotel.data.model.WeatherInfo
import com.example.hotel.data.model.WeatherType
import com.example.hotel.ui.components.BottomNavigationBar
import com.example.hotel.ui.theme.Blue
import com.example.hotel.ui.util.LocalWindowSizeClass
import com.example.hotel.ui.util.ResponsiveDimensions.responsiveHeight
import com.example.hotel.ui.util.ResponsiveDimensions.responsiveTextSize
import com.example.hotel.ui.util.ResponsiveDimensions.responsiveWidth
import com.example.hotel.ui.util.WindowHeightSizeClass
import com.example.hotel.ui.util.WindowWidthSizeClass
import com.example.hotel.ui.viewmodel.PlaceDetailUiState
import com.example.hotel.ui.viewmodel.PlaceDetailViewModel

/**
 * Place detail screen
 */
@Composable
fun PlaceDetailScreen(
    placeId: String,
    viewModel: PlaceDetailViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
            androidx.compose.ui.platform.LocalContext.current.applicationContext as android.app.Application
        )
    ),
    onBackClick: () -> Unit
) {
    // Get window size class for responsive layout
    val windowSizeClass = LocalWindowSizeClass.current
    val uiState by viewModel.uiState.collectAsState()

    // Load place details when the screen is first displayed
    LaunchedEffect(placeId) {
        viewModel.loadPlaceDetails(placeId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Destination Detail") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Notification icon with badge
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(8.dp)
                    ) {
                        IconButton(
                            onClick = { /* Handle notification click */ },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0x0D007AFF))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.Black,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Notification badge
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color.Red)
                                .align(Alignment.TopEnd)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(currentRoute = "explore")
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            when (val state = uiState) {
                is PlaceDetailUiState.Loading -> {
                    // Show loading indicator
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Blue
                    )
                }

                is PlaceDetailUiState.Error -> {
                    // Show error message
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Error: ${state.message}",
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.loadPlaceDetails(placeId) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Blue
                            )
                        ) {
                            Text("Retry")
                        }
                    }
                }

                is PlaceDetailUiState.Success -> {
                    // Show place details
                    PlaceDetailContent(
                        placeDetail = state.placeDetail,
                        onBookNowClick = { /* Handle book now click */ },
                        onViewMapClick = { /* Handle view map click */ }
                    )
                }

                else -> {
                    // Initial state, do nothing
                }
            }
        }
    }
}

/**
 * Place detail content
 */
@Composable
fun PlaceDetailContent(
    placeDetail: PlaceDetail,
    onBookNowClick: () -> Unit,
    onViewMapClick: () -> Unit
) {
    // Get window size class for responsive layout
    val windowSizeClass = LocalWindowSizeClass.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Hero image with overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(when (windowSizeClass.heightSizeClass) {
                    WindowHeightSizeClass.COMPACT -> 200.responsiveHeight()
                    WindowHeightSizeClass.MEDIUM -> 250.responsiveHeight()
                    WindowHeightSizeClass.EXPANDED -> 300.responsiveHeight()
                })
        ) {
            // Main image
            AsyncImage(
                model = placeDetail.imageUrl,
                contentDescription = placeDetail.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Dark overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x4D000000))
            )

            // Content overlay
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                // Frequently visited badge
                if (placeDetail.frequentlyVisited) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Verified,
                            contentDescription = null,
                            tint = Color(0xFFFFD233),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Frequently Visited",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Place name
                Text(
                    text = placeDetail.name,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                // People explored
                Text(
                    text = "${placeDetail.peopleExplored} people have explored",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp)
                )

                // People avatars
                LazyRow(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy((-8).dp)
                ) {
                    items(9) { index ->
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.White, CircleShape)
                                .background(Color.Gray)
                        ) {
                            // In a real app, we would load actual user avatars
                            Image(
                                painter = painterResource(id = R.drawable.avatar_placeholder),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }

        // Place info section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = placeDetail.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFFFB1616),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = placeDetail.location,
                        fontSize = 13.sp,
                        color = Color(0xFFC0BDBD)
                    )
                }
            }

            Text(
                text = "$${placeDetail.price.toInt()}/person",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = Blue
            )
        }

        // View on map button
        TextButton(
            onClick = onViewMapClick,
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(
                text = "View on map",
                color = Blue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Description section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Description Destination",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = placeDetail.description,
                fontSize = 14.sp,
                color = Color(0x5C000000),
                lineHeight = 18.sp
            )
        }

        // Gallery section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Gallery Photo",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(placeDetail.galleryImages) { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp, 60.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                }
            }
        }

        // Weather section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Today's weather",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(placeDetail.weather) { weatherInfo ->
                    WeatherCard(weatherInfo = weatherInfo)
                }
            }
        }

        // Book now button
        Button(
            onClick = onBookNowClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .height(45.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "Book Now",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * Weather card component
 */
@Composable
fun WeatherCard(weatherInfo: WeatherInfo) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Weather icon card
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF85B6FF)),
            contentAlignment = Alignment.Center
        ) {
            // Weather icon based on type
            when (weatherInfo.weatherType) {
                WeatherType.SUNNY -> {
                    // Sun icon
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(
                                brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                    colors = listOf(Color(0xFFFAE26F), Color(0xFFF7BC3D))
                                )
                            )
                    )
                }
                WeatherType.PARTLY_CLOUDY -> {
                    // Sun with cloud
                    Box(
                        modifier = Modifier.size(22.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                        colors = listOf(Color(0xFFFAE26F), Color(0xFFF7BC3D))
                                    )
                                )
                                .align(Alignment.TopStart)
                        )
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                        colors = listOf(Color.White, Color(0xFFBDE0F5))
                                    )
                                )
                                .align(Alignment.BottomEnd)
                        )
                    }
                }
                WeatherType.CLOUDY -> {
                    // Cloud
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(
                                brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                    colors = listOf(Color.White, Color(0xFFBDE0F5))
                                )
                            )
                    )
                }
                WeatherType.RAINY -> {
                    // Cloud with rain
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                        colors = listOf(Color.White, Color(0xFFBDE0F5))
                                    )
                                )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(6.dp)
                                    .background(
                                        brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                            colors = listOf(Color(0x800077FF), Color(0x804EA8F4))
                                        ),
                                        shape = RoundedCornerShape(1.dp)
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(6.dp)
                                    .background(
                                        brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                            colors = listOf(Color(0x800077FF), Color(0x804EA8F4))
                                        ),
                                        shape = RoundedCornerShape(1.dp)
                                    )
                            )
                        }
                    }
                }
                WeatherType.STORMY -> {
                    // Cloud with lightning
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                        colors = listOf(Color(0xFF9E9E9E), Color(0xFF616161))
                                    )
                                )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .height(8.dp)
                                .background(
                                    color = Color(0xFFFFD600),
                                    shape = RoundedCornerShape(1.dp)
                                )
                        )
                    }
                }
            }

            // Temperature
            Text(
                text = "${weatherInfo.temperature}°C",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 4.dp)
            )
        }

        // Time
        Text(
            text = weatherInfo.time,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0x5C000000),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
