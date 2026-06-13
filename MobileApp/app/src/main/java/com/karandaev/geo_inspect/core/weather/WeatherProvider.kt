package com.karandaev.geo_inspect.core.weather

/**
 * Provides weather information for geographic coordinates.
 *
 * Implementations may load weather from network APIs, local cache, fake test data,
 * or any other source.
 */
interface WeatherProvider {

  /**
   * Returns current weather for the provided coordinates.
   *
   * @param latitude Latitude in degrees.
   * @param longitude Longitude in degrees.
   * @return Result containing weather information or a mapped loading error.
   */
  suspend fun getWeather(
    latitude: Double,
    longitude: Double
  ): Result<WeatherInfo>
}