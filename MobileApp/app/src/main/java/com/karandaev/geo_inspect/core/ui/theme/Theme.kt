package com.karandaev.geo_inspect.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.karandaev.geo_inspect.core.domain.model.ThemeMode

private val LightColorScheme = lightColorScheme(
  primary = Primary,
  onPrimary = OnPrimary,
  primaryContainer = PrimaryContainer,
  onPrimaryContainer = OnPrimaryContainer,
  secondary = Secondary,
  onSecondary = OnSecondary,
  secondaryContainer = SecondaryContainer,
  onSecondaryContainer = OnSecondaryContainer,
  surface = Surface,
  surfaceVariant = SurfaceVariant,
  onSurface = OnSurface,
  onSurfaceVariant = OnSurfaceVariant,
  outline = Outline,
  outlineVariant = OutlineVariant,
  error = Error,
  onError = OnError,
  errorContainer = ErrorContainer,
  onErrorContainer = OnErrorContainer
)

private val DarkColorScheme = darkColorScheme(
  primary = DarkPrimary,
  onPrimary = DarkOnPrimary,
  primaryContainer = DarkPrimaryContainer,
  onPrimaryContainer = DarkOnPrimaryContainer,
  secondary = DarkSecondary,
  onSecondary = DarkOnSecondary,
  secondaryContainer = DarkSecondaryContainer,
  onSecondaryContainer = DarkOnSecondaryContainer,
  surface = DarkSurface,
  surfaceVariant = DarkSurfaceVariant,
  onSurface = DarkOnSurface,
  onSurfaceVariant = DarkOnSurfaceVariant,
  outline = DarkOutline,
  outlineVariant = DarkOutlineVariant,
  error = DarkError,
  onError = DarkOnError,
  errorContainer = DarkErrorContainer,
  onErrorContainer = DarkOnErrorContainer
)

@Composable
fun MyTheme(
  themeMode: ThemeMode = ThemeMode.LIGHT,
  content: @Composable () -> Unit
) {
  val colorScheme = when (themeMode) {
    ThemeMode.LIGHT -> LightColorScheme
    ThemeMode.DARK -> DarkColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}