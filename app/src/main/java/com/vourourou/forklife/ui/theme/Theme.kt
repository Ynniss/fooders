package com.vourourou.forklife.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Dark mode preference enum
enum class DarkModePreference {
    System,
    Light,
    Dark
}

// Theme name enum
enum class ForkLifeColorTheme {
    Dynamic,
    Orange,
    Avocado,
    Cherry
}

// Orange Light Color Scheme
private val OrangeLightColorScheme = lightColorScheme(
    primary = OrangePrimary,
    onPrimary = OrangeOnPrimary,
    primaryContainer = OrangePrimaryContainer,
    onPrimaryContainer = OrangeOnPrimaryContainer,
    secondary = OrangeSecondary,
    onSecondary = OrangeOnSecondary,
    secondaryContainer = OrangeSecondaryContainer,
    onSecondaryContainer = OrangeOnSecondaryContainer,
    tertiary = OrangeTertiary,
    onTertiary = OrangeOnTertiary,
    tertiaryContainer = OrangeTertiaryContainer,
    onTertiaryContainer = OrangeOnTertiaryContainer,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,
    inverseSurface = InverseSurfaceLight,
    inverseOnSurface = InverseOnSurfaceLight,
    inversePrimary = OrangeInversePrimary,
    scrim = ScrimLight,
    surfaceTint = OrangeSurfaceTint,
    surfaceContainerLowest = SurfaceContainerLowestLight,
    surfaceContainerLow = SurfaceContainerLowLight,
    surfaceContainer = SurfaceContainerLight,
    surfaceContainerHigh = SurfaceContainerHighLight,
    surfaceContainerHighest = SurfaceContainerHighestLight
)

// Orange Dark Color Scheme
private val OrangeDarkColorScheme = darkColorScheme(
    primary = OrangePrimaryDark,
    onPrimary = OrangeOnPrimaryDark,
    primaryContainer = OrangePrimaryContainerDark,
    onPrimaryContainer = OrangeOnPrimaryContainerDark,
    secondary = OrangeSecondaryDark,
    onSecondary = OrangeOnSecondaryDark,
    secondaryContainer = OrangeSecondaryContainerDark,
    onSecondaryContainer = OrangeOnSecondaryContainerDark,
    tertiary = OrangeTertiaryDark,
    onTertiary = OrangeOnTertiaryDark,
    tertiaryContainer = OrangeTertiaryContainerDark,
    onTertiaryContainer = OrangeOnTertiaryContainerDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,
    inverseSurface = InverseSurfaceDark,
    inverseOnSurface = InverseOnSurfaceDark,
    inversePrimary = OrangeInversePrimaryDark,
    scrim = ScrimDark,
    surfaceTint = OrangeSurfaceTintDark,
    surfaceContainerLowest = SurfaceContainerLowestDark,
    surfaceContainerLow = SurfaceContainerLowDark,
    surfaceContainer = SurfaceContainerDark,
    surfaceContainerHigh = SurfaceContainerHighDark,
    surfaceContainerHighest = SurfaceContainerHighestDark
)

// Avocado Light Color Scheme
private val AvocadoLightColorScheme = lightColorScheme(
    primary = AvocadoPrimary,
    onPrimary = AvocadoOnPrimary,
    primaryContainer = AvocadoPrimaryContainer,
    onPrimaryContainer = AvocadoOnPrimaryContainer,
    secondary = AvocadoSecondary,
    onSecondary = AvocadoOnSecondary,
    secondaryContainer = AvocadoSecondaryContainer,
    onSecondaryContainer = AvocadoOnSecondaryContainer,
    tertiary = AvocadoTertiary,
    onTertiary = AvocadoOnTertiary,
    tertiaryContainer = AvocadoTertiaryContainer,
    onTertiaryContainer = AvocadoOnTertiaryContainer,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,
    inverseSurface = InverseSurfaceLight,
    inverseOnSurface = InverseOnSurfaceLight,
    inversePrimary = AvocadoInversePrimary,
    scrim = ScrimLight,
    surfaceTint = AvocadoSurfaceTint,
    surfaceContainerLowest = SurfaceContainerLowestLight,
    surfaceContainerLow = SurfaceContainerLowLight,
    surfaceContainer = SurfaceContainerLight,
    surfaceContainerHigh = SurfaceContainerHighLight,
    surfaceContainerHighest = SurfaceContainerHighestLight
)

// Avocado Dark Color Scheme
private val AvocadoDarkColorScheme = darkColorScheme(
    primary = AvocadoPrimaryDark,
    onPrimary = AvocadoOnPrimaryDark,
    primaryContainer = AvocadoPrimaryContainerDark,
    onPrimaryContainer = AvocadoOnPrimaryContainerDark,
    secondary = AvocadoSecondaryDark,
    onSecondary = AvocadoOnSecondaryDark,
    secondaryContainer = AvocadoSecondaryContainerDark,
    onSecondaryContainer = AvocadoOnSecondaryContainerDark,
    tertiary = AvocadoTertiaryDark,
    onTertiary = AvocadoOnTertiaryDark,
    tertiaryContainer = AvocadoTertiaryContainerDark,
    onTertiaryContainer = AvocadoOnTertiaryContainerDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,
    inverseSurface = InverseSurfaceDark,
    inverseOnSurface = InverseOnSurfaceDark,
    inversePrimary = AvocadoInversePrimaryDark,
    scrim = ScrimDark,
    surfaceTint = AvocadoSurfaceTintDark,
    surfaceContainerLowest = SurfaceContainerLowestDark,
    surfaceContainerLow = SurfaceContainerLowDark,
    surfaceContainer = SurfaceContainerDark,
    surfaceContainerHigh = SurfaceContainerHighDark,
    surfaceContainerHighest = SurfaceContainerHighestDark
)

// Cherry Light Color Scheme
private val CherryLightColorScheme = lightColorScheme(
    primary = CherryPrimary,
    onPrimary = CherryOnPrimary,
    primaryContainer = CherryPrimaryContainer,
    onPrimaryContainer = CherryOnPrimaryContainer,
    secondary = CherrySecondary,
    onSecondary = CherryOnSecondary,
    secondaryContainer = CherrySecondaryContainer,
    onSecondaryContainer = CherryOnSecondaryContainer,
    tertiary = CherryTertiary,
    onTertiary = CherryOnTertiary,
    tertiaryContainer = CherryTertiaryContainer,
    onTertiaryContainer = CherryOnTertiaryContainer,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,
    inverseSurface = InverseSurfaceLight,
    inverseOnSurface = InverseOnSurfaceLight,
    inversePrimary = CherryInversePrimary,
    scrim = ScrimLight,
    surfaceTint = CherrySurfaceTint,
    surfaceContainerLowest = SurfaceContainerLowestLight,
    surfaceContainerLow = SurfaceContainerLowLight,
    surfaceContainer = SurfaceContainerLight,
    surfaceContainerHigh = SurfaceContainerHighLight,
    surfaceContainerHighest = SurfaceContainerHighestLight
)

// Cherry Dark Color Scheme
private val CherryDarkColorScheme = darkColorScheme(
    primary = CherryPrimaryDark,
    onPrimary = CherryOnPrimaryDark,
    primaryContainer = CherryPrimaryContainerDark,
    onPrimaryContainer = CherryOnPrimaryContainerDark,
    secondary = CherrySecondaryDark,
    onSecondary = CherryOnSecondaryDark,
    secondaryContainer = CherrySecondaryContainerDark,
    onSecondaryContainer = CherryOnSecondaryContainerDark,
    tertiary = CherryTertiaryDark,
    onTertiary = CherryOnTertiaryDark,
    tertiaryContainer = CherryTertiaryContainerDark,
    onTertiaryContainer = CherryOnTertiaryContainerDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,
    inverseSurface = InverseSurfaceDark,
    inverseOnSurface = InverseOnSurfaceDark,
    inversePrimary = CherryInversePrimaryDark,
    scrim = ScrimDark,
    surfaceTint = CherrySurfaceTintDark,
    surfaceContainerLowest = SurfaceContainerLowestDark,
    surfaceContainerLow = SurfaceContainerLowDark,
    surfaceContainer = SurfaceContainerDark,
    surfaceContainerHigh = SurfaceContainerHighDark,
    surfaceContainerHighest = SurfaceContainerHighestDark
)

@Composable
fun ForkLifeTheme(
    colorTheme: ForkLifeColorTheme = ForkLifeColorTheme.Orange,
    darkModePreference: DarkModePreference = DarkModePreference.System,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val isDarkTheme = when (darkModePreference) {
        DarkModePreference.System -> isSystemInDarkTheme()
        DarkModePreference.Light -> false
        DarkModePreference.Dark -> true
    }

    // Dynamic color scheme for Android 12+
    val dynamicColorScheme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (isDarkTheme) dynamicDarkColorScheme(context)
        else dynamicLightColorScheme(context)
    } else null

    val colorScheme: ColorScheme = when (colorTheme) {
        ForkLifeColorTheme.Dynamic -> dynamicColorScheme
            ?: if (isDarkTheme) OrangeDarkColorScheme else OrangeLightColorScheme
        ForkLifeColorTheme.Orange -> if (isDarkTheme) OrangeDarkColorScheme else OrangeLightColorScheme
        ForkLifeColorTheme.Avocado -> if (isDarkTheme) AvocadoDarkColorScheme else AvocadoLightColorScheme
        ForkLifeColorTheme.Cherry -> if (isDarkTheme) CherryDarkColorScheme else CherryLightColorScheme
    }

    // Update system bar icon colors based on theme (colors handled by enableEdgeToEdge)
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !isDarkTheme
                isAppearanceLightNavigationBars = !isDarkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ForkLifeTypography,
        shapes = ForkLifeShapes,
        content = content
    )
}

// Helper function to convert theme name string to enum
fun String.toForkLifeColorTheme(): ForkLifeColorTheme = when (this.lowercase()) {
    "dynamic" -> ForkLifeColorTheme.Dynamic
    "avocado" -> ForkLifeColorTheme.Avocado
    "cherry" -> ForkLifeColorTheme.Cherry
    else -> ForkLifeColorTheme.Orange
}

// Helper function to convert dark mode string to enum
fun String.toDarkModePreference(): DarkModePreference = when (this.lowercase()) {
    "light" -> DarkModePreference.Light
    "dark" -> DarkModePreference.Dark
    else -> DarkModePreference.System
}
