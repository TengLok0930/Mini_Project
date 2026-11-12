package my.edu.tarc.tarumtfoodorderingsystem.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = CampusBlue,
    onPrimary = CampusOnPrimary,
    primaryContainer = DarkCampusPrimaryContainer,
    onPrimaryContainer = DarkCampusOnPrimaryContainer,

    secondary = DarkCampusSecondary,
    onSecondary = DarkCampusOnSecondary,
    secondaryContainer = DarkCampusSecondaryContainer,
    onSecondaryContainer = DarkCampusOnSecondaryContainer,

    tertiary = DarkCampusTertiary,
    onTertiary = DarkCampusOnTertiary,
    tertiaryContainer = DarkCampusTertiaryContainer,
    onTertiaryContainer = DarkCampusOnTertiaryContainer,

    background = DarkCampusBackground,
    onBackground = DarkCampusOnBackground,
    surface = DarkCampusSurface,
    onSurface = DarkCampusOnSurface,
    surfaceVariant = DarkCampusSurfaceVariant,
    onSurfaceVariant = DarkCampusOnSurfaceVariant,

    outline = DarkCampusOutline,
    error = DarkCampusError,
    onError = DarkCampusOnError
)

private val LightColorScheme = lightColorScheme(
    primary = CampusBlue,
    onPrimary = CampusOnPrimary,
    primaryContainer = LightCampusPrimaryContainer,
    onPrimaryContainer = LightCampusOnPrimaryContainer,

    secondary = LightCampusSecondary,
    onSecondary = LightCampusOnSecondary,
    secondaryContainer = LightCampusSecondaryContainer,
    onSecondaryContainer = LightCampusOnSecondaryContainer,

    tertiary = LightCampusTertiary,
    onTertiary = LightCampusOnTertiary,
    tertiaryContainer = LightCampusTertiaryContainer,
    onTertiaryContainer = LightCampusOnTertiaryContainer,

    background = LightCampusBackground,
    onBackground = LightCampusOnBackground,
    surface = LightCampusSurface,
    onSurface = LightCampusOnSurface,
    surfaceVariant = LightCampusSurfaceVariant,
    onSurfaceVariant = LightCampusOnSurfaceVariant,

    outline = LightCampusOutline,
    error = LightCampusError,
    onError = LightCampusOnError
)

@Composable
fun TARUMTFoodOrderingSystemTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) {
            DarkColorScheme
        } else {
            LightColorScheme
        },
        typography = Typography,
        content = content
    )
}