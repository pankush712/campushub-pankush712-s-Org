package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = CampusIndigoLight,
    onPrimary = Color.White,
    primaryContainer = CampusIndigoDark,
    onPrimaryContainer = Color(0xFFE0E7FF),
    secondary = CampusTealLight,
    onSecondary = Color(0xFF003831),
    secondaryContainer = CampusTealSecondary,
    onSecondaryContainer = Color(0xFFCCFBF1),
    tertiary = CampusVioletLight,
    onTertiary = Color.White,
    background = DarkNavyBackground,
    onBackground = TextLightHigh,
    surface = DarkNavySurface,
    onSurface = TextLightHigh,
    surfaceVariant = DarkNavySurfaceVariant,
    onSurfaceVariant = TextLightMedium,
    outline = DarkNavyBorder
)

private val LightColorScheme = lightColorScheme(
    primary = CampusIndigoPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0E7FF),
    onPrimaryContainer = CampusIndigoDark,
    secondary = CampusTealSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCCFBF1),
    onSecondaryContainer = Color(0xFF115E59),
    tertiary = CampusVioletTertiary,
    onTertiary = Color.White,
    background = LightCanvasBackground,
    onBackground = TextDarkHigh,
    surface = LightCanvasSurface,
    onSurface = TextDarkHigh,
    surfaceVariant = LightCanvasSurfaceVariant,
    onSurfaceVariant = TextDarkMedium,
    outline = LightCanvasBorder
)

@Composable
fun CampusHubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set false to ensure branded CampusHub palette
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
