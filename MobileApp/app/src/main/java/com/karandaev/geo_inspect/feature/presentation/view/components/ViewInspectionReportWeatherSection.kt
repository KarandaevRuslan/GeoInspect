package com.karandaev.geo_inspect.feature.presentation.view.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.core.presentation.location.LocationUiState
import com.karandaev.geo_inspect.core.presentation.weather.WeatherUiState
import com.karandaev.geo_inspect.feature.ui.components.cards.WeatherCard

@Composable
internal fun ViewInspectionReportWeatherSection(
  inspectionLocationState: LocationUiState,
  inspectionWeatherState: WeatherUiState,
  onRetry: () -> Unit
) {
  Text(
    text = "Weather at this location",
    style = MaterialTheme.typography.labelLarge,
    color = MaterialTheme.colorScheme.onSurfaceVariant,
    modifier = Modifier.padding(top = 20.dp, bottom = 8.dp)
  )

  WeatherCard(
    locationState = inspectionLocationState,
    weatherState = inspectionWeatherState,
    onRetry = onRetry,
    compact = true
  )
}