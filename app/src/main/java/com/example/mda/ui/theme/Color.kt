
package com.example.mda.ui.theme

// Theme configuration

import android.R.style.Theme
import android.content.res.Resources
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
val AppVerticalGradient = Brush.verticalGradient(
    colorStops = arrayOf(
        0.0f to Color(0xFF0C2B4E),
        0.35f to Color(0xFF1A3D64),
        1.0f to Color(0xFF1D546C)
    )
)

val DarkBackground = Color(0xFF0D0F1C)
val DarkSurface = Color(0xFF1A1C2A)
val DarkSurfaceVariant = Color(0xFF1A2233)
val DarkContainer = Color(0xFF101528)
val DarkCardBackground = Color(0xFF1E1E3A)
val DarkInfoCard = Color(0xFF2C2C4A)
val DarkMovieCard = Color(0xFF1E1E2E)

val PrimaryBlue = Color(0xFF60D2FF)
val AccentCyan = Color(0xFF53DEED)
val RatingYellow = Color(0xFFFFC107)

val TextPrimaryDark = Color(0xFFFFFFFF)
val TextSecondaryDark = Color(0xFFC5D1D9)
val TextAccentDark = Color(0xFFAAAAFF)
val TextSubtleDark = Color(0xFFB0B0C0)
val TextDimDark = Color(0xFFE0E0E0)

val IconGrayDark = Color(0xFF757575)

val LightBackground = Color(0xFFF6F7FB)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFF5F5F5)
val LightContainer = Color(0xFFEAEFF3)
val LightCardBackground = Color(0xFFF8F8FA)
val LightInfoCard = Color(0xFFE8EAF6)
val LightMovieCard = Color(0xFFF5F5F5)

val PrimaryLightBlue = Color(0xFF1976D2)
val AccentLightCyan = Color(0xFF26C6DA)
val RatingYellowLight = Color(0xFFFFC107)

val TextPrimaryLight = Color(0xFF212121)
val TextSecondaryLight = Color(0xFF757575)
val TextAccentLight = Color(0xFF5E35B1)
val TextSubtleLight = Color(0xFF616161)
val TextDimLight = Color(0xFF424242)

val IconGrayLight = Color(0xFF9E9E9E)

@Composable
fun AppBackgroundGradient(darkTheme: Boolean = isSystemInDarkTheme()): Brush {
    return if (darkTheme) {
        Brush.verticalGradient(
            colorStops = arrayOf(
                0.0f to Color(0xFF0C2B4E),
                0.35f to Color(0xFF1A3D64),
                1.0f to Color(0xFF1D546C)
            )
        )
    } else {
        Brush.verticalGradient(
            colorStops = arrayOf(
                0.0f to Color(0xFFEAEFEF),
                0.35f to Color(0xFFB8CFCE),
                1.0f to Color(0xFF2973B2)
            )
        )
    }
}

@Composable
fun AppTopBarColors(darkTheme: Boolean = isSystemInDarkTheme()): Pair<Color, Color> {
    return if (darkTheme) {
        Color(0xFF0C2B4E) to Color(0xFFFFFFFF)
    } else {
        Color(0xFFEAEFEF) to Color(0xFF212121)
    }
}