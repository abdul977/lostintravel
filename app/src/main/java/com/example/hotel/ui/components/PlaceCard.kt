package com.example.hotel.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import coil.compose.AsyncImage
import com.example.hotel.R
import com.example.hotel.data.model.Place
import com.example.hotel.ui.theme.Blue
import com.example.hotel.ui.theme.GoogleSansDisplay
import com.example.hotel.ui.util.LocalWindowSizeClass
import com.example.hotel.ui.util.WindowWidthSizeClass
import com.example.hotel.ui.util.WindowHeightSizeClass
import com.example.hotel.ui.util.ResponsiveDimensions.responsiveWidth
import com.example.hotel.ui.util.ResponsiveDimensions.responsiveHeight
import com.example.hotel.ui.util.ResponsiveDimensions.responsiveTextSize

/**
 * Card component for displaying a place in the frequently visited section
 */
@Composable
fun FrequentlyVisitedCard(
    place: Place,
    onClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {}
) {
    // Get window size class for responsive layout
    val windowSizeClass = LocalWindowSizeClass.current
    Card(
        modifier = Modifier
            .width(when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.COMPACT -> 155.responsiveWidth()
                WindowWidthSizeClass.MEDIUM -> 180.responsiveWidth()
                WindowWidthSizeClass.EXPANDED -> 220.responsiveWidth()
            })
            .height(when (windowSizeClass.heightSizeClass) {
                WindowHeightSizeClass.COMPACT -> 160.responsiveHeight()
                WindowHeightSizeClass.MEDIUM -> 176.responsiveHeight()
                WindowHeightSizeClass.EXPANDED -> 200.responsiveHeight()
            })
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.responsiveWidth()),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Place image
            AsyncImage(
                model = place.imageUrl,
                contentDescription = place.leadingDestinationTitle,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(when (windowSizeClass.heightSizeClass) {
                        WindowHeightSizeClass.COMPACT -> 110.responsiveHeight()
                        WindowHeightSizeClass.MEDIUM -> 122.responsiveHeight()
                        WindowHeightSizeClass.EXPANDED -> 140.responsiveHeight()
                    })
                    .clip(RoundedCornerShape(topStart = 10.responsiveWidth(), topEnd = 10.responsiveWidth()))
            )

            // Favorite button
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.responsiveWidth())
                    .size(when (windowSizeClass.widthSizeClass) {
                        WindowWidthSizeClass.COMPACT -> 20.responsiveWidth()
                        WindowWidthSizeClass.MEDIUM -> 24.responsiveWidth()
                        WindowWidthSizeClass.EXPANDED -> 28.responsiveWidth()
                    })
            ) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Add to favorites",
                    tint = Color.Red
                )
            }

            // Place details
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(
                        horizontal = 8.responsiveWidth(),
                        vertical = 8.responsiveHeight()
                    ),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Place name and location
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = place.leadingDestinationTitle,
                        fontFamily = GoogleSansDisplay,
                        fontWeight = FontWeight.Normal,
                        fontSize = when (windowSizeClass.widthSizeClass) {
                            WindowWidthSizeClass.COMPACT -> 12.responsiveTextSize()
                            WindowWidthSizeClass.MEDIUM -> 14.responsiveTextSize()
                            WindowWidthSizeClass.EXPANDED -> 16.responsiveTextSize()
                        },
                        letterSpacing = (-0.165).sp,
                        color = Color.Black
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_location),
                            contentDescription = null,
                            modifier = Modifier.size(when (windowSizeClass.widthSizeClass) {
                                WindowWidthSizeClass.COMPACT -> 12.responsiveWidth()
                                WindowWidthSizeClass.MEDIUM -> 14.responsiveWidth()
                                WindowWidthSizeClass.EXPANDED -> 16.responsiveWidth()
                            }),
                            tint = Color.Red
                        )

                        Text(
                            text = place.subDestinationTitle,
                            fontFamily = GoogleSansDisplay,
                            fontWeight = FontWeight.Medium,
                            fontSize = when (windowSizeClass.widthSizeClass) {
                                WindowWidthSizeClass.COMPACT -> 10.responsiveTextSize()
                                WindowWidthSizeClass.MEDIUM -> 12.responsiveTextSize()
                                WindowWidthSizeClass.EXPANDED -> 14.responsiveTextSize()
                            },
                            letterSpacing = (-0.165).sp,
                            color = Color(0xFFC0BDBD)
                        )
                    }
                }

                // Price
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "$${"%.0f".format(place.price)}",
                        fontFamily = GoogleSansDisplay,
                        fontWeight = FontWeight.Normal,
                        fontSize = when (windowSizeClass.widthSizeClass) {
                            WindowWidthSizeClass.COMPACT -> 12.responsiveTextSize()
                            WindowWidthSizeClass.MEDIUM -> 14.responsiveTextSize()
                            WindowWidthSizeClass.EXPANDED -> 16.responsiveTextSize()
                        },
                        letterSpacing = (-0.165).sp,
                        color = Blue
                    )

                    Text(
                        text = stringResource(R.string.per_person),
                        fontFamily = GoogleSansDisplay,
                        fontWeight = FontWeight.Normal,
                        fontSize = when (windowSizeClass.widthSizeClass) {
                            WindowWidthSizeClass.COMPACT -> 10.responsiveTextSize()
                            WindowWidthSizeClass.MEDIUM -> 12.responsiveTextSize()
                            WindowWidthSizeClass.EXPANDED -> 14.responsiveTextSize()
                        },
                        letterSpacing = (-0.165).sp,
                        color = Color(0xFFC0BDBD)
                    )
                }
            }
        }
    }
}

/**
 * Card component for displaying a place in the recommended places section
 */
@Composable
fun RecommendedPlaceCard(
    place: Place,
    onClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {}
) {
    // Get window size class for responsive layout
    val windowSizeClass = LocalWindowSizeClass.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(when (windowSizeClass.heightSizeClass) {
                WindowHeightSizeClass.COMPACT -> 75.responsiveHeight()
                WindowHeightSizeClass.MEDIUM -> 83.responsiveHeight()
                WindowHeightSizeClass.EXPANDED -> 95.responsiveHeight()
            })
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(5.responsiveWidth()),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = when (windowSizeClass.widthSizeClass) {
                        WindowWidthSizeClass.COMPACT -> 15.responsiveWidth()
                        WindowWidthSizeClass.MEDIUM -> 20.responsiveWidth()
                        WindowWidthSizeClass.EXPANDED -> 25.responsiveWidth()
                    },
                    vertical = when (windowSizeClass.heightSizeClass) {
                        WindowHeightSizeClass.COMPACT -> 10.responsiveHeight()
                        WindowHeightSizeClass.MEDIUM -> 13.responsiveHeight()
                        WindowHeightSizeClass.EXPANDED -> 16.responsiveHeight()
                    }
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Place image
            AsyncImage(
                model = place.imageUrl,
                contentDescription = place.leadingDestinationTitle,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(when (windowSizeClass.widthSizeClass) {
                        WindowWidthSizeClass.COMPACT -> 57.responsiveWidth()
                        WindowWidthSizeClass.MEDIUM -> 65.responsiveWidth()
                        WindowWidthSizeClass.EXPANDED -> 75.responsiveWidth()
                    })
                    .clip(RoundedCornerShape(5.responsiveWidth()))
            )

            Spacer(modifier = Modifier.width(16.responsiveWidth()))

            // Place details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = place.leadingDestinationTitle,
                    fontFamily = GoogleSansDisplay,
                    fontWeight = FontWeight.Normal,
                    fontSize = when (windowSizeClass.widthSizeClass) {
                        WindowWidthSizeClass.COMPACT -> 12.responsiveTextSize()
                        WindowWidthSizeClass.MEDIUM -> 14.responsiveTextSize()
                        WindowWidthSizeClass.EXPANDED -> 16.responsiveTextSize()
                    },
                    letterSpacing = (-0.165).sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(13.responsiveHeight()))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_location),
                        contentDescription = null,
                        modifier = Modifier.size(when (windowSizeClass.widthSizeClass) {
                            WindowWidthSizeClass.COMPACT -> 12.responsiveWidth()
                            WindowWidthSizeClass.MEDIUM -> 14.responsiveWidth()
                            WindowWidthSizeClass.EXPANDED -> 16.responsiveWidth()
                        }),
                        tint = Color.Red
                    )

                    Text(
                        text = place.subDestinationTitle,
                        fontFamily = GoogleSansDisplay,
                        fontWeight = FontWeight.Medium,
                        fontSize = when (windowSizeClass.widthSizeClass) {
                            WindowWidthSizeClass.COMPACT -> 10.responsiveTextSize()
                            WindowWidthSizeClass.MEDIUM -> 12.responsiveTextSize()
                            WindowWidthSizeClass.EXPANDED -> 14.responsiveTextSize()
                        },
                        letterSpacing = (-0.165).sp,
                        color = Color(0xFFC0BDBD)
                    )
                }
            }

            // Favorite button and price
            Column(
                horizontalAlignment = Alignment.End
            ) {
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier.size(when (windowSizeClass.widthSizeClass) {
                        WindowWidthSizeClass.COMPACT -> 16.responsiveWidth()
                        WindowWidthSizeClass.MEDIUM -> 20.responsiveWidth()
                        WindowWidthSizeClass.EXPANDED -> 24.responsiveWidth()
                    })
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Add to favorites",
                        tint = Color(0x5B000000)
                    )
                }

                Spacer(modifier = Modifier.height(12.responsiveHeight()))

                Text(
                    text = "$${"%.0f".format(place.price)}" + stringResource(R.string.per_person),
                    fontFamily = GoogleSansDisplay,
                    fontWeight = FontWeight.Medium,
                    fontSize = when (windowSizeClass.widthSizeClass) {
                        WindowWidthSizeClass.COMPACT -> 10.responsiveTextSize()
                        WindowWidthSizeClass.MEDIUM -> 12.responsiveTextSize()
                        WindowWidthSizeClass.EXPANDED -> 14.responsiveTextSize()
                    },
                    letterSpacing = (-0.165).sp,
                    color = Blue,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}
