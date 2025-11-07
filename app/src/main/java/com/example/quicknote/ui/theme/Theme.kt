package com.example.quicknote.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = NoteColors(
    backgroundColor = BackgroundDark,
    noteBackground = NoteBackgroundDark,
    textPrimary = TextPrimaryDark,
    backgroundBrand = BackgroundBrand,
    textSecondary = TextSecondary,
    textLight = TextLight,
    selectionColor = SelectionColor,
    backgroundSecondary = BackgroundSecondaryDark,
)

private val LightColorScheme = NoteColors(
    backgroundColor = Background,
    noteBackground = NoteBackground,
    textPrimary = TextPrimary,
    backgroundBrand = BackgroundBrand,
    textSecondary = TextSecondary,
    textLight = TextLight,
    selectionColor = SelectionColor,
    backgroundSecondary = BackgroundSecondary,
)

@Composable
fun QuickNoteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    ProvideNoteColors(colors) {
        MaterialTheme(
            colorScheme = debugColors(),
            typography = Typography,
            content = content
        )
    }
}

object NoteTheme {
    val colors: NoteColors
        @Composable
        get() = LocalNoteColors.current
}

@Immutable
data class NoteColors(
    val backgroundColor: Color,
    val noteBackground: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val backgroundBrand: Color,
    val textLight: Color,
    val selectionColor: Color,
    val backgroundSecondary: Color,
)

private val LocalNoteColors = staticCompositionLocalOf<NoteColors> {
    error("No ColorPalette provided")
}

@Composable
fun ProvideNoteColors(colors: NoteColors, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalNoteColors provides colors, content = content)
}

fun debugColors(debugColor: Color = Color.Magenta) = ColorScheme(
    primary = debugColor,
    onPrimary = debugColor,
    primaryContainer = debugColor,
    onPrimaryContainer = debugColor,
    inversePrimary = debugColor,
    secondary = debugColor,
    onSecondary = debugColor,
    secondaryContainer = debugColor,
    onSecondaryContainer = debugColor,
    tertiary = debugColor,
    onTertiary = debugColor,
    tertiaryContainer = debugColor,
    onTertiaryContainer = debugColor,
    background = debugColor,
    onBackground = debugColor,
    surface = debugColor,
    onSurface = debugColor,
    surfaceVariant = debugColor,
    onSurfaceVariant = debugColor,
    surfaceTint = debugColor,
    inverseSurface = debugColor,
    inverseOnSurface = debugColor,
    error = debugColor,
    onError = debugColor,
    errorContainer = debugColor,
    onErrorContainer = debugColor,
    outline = debugColor,
    outlineVariant = debugColor,
    scrim = debugColor,
    surfaceBright = debugColor,
    surfaceDim = debugColor,
    surfaceContainer = debugColor,
    surfaceContainerHigh = debugColor,
    surfaceContainerHighest = debugColor,
    surfaceContainerLow = debugColor,
    surfaceContainerLowest = debugColor,
)