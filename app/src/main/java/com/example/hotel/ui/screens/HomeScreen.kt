package com.example.hotel.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Search
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.example.hotel.R
import com.example.hotel.ui.components.BottomNavigationBar
import com.example.hotel.ui.components.FrequentlyVisitedCard
import com.example.hotel.ui.components.RecommendedPlaceCard
import com.example.hotel.ui.theme.Blue
import com.example.hotel.ui.viewmodel.HomeUiState
import com.example.hotel.ui.viewmodel.HomeViewModel
import com.example.hotel.ui.util.LocalWindowSizeClass
import com.example.hotel.ui.util.WindowWidthSizeClass
import com.example.hotel.ui.util.WindowHeightSizeClass
import com.example.hotel.ui.util.ResponsiveDimensions.responsiveWidth
import com.example.hotel.ui.util.ResponsiveDimensions.responsiveHeight
import com.example.hotel.ui.util.ResponsiveDimensions.responsiveTextSize

/**
 * Main home screen composable
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(factory = ViewModelProvider.AndroidViewModelFactory.getInstance(androidx.compose.ui.platform.LocalContext.current.applicationContext as android.app.Application)),
    onSignOut: () -> Unit = {}
) {
    // Get window size class for responsive layout
    val windowSizeClass = LocalWindowSizeClass.current
    val uiState by viewModel.uiState.collectAsState()
    val userName by viewModel.userName.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(currentRoute = "home")
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            when (val state = uiState) {
                is HomeUiState.Loading -> {
                    // Show loading indicator
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Blue
                    )
                }

                is HomeUiState.Error -> {
                    // Show error message with option to load fallback data
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

                        if (state.isAuthError) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = onSignOut,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Blue
                                    )
                                ) {
                                    Text("Sign In Again")
                                }

                                Button(
                                    onClick = { viewModel.loadFallbackData() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF4CAF50)
                                    )
                                ) {
                                    Text("Use Offline Data")
                                }
                            }
                        } else {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { viewModel.refresh() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Blue
                                    )
                                ) {
                                    Text("Retry")
                                }

                                Button(
                                    onClick = { viewModel.loadFallbackData() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF4CAF50)
                                    )
                                ) {
                                    Text("Use Offline Data")
                                }
                            }
                        }
                    }
                }

                is HomeUiState.Success -> {
                    // Show content
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            // User profile header
                            UserProfileHeader(
                                userName = userName,
                                onNotificationClick = { /* Handle notification click */ },
                                onSignOut = onSignOut,
                                viewModel = viewModel
                            )

                            // Search bar
                            SearchBar(
                                onSearchClick = { /* Handle search click */ }
                            )

                            // Frequently visited section
                            if (state.frequentlyVisited.isNotEmpty()) {
                                FrequentlyVisitedSection(
                                    places = state.frequentlyVisited
                                )
                            }

                            // Recommended places section
                            RecommendedPlacesSection(
                                places = state.recommendedPlaces
                            )
                        }
                    }
                }

                HomeUiState.Initial -> {
                    // Initial state - load fallback data immediately
                    LaunchedEffect(Unit) {
                        viewModel.loadFallbackData()
                    }

                    // Show loading indicator while fallback data is being loaded
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Blue
                    )
                }
            }
        }
    }
}

/**
 * User profile header section
 */
@Composable
fun UserProfileHeader(
    userName: String,
    onNotificationClick: () -> Unit = {},
    onSignOut: () -> Unit = {},
    viewModel: HomeViewModel? = null
) {
    // Get window size class for responsive layout
    val windowSizeClass = LocalWindowSizeClass.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.COMPACT -> 21.responsiveWidth()
                    WindowWidthSizeClass.MEDIUM -> 30.responsiveWidth()
                    WindowWidthSizeClass.EXPANDED -> 40.responsiveWidth()
                },
                vertical = when (windowSizeClass.heightSizeClass) {
                    WindowHeightSizeClass.COMPACT -> 12.responsiveHeight()
                    WindowHeightSizeClass.MEDIUM -> 16.responsiveHeight()
                    WindowHeightSizeClass.EXPANDED -> 20.responsiveHeight()
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // User avatar
        Box(
            modifier = Modifier
                .size(when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.COMPACT -> 36.responsiveWidth()
                    WindowWidthSizeClass.MEDIUM -> 42.responsiveWidth()
                    WindowWidthSizeClass.EXPANDED -> 48.responsiveWidth()
                })
                .clip(CircleShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.avatar_placeholder),
                contentDescription = "User avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Online indicator
            Box(
                modifier = Modifier
                    .size(when (windowSizeClass.widthSizeClass) {
                        WindowWidthSizeClass.COMPACT -> 4.responsiveWidth()
                        WindowWidthSizeClass.MEDIUM -> 5.responsiveWidth()
                        WindowWidthSizeClass.EXPANDED -> 6.responsiveWidth()
                    })
                    .clip(CircleShape)
                    .background(Color(0xFF4CD964))
                    .align(Alignment.BottomEnd)
            )
        }

        Spacer(modifier = Modifier.width(14.responsiveWidth()))

        // Welcome text
        Column {
            Text(
                text = "Welcome $userName,",
                fontWeight = FontWeight.Normal,
                fontSize = when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.COMPACT -> 14.responsiveTextSize()
                    WindowWidthSizeClass.MEDIUM -> 16.responsiveTextSize()
                    WindowWidthSizeClass.EXPANDED -> 18.responsiveTextSize()
                },
                lineHeight = when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.COMPACT -> 18.responsiveTextSize()
                    WindowWidthSizeClass.MEDIUM -> 20.responsiveTextSize()
                    WindowWidthSizeClass.EXPANDED -> 22.responsiveTextSize()
                },
                letterSpacing = (-0.165).sp,
                color = Color.Black
            )

            Text(
                text = "Where do you want to go?",
                fontWeight = FontWeight.Normal,
                fontSize = when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.COMPACT -> 10.responsiveTextSize()
                    WindowWidthSizeClass.MEDIUM -> 12.responsiveTextSize()
                    WindowWidthSizeClass.EXPANDED -> 14.responsiveTextSize()
                },
                lineHeight = when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.COMPACT -> 13.responsiveTextSize()
                    WindowWidthSizeClass.MEDIUM -> 15.responsiveTextSize()
                    WindowWidthSizeClass.EXPANDED -> 17.responsiveTextSize()
                },
                letterSpacing = (-0.165).sp,
                color = Color(0x5C000000)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Sign out button
        TextButton(
            onClick = {
                viewModel?.signOut()
                onSignOut()
            },
            modifier = Modifier.padding(end = 8.responsiveWidth())
        ) {
            Text(
                text = "Sign Out",
                color = Blue,
                fontSize = when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.COMPACT -> 12.responsiveTextSize()
                    WindowWidthSizeClass.MEDIUM -> 14.responsiveTextSize()
                    WindowWidthSizeClass.EXPANDED -> 16.responsiveTextSize()
                },
                fontWeight = FontWeight.Medium
            )
        }

        // Notification icon with badge
        Box(
            modifier = Modifier
                .size(when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.COMPACT -> 30.responsiveWidth()
                    WindowWidthSizeClass.MEDIUM -> 36.responsiveWidth()
                    WindowWidthSizeClass.EXPANDED -> 42.responsiveWidth()
                })
        ) {
            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color(0x0D007AFF))
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = Color.Black,
                    modifier = Modifier.size(when (windowSizeClass.widthSizeClass) {
                        WindowWidthSizeClass.COMPACT -> 20.responsiveWidth()
                        WindowWidthSizeClass.MEDIUM -> 24.responsiveWidth()
                        WindowWidthSizeClass.EXPANDED -> 28.responsiveWidth()
                    })
                )
            }

            // Notification badge
            Box(
                modifier = Modifier
                    .size(when (windowSizeClass.widthSizeClass) {
                        WindowWidthSizeClass.COMPACT -> 6.responsiveWidth()
                        WindowWidthSizeClass.MEDIUM -> 8.responsiveWidth()
                        WindowWidthSizeClass.EXPANDED -> 10.responsiveWidth()
                    })
                    .clip(CircleShape)
                    .background(Color.Red)
                    .align(Alignment.TopEnd)
            )
        }
    }
}

/**
 * Search bar component
 */
@Composable
fun SearchBar(
    onSearchClick: () -> Unit = {}
) {
    // Get window size class for responsive layout
    val windowSizeClass = LocalWindowSizeClass.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.COMPACT -> 21.responsiveWidth()
                WindowWidthSizeClass.MEDIUM -> 30.responsiveWidth()
                WindowWidthSizeClass.EXPANDED -> 40.responsiveWidth()
            })
            .height(when (windowSizeClass.heightSizeClass) {
                WindowHeightSizeClass.COMPACT -> 38.responsiveHeight()
                WindowHeightSizeClass.MEDIUM -> 42.responsiveHeight()
                WindowHeightSizeClass.EXPANDED -> 48.responsiveHeight()
            })
            .clip(RoundedCornerShape(5.responsiveWidth()))
            .background(Color(0x0D007AFF))
            .padding(horizontal = when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.COMPACT -> 16.responsiveWidth()
                WindowWidthSizeClass.MEDIUM -> 20.responsiveWidth()
                WindowWidthSizeClass.EXPANDED -> 24.responsiveWidth()
            }),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "Search",
                tint = Color(0xFFCECCCC),
                modifier = Modifier.size(when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.COMPACT -> 16.responsiveWidth()
                    WindowWidthSizeClass.MEDIUM -> 20.responsiveWidth()
                    WindowWidthSizeClass.EXPANDED -> 24.responsiveWidth()
                })
            )

            Spacer(modifier = Modifier.width(10.responsiveWidth()))

            Text(
                text = stringResource(R.string.search_for_places),
                fontWeight = FontWeight.Normal,
                fontSize = when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.COMPACT -> 10.responsiveTextSize()
                    WindowWidthSizeClass.MEDIUM -> 12.responsiveTextSize()
                    WindowWidthSizeClass.EXPANDED -> 14.responsiveTextSize()
                },
                lineHeight = when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.COMPACT -> 13.responsiveTextSize()
                    WindowWidthSizeClass.MEDIUM -> 15.responsiveTextSize()
                    WindowWidthSizeClass.EXPANDED -> 17.responsiveTextSize()
                },
                letterSpacing = (-0.165).sp,
                color = Color(0xFFCECCCC)
            )
        }
    }

    Spacer(modifier = Modifier.height(16.responsiveHeight()))
}

/**
 * Frequently visited section
 */
@Composable
fun FrequentlyVisitedSection(
    places: List<com.example.hotel.data.model.Place>
) {
    // Get window size class for responsive layout
    val windowSizeClass = LocalWindowSizeClass.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.responsiveHeight())
    ) {
        Text(
            text = stringResource(R.string.frequently_visited),
            fontWeight = FontWeight.Normal,
            fontSize = when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.COMPACT -> 14.responsiveTextSize()
                WindowWidthSizeClass.MEDIUM -> 16.responsiveTextSize()
                WindowWidthSizeClass.EXPANDED -> 18.responsiveTextSize()
            },
            lineHeight = when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.COMPACT -> 18.responsiveTextSize()
                WindowWidthSizeClass.MEDIUM -> 20.responsiveTextSize()
                WindowWidthSizeClass.EXPANDED -> 22.responsiveTextSize()
            },
            letterSpacing = (-0.165).sp,
            color = Color.Black,
            modifier = Modifier.padding(start = when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.COMPACT -> 27.responsiveWidth()
                WindowWidthSizeClass.MEDIUM -> 32.responsiveWidth()
                WindowWidthSizeClass.EXPANDED -> 40.responsiveWidth()
            })
        )

        Spacer(modifier = Modifier.height(16.responsiveHeight()))

        LazyRow(
            contentPadding = PaddingValues(horizontal = when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.COMPACT -> 21.responsiveWidth()
                WindowWidthSizeClass.MEDIUM -> 30.responsiveWidth()
                WindowWidthSizeClass.EXPANDED -> 40.responsiveWidth()
            }),
            horizontalArrangement = Arrangement.spacedBy(when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.COMPACT -> 10.responsiveWidth()
                WindowWidthSizeClass.MEDIUM -> 15.responsiveWidth()
                WindowWidthSizeClass.EXPANDED -> 20.responsiveWidth()
            })
        ) {
            items(places) { place ->
                FrequentlyVisitedCard(
                    place = place,
                    onClick = { /* Handle click */ },
                    onFavoriteClick = { /* Handle favorite click */ }
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.responsiveHeight()))
}

/**
 * Recommended places section
 */
@Composable
fun RecommendedPlacesSection(
    places: List<com.example.hotel.data.model.Place>
) {
    // Get window size class for responsive layout
    val windowSizeClass = LocalWindowSizeClass.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.responsiveHeight())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.COMPACT -> 21.responsiveWidth()
                    WindowWidthSizeClass.MEDIUM -> 30.responsiveWidth()
                    WindowWidthSizeClass.EXPANDED -> 40.responsiveWidth()
                }),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.recommended_places),
                fontWeight = FontWeight.Normal,
                fontSize = when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.COMPACT -> 14.responsiveTextSize()
                    WindowWidthSizeClass.MEDIUM -> 16.responsiveTextSize()
                    WindowWidthSizeClass.EXPANDED -> 18.responsiveTextSize()
                },
                lineHeight = when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.COMPACT -> 18.responsiveTextSize()
                    WindowWidthSizeClass.MEDIUM -> 20.responsiveTextSize()
                    WindowWidthSizeClass.EXPANDED -> 22.responsiveTextSize()
                },
                letterSpacing = (-0.165).sp,
                color = Color.Black
            )

            Text(
                text = stringResource(R.string.explore_all),
                fontWeight = FontWeight.Normal,
                fontSize = when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.COMPACT -> 10.responsiveTextSize()
                    WindowWidthSizeClass.MEDIUM -> 12.responsiveTextSize()
                    WindowWidthSizeClass.EXPANDED -> 14.responsiveTextSize()
                },
                lineHeight = when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.COMPACT -> 13.responsiveTextSize()
                    WindowWidthSizeClass.MEDIUM -> 15.responsiveTextSize()
                    WindowWidthSizeClass.EXPANDED -> 17.responsiveTextSize()
                },
                letterSpacing = (-0.165).sp,
                color = Blue
            )
        }

        Spacer(modifier = Modifier.height(16.responsiveHeight()))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.COMPACT -> 17.responsiveWidth()
                    WindowWidthSizeClass.MEDIUM -> 25.responsiveWidth()
                    WindowWidthSizeClass.EXPANDED -> 35.responsiveWidth()
                }),
            verticalArrangement = Arrangement.spacedBy(when (windowSizeClass.heightSizeClass) {
                WindowHeightSizeClass.COMPACT -> 8.responsiveHeight()
                WindowHeightSizeClass.MEDIUM -> 11.responsiveHeight()
                WindowHeightSizeClass.EXPANDED -> 14.responsiveHeight()
            })
        ) {
            places.forEach { place ->
                RecommendedPlaceCard(
                    place = place,
                    onClick = { /* Handle click */ },
                    onFavoriteClick = { /* Handle favorite click */ }
                )
            }
        }
    }
}
