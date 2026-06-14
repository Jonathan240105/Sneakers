package com.example.snkrsapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.snkrsapp.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val miTipografia = FontFamily(
    Font(R.font.robotoslab_regular, FontWeight.Normal),
    Font(R.font.robotoslab_bold, FontWeight.Bold),
    Font(R.font.robotoslab_medium, FontWeight.Medium),
    Font(R.font.robotoslab_extrabold, FontWeight.ExtraBold)
)
val otraTipografia = FontFamily(
    Font(R.font.jost_bold, FontWeight.Bold),
    Font(R.font.jost_extrabold, FontWeight.ExtraBold),
    Font(R.font.jost_medium, FontWeight.Medium),
    Font(R.font.jost_regular, FontWeight.Normal)
)