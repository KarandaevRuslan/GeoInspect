package com.karandaev.geo_inspect.core.weather.openmeteo.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO for the Open-Meteo forecast response.
 *
 * Only fields used by the app are declared. Moshi ignores unknown fields.
 */
@JsonClass(generateAdapter = true)
data class OpenMeteoWeatherResponseDto(
  @Json(name = "latitude") val latitude: Double?,
  @Json(name = "longitude") val longitude: Double?,
  @Json(name = "timezone") val timezone: String?,
  @Json(name = "current") val current: OpenMeteoCurrentWeatherDto?,
  @Json(name = "hourly") val hourly: OpenMeteoHourlyWeatherDto?
)