package com.karandaev.geo_inspect.core.weather.openmeteo.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO for the current weather section in the Open-Meteo response.
 */
@JsonClass(generateAdapter = true)
data class OpenMeteoCurrentWeatherDto(
  @Json(name = "time") val time: String?,
  @Json(name = "temperature_2m") val temperature: Double?,
  @Json(name = "relative_humidity_2m") val relativeHumidity: Int?,
  @Json(name = "weather_code") val weatherCode: Int?,
  @Json(name = "wind_speed_10m") val windSpeed: Double?
)