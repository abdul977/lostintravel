package com.example.hotel.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hotel.R
import com.example.hotel.ui.theme.Blue
import com.example.hotel.ui.theme.GoogleSansDisplay
import com.example.hotel.ui.util.LocalWindowSizeClass
import com.example.hotel.ui.util.WindowWidthSizeClass
import com.example.hotel.ui.util.WindowHeightSizeClass
import com.example.hotel.ui.util.ResponsiveDimensions.responsiveWidth
import com.example.hotel.ui.util.ResponsiveDimensions.responsiveHeight
import com.example.hotel.ui.util.ResponsiveDimensions.responsiveTextSize

/**
 * Bottom navigation bar for the app
 */
@Composable
fun BottomNavigationBar(
    currentRoute: String = "home"
) {
    // Get window size class for responsive layout
    val windowSizeClass = LocalWindowSizeClass.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(when (windowSizeClass.heightSizeClass) {
                WindowHeightSizeClass.COMPACT -> 70.responsiveHeight()
                WindowHeightSizeClass.MEDIUM -> 82.responsiveHeight()
                WindowHeightSizeClass.EXPANDED -> 90.responsiveHeight()
            })
            .background(Color.White)
            .padding(
                horizontal = when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.COMPACT -> 22.responsiveWidth()
                    WindowWidthSizeClass.MEDIUM -> 30.responsiveWidth()
                    WindowWidthSizeClass.EXPANDED -> 40.responsiveWidth()
                },
                vertical = when (windowSizeClass.heightSizeClass) {
                    WindowHeightSizeClass.COMPACT -> 14.responsiveHeight()
                    WindowHeightSizeClass.MEDIUM -> 18.responsiveHeight()
                    WindowHeightSizeClass.EXPANDED -> 22.responsiveHeight()
                }
            ),
        horizontalArrangement = Arrangement.spacedBy(when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.COMPACT -> 28.responsiveWidth()
            WindowWidthSizeClass.MEDIUM -> 40.responsiveWidth()
            WindowWidthSizeClass.EXPANDED -> 60.responsiveWidth()
        }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Home
        BottomNavItem(
            icon = R.drawable.ic_home,
            label = stringResource(R.string.home),
            isSelected = currentRoute == "home",
            onClick = { /* Navigate to home */ }
        )

        // Explore
        BottomNavItem(
            icon = R.drawable.ic_explore,
            label = stringResource(R.string.explore),
            isSelected = currentRoute == "explore",
            onClick = { /* Navigate to explore */ }
        )

        // Plan
        BottomNavItem(
            icon = R.drawable.ic_plan,
            label = stringResource(R.string.plan),
            isSelected = currentRoute == "plan",
            onClick = { /* Navigate to plan */ }
        )

        // History
        BottomNavItem(
            icon = R.drawable.ic_history,
            label = stringResource(R.string.history),
            isSelected = currentRoute == "history",
            onClick = { /* Navigate to history */ }
        )

        // Profile
        BottomNavItem(
            icon = R.drawable.ic_profile,
            label = stringResource(R.string.profile),
            isSelected = currentRoute == "profile",
            onClick = { /* Navigate to profile */ }
        )
    }
}

/**
 * Individual item in the bottom navigation bar
 */
@Composable
fun BottomNavItem(
    icon: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Get window size class for responsive layout
    val windowSizeClass = LocalWindowSizeClass.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.responsiveHeight()),
        modifier = Modifier.width(when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.COMPACT -> 47.responsiveWidth()
            WindowWidthSizeClass.MEDIUM -> 60.responsiveWidth()
            WindowWidthSizeClass.EXPANDED -> 80.responsiveWidth()
        })
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            tint = if (isSelected) Blue else Color(0xBF303030),
            modifier = Modifier.size(when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.COMPACT -> 14.responsiveWidth()
                WindowWidthSizeClass.MEDIUM -> 18.responsiveWidth()
                WindowWidthSizeClass.EXPANDED -> 24.responsiveWidth()
            })
        )

        Text(
            text = label,
            fontFamily = GoogleSansDisplay,
            fontWeight = FontWeight.Normal,
            fontSize = when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.COMPACT -> 10.responsiveTextSize()
                WindowWidthSizeClass.MEDIUM -> 12.responsiveTextSize()
                WindowWidthSizeClass.EXPANDED -> 14.responsiveTextSize()
            },
            lineHeight = when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.COMPACT -> 12.responsiveTextSize()
                WindowWidthSizeClass.MEDIUM -> 14.responsiveTextSize()
                WindowWidthSizeClass.EXPANDED -> 16.responsiveTextSize()
            },
            letterSpacing = 0.16.sp,
            textAlign = TextAlign.Center,
            color = if (isSelected) Blue else Color(0xBF303030)
        )
    }
}
