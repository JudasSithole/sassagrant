package com.sassa.scamshield.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val ScamShieldDarkColorScheme = darkColorScheme(
    primary = Purple500,
    onPrimary = Color.White,
    secondary = Blue500,
    onSecondary = Color.White,
    tertiary = Amber,
    onTertiary = Color.Black,
    background = DarkCharcoal,
    onBackground = TextPrimary,
    surface = CardBackground,
    onSurface = TextPrimary,
    surfaceVariant = CardBackgroundSecondary,
    onSurfaceVariant = TextSecondary,
    error = RedError,
    onError = Color.White,
    outline = BorderColor,
    inverseOnSurface = Color.Black
)

@Composable
fun ScamShieldTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else ScamShieldDarkColorScheme
        }
        darkTheme -> ScamShieldDarkColorScheme
        else -> ScamShieldDarkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

fun ColorScheme.gradientPrimary(): androidx.compose.ui.graphics.Brush {
    return androidx.compose.ui.graphics.Brush.horizontalGradient(
        colors = listOf(GradientStart, GradientEnd)
    )
}
