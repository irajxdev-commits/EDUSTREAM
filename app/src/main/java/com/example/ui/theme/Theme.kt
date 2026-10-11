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
    primary = MintGlow,
    onPrimary = Color(0xFF03281E),
    primaryContainer = Color(0xFF064E3B),
    onPrimaryContainer = Color(0xFFA7F3D0),
    secondary = SolarAmber,
    onSecondary = Color(0xFF451A03),
    secondaryContainer = Color(0xFF78350F),
    onSecondaryContainer = Color(0xFFFEF3C7),
    tertiary = SkyCyan,
    background = DarkCanvas,
    surface = DarkCardSurface,
    surfaceVariant = DarkCardElevated,
    onBackground = DarkInkPrimary,
    onSurface = DarkInkPrimary,
    onSurfaceVariant = DarkInkSecondary,
    outline = DarkBorderSubtle,
    outlineVariant = DarkBorderFocus
)

private val LightColorScheme = lightColorScheme(
    primary = EmeraldDark,
    onPrimary = Color.White,
    primaryContainer = MintSoft,
    onPrimaryContainer = Color(0xFF064E3B),
    secondary = TangerineAccent,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFEDD5),
    onSecondaryContainer = Color(0xFF7C2D12),
    tertiary = ElectricIndigo,
    background = LightCanvas,
    surface = LightCardSurface,
    surfaceVariant = LightCardElevated,
    onBackground = LightInkPrimary,
    onSurface = LightInkPrimary,
    onSurfaceVariant = LightInkSecondary,
    outline = LightBorderSubtle,
    outlineVariant = LightBorderFocus
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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
