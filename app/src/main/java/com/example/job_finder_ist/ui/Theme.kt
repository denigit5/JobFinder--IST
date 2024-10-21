package com.example.job_finder_ist.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Define light theme colors
private val LightColors = lightColors(
    primary = Color(0xFF6200EE),
    primaryVariant = Color(0xFF3700B3),
    onPrimary = Color.White,
    secondary = Color(0xFF03DAC5),
    secondaryVariant = Color(0xFF018786),
    onSecondary = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)

// Define dark theme colors
private val DarkColors = darkColors(
    primary = Color(0xFFBB86FC),
    primaryVariant = Color(0xFF3700B3),
    onPrimary = Color.Black,
    secondary = Color(0xFF03DAC5),
    secondaryVariant = Color(0xFF03DAC5),
    onSecondary = Color.Black,
    surface = Color.Black,
    onSurface = Color.White
)

// Your custom theme function using Material (not Material3)
@Composable
fun JobFinderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Detect system theme
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    // Apply the selected color scheme, typography, and shapes using Material Theme
    MaterialTheme(
        colors = colors,
        typography = Typography,  // Define Typography in Typography.kt or use default
        shapes = Shapes,  // Define Shapes in Shapes.kt or use default
        content = content
    )
}
