package com.karandaev.geo_inspect.core.weather.openmeteo

import com.karandaev.geo_inspect.core.weather.WeatherInfo
import com.karandaev.geo_inspect.core.weather.openmeteo.remote.dto.OpenMeteoWeatherResponseDto

/**
 * Converts Open-Meteo API responses into domain weather models.
 *
 * This mapper only extracts required weather data and does not interpret it
 * for presentation.
 */
internal fun OpenMeteoWeatherResponseDto.toWeatherInfo(): WeatherInfo {
  val currentData = current
    ?: throw OpenMeteoWeatherParseException("Open-Meteo response: current field is missing")

  val temperature = currentData.temperature
    ?: throw OpenMeteoWeatherParseException("Open-Meteo response: temperature is missing")

  val weatherCode = currentData.weatherCode
    ?: throw OpenMeteoWeatherParseException("Open-Meteo response: weather_code is missing")

  return WeatherInfo(
    temperature = temperature,
    weatherCode = weatherCode
  )
}

/**
 * Error thrown when an Open-Meteo response cannot be converted into [WeatherInfo].
 */
class OpenMeteoWeatherParseException(message: String) : RuntimeException(message)