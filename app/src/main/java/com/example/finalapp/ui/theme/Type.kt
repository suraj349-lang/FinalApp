package com.example.finalapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.finalapp.R

// good for body small
val Dongle= FontFamily(
    Font(R.font.dongle_regular),
    Font(R.font.dongle_bold),
    Font(R.font.dongle_light)
)
val Estonia= FontFamily(
    Font(R.font.estonia_regular)
)
val Gluten= FontFamily(
    Font(R.font.gluten_regular)
)
val Joti_One= FontFamily(
    Font(R.font.jotioneregular)
)
val Marhem= FontFamily(
    Font(R.font.marhey_regular)
)
val Oregano= FontFamily(
    Font(R.font.oreganoregular),
    Font(R.font.oreganoitalic)
)
val OfferFont= FontFamily(
    Font(R.font.audiowide_regular)
)


// Set of Material typography styles to start with
val Typography = Typography(
    //appbar title
    titleLarge = TextStyle(
        color= Color(0xFF08155E),
        fontFamily = Oregano,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        lineHeight = 10.sp,
        letterSpacing = 2.sp
    ),
    titleMedium = TextStyle(
        color= Color(0xFF08155E),
        fontFamily = Oregano,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Dongle,
        fontWeight = FontWeight.Medium,
        fontSize = 40.sp,
        lineHeight = 2.sp,
        letterSpacing = 0.5.sp

    ),
    bodyMedium = TextStyle(
        fontFamily = OfferFont,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 2.sp,
        letterSpacing = 0.5.sp

    ),
    bodySmall = TextStyle(
        fontFamily = Dongle,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 2.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )

)