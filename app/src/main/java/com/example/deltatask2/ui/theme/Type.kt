package com.example.deltatask2.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.deltatask2.R

private val bangers = Font(R.font.bangers)

private val bangersFontFamily = FontFamily(bangers)

val Typography = Typography(
    defaultFontFamily = bangersFontFamily,
    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = LightGrey
    )
)
