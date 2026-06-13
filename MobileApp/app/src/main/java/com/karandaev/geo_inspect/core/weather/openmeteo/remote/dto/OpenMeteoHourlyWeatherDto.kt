package com.karandaev.geo_inspect.core.weather.openmeteo.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO for the hourly weather section in the Open-Meteo response.
 */
@JsonClass(generateAdapter = true)
data class OpenMeteoHourlyWeatherDto(
  @Json(name = "time") val time: List<String>?,
  @Json(name = "temperature_2m") val temperature: List<Double>?,
  @Json(name = "precipitation_probability") val precipitationProbability: List<Int>?,
  @Json(name = "weather_code") val weatherCode: List<Int>?
)