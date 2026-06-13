package com.karandaev.geo_inspect.feature.presentation.weather_card

import com.karandaev.geo_inspect.core.ui.components.card.AppCardMedia

data class WeatherCardUiModel(
  val temperature: String,
  val description: String,
  val locationText: String?,
  val icon: AppCardMedia
)