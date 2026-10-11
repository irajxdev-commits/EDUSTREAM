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
    primary = MintAccent,
    onPrimary = Color(0xFF06201B),
    primaryContainer = Color(0xFF153F38),
    onPrimaryContainer = Color(0xFFD5F3E8),
    secondary = DarkOrange,
    onSecondary = Color(0xFF3C1400),
    secondaryContainer = Color(0xFF4A2810),
    onSecondaryContainer = Color(0xFFFFDBC8),
    tertiary = AccentYellow,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onBackground = DarkInkPrimary,
    onSurface = DarkInkPrimary,
    onSurfaceVariant = DarkInkMuted,
    outline = DarkLineBorder
)

private val LightColorScheme = lightColorScheme(
    primary = ForestGreen,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD4E8E1),
    onPrimaryContainer = Color(0xFF092923),
    secondary = AmberOrange,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE0D0),
    onSecondaryContainer = Color(0xFF381400),
    tertiary = AccentYellow,
    background = CreamBackground,
    surface = CreamSurface,
    surfaceVariant = CreamSurfaceVariant,
    onBackground = InkPrimary,
    onSurface = InkPrimary,
    onSurfaceVariant = InkMuted,
    outline = LineBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set false to preserve our bespoke Padho Bhai brand identity
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
