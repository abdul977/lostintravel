package com.example.hotel.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.hotel.R

val GoogleSansDisplay = FontFamily(
    Font(R.font.google_sans_display_regular, FontWeight.Normal),
    Font(R.font.google_sans_display_medium, FontWeight.Medium),
    Font(R.font.google_sans_display_bold, FontWeight.Bold)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = GoogleSansDisplay,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = GoogleSansDisplay,
        fontWeight = FontWeight.Normal,
        fontSize = 30.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.165).sp
    ),
    labelMedium = TextStyle(
        fontFamily = GoogleSansDisplay,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.165).sp
    ),
    bodyMedium = TextStyle(
        fontFamily = GoogleSansDisplay,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.165).sp
    ),
    bodySmall = TextStyle(
        fontFamily = GoogleSansDisplay,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.165).sp
    )
)
