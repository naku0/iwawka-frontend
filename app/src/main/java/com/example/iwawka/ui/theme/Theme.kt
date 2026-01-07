package com.example.iwawka.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import kotlin.math.pow

private fun Color.isLight(): Boolean {
    // относительная яркость (приближенно, но нормально для выбора onPrimary)
    fun c(x: Float) = if (x <= 0.04045f) x / 12.92f else ((x + 0.055f) / 1.055f).pow(2.4f)
    val r = c(red); val g = c(green); val b = c(blue)
    val luminance = 0.2126f * r + 0.7152f * g + 0.0722f * b
    return luminance > 0.5f
}

@Composable
fun IwawkaTheme(
    darkTheme: Boolean,
    accent: Color,
    content: @Composable () -> Unit
) {
    val onAccent = if (accent.isLight()) Color.Black else Color.White

    val light = lightColorScheme(
        primary = accent,
        onPrimary = onAccent,

        background = Color.White,
        onBackground = Color.Black,

        surface = Color.White,
        onSurface = Color.Black,

        outline = Color.Black.copy(alpha = 0.10f),
    )

    val dark = darkColorScheme(
        primary = accent,
        onPrimary = onAccent,

        background = Color.Black,
        onBackground = Color.White,

        surface = Color.Black,
        onSurface = Color.White,

        outline = Color.White.copy(alpha = 0.10f),
    )

    MaterialTheme(
        colorScheme = if (darkTheme) dark else light,
        typography = Typography,
        content = content
    )
}
