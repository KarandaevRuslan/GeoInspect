package com.karandaev.geo_inspect.feature.presentation.weather_card.mapper

import com.karandaev.geo_inspect.core.location.LocatedPlace
import com.karandaev.geo_inspect.core.presentation.weather.WeatherUiState
import com.karandaev.geo_inspect.core.util.formatters.formatTemperature
import com.karandaev.geo_inspect.feature.presentation.weather_card.WeatherCardUiModel

fun WeatherUiState.Success.toWeatherCardUiModel(
  locatedPlace: LocatedPlace
): WeatherCardUiModel {
  val weatherCodeUi = data.weatherCode.toWeatherCodeUi()

  return WeatherCardUiModel(
    temperature = data.temperature.formatTemperature(),
    description = weatherCodeUi.description,
    locationText = locatedPlace.placeName.takeIf { it.isNotBlank() },
    icon = weatherCodeUi.icon
  )
}