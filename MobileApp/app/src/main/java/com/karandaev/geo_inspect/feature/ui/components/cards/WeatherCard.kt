package com.karandaev.geo_inspect.feature.ui.components.cards

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.core.presentation.location.LocationUiState
import com.karandaev.geo_inspect.core.presentation.weather.WeatherUiState
import com.karandaev.geo_inspect.core.ui.components.card.AppCard
import com.karandaev.geo_inspect.core.ui.components.card.AppCardBackground
import com.karandaev.geo_inspect.core.ui.components.card.AppCardContent
import com.karandaev.geo_inspect.core.ui.components.card.AppCardError
import com.karandaev.geo_inspect.core.ui.components.card.AppCardLoading
import com.karandaev.geo_inspect.core.ui.components.card.AppCardMediaContent
import com.karandaev.geo_inspect.core.ui.theme.WeatherGradientEnd
import com.karandaev.geo_inspect.core.ui.theme.WeatherGradientStart
import com.karandaev.geo_inspect.feature.presentation.weather_card.mapper.toWeatherCardUiModel

@Composable
fun WeatherCard(
  locationState: LocationUiState,
  weatherState: WeatherUiState,
  modifier: Modifier = Modifier,
  onRetry: (() -> Unit)? = null,
  compact: Boolean = false
) {
  val contentColor = Color.White
  val contentMinHeight = if (compact) 56.dp else 72.dp

  AppCard(
    modifier = modifier,
    colors = CardDefaults.cardColors(
      containerColor = WeatherGradientStart
    ),
    border = null,
    background = AppCardBackground.Brush(
      Brush.linearGradient(
        colors = listOf(
          WeatherGradientStart,
          WeatherGradientEnd
        )
      )
    ),
    contentPadding = if (compact) {
      PaddingValues(horizontal = 20.dp, vertical = 12.dp)
    } else {
      PaddingValues(horizontal = 20.dp, vertical = 16.dp)
    }
  ) {
    when (locationState) {
      LocationUiState.Loading -> {
        AppCardLoading(
          message = "Resolving location…",
          minHeight = contentMinHeight,
          contentColor = contentColor
        )
      }

      is LocationUiState.Unknown -> {
        AppCardError(
          title = "Location is unknown",
          message = locationState.message,
          minHeight = contentMinHeight,
          onRetry = onRetry,
          titleColor = contentColor,
          messageColor = contentColor.copy(alpha = 0.9f),
          actionColor = contentColor
        )
      }

      is LocationUiState.Success -> {
        when (weatherState) {
          WeatherUiState.Loading -> {
            AppCardLoading(
              message = "Loading weather…",
              minHeight = contentMinHeight,
              contentColor = contentColor
            )
          }

          is WeatherUiState.Error -> {
            AppCardError(
              title = "Failed to load weather",
              message = weatherState.message,
              minHeight = contentMinHeight,
              onRetry = onRetry,
              titleColor = contentColor,
              messageColor = contentColor.copy(alpha = 0.9f),
              actionColor = contentColor
            )
          }

          is WeatherUiState.Success -> {
            val uiModel = weatherState.toWeatherCardUiModel(
              locatedPlace = locationState.data
            )

            AppCardContent(
              title = uiModel.temperature,
              subtitle = uiModel.description,
              supportingText = uiModel.locationText,
              verticalAlignment = Alignment.CenterVertically,
              horizontalSpacing = 16.dp,
              titleStyle = if (compact) {
                MaterialTheme.typography.titleMedium
              } else {
                MaterialTheme.typography.headlineMedium
              },
              subtitleStyle = if (compact) {
                MaterialTheme.typography.bodySmall
              } else {
                MaterialTheme.typography.bodyMedium
              },
              supportingTextStyle = MaterialTheme.typography.bodySmall,
              titleColor = contentColor,
              subtitleColor = contentColor.copy(alpha = 0.9f),
              supportingTextColor = contentColor.copy(alpha = 0.7f),
              titleMaxLines = 1,
              subtitleMaxLines = 1,
              supportingTextMaxLines = 1,
              leadingContent = {
                AppCardMediaContent(
                  media = uiModel.icon,
                  fallbackIcon = Icons.Default.Cloud,
                  contentDescription = "Weather",
                  fallbackTint = contentColor,
                  modifier = Modifier.size(
                    if (compact) 40.dp else 56.dp
                  )
                )
              }
            )
          }
        }
      }
    }
  }
}