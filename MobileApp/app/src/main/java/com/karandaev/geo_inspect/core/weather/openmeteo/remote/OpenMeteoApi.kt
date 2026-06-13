package com.karandaev.geo_inspect.core.weather.openmeteo.remote

import com.karandaev.geo_inspect.core.weather.openmeteo.remote.dto.OpenMeteoWeatherResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API for Open-Meteo forecast endpoints.
 */
interface OpenMeteoApi {

  /**
   * Loads current weather and hourly forecast by coordinates.
   *
   * @param latitude Latitude in degrees.
   * @param longitude Longitude in degrees.
   * @param current Current weather fields returned by Open-Meteo.
   * @param hourly Hourly forecast fields returned by Open-Meteo.
   * @param forecastDays Number of forecast days.
   * @param timezone `auto` makes Open-Meteo use the local timezone for the coordinates.
   */
  @GET(OpenMeteoEndpoints.ForecastPath)
  suspend fun getForecast(
    @Query("latitude") latitude: Double,
    @Query("longitude") longitude: Double,
    @Query("current") current: String = OpenMeteoQueryDefaults.CurrentFields,
    @Query("hourly") hourly: String = OpenMeteoQueryDefaults.HourlyFields,
    @Query("forecast_days") forecastDays: Int = OpenMeteoQueryDefaults.ForecastDays,
    @Query("timezone") timezone: String = OpenMeteoQueryDefaults.Timezone
  ): OpenMeteoWeatherResponseDto
}

/**
 * Default Open-Meteo query parameters used by the app.
 */
object OpenMeteoQueryDefaults {
  const val CurrentFields =
    "temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m"

  const val HourlyFields =
    "temperature_2m,precipitation_probability,weather_code"

  const val ForecastDays = 1
  const val Timezone = "auto"
}