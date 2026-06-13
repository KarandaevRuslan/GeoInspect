package com.karandaev.geo_inspect.core.weather.openmeteo

private const val CacheCoordinatePrecision = 10_000.0

/**
 * Cache key for weather data based on rounded coordinates.
 *
 * Coordinates are rounded to reduce duplicated network calls for nearly identical locations.
 */
data class WeatherCacheKey(
  val latitude: Double,
  val longitude: Double
) {
  companion object {

    /**
     * Creates a cache key from raw coordinates.
     */
    fun from(
      latitude: Double,
      longitude: Double
    ): WeatherCacheKey {
      return WeatherCacheKey(
        latitude = latitude.roundForCache(),
        longitude = longitude.roundForCache()
      )
    }
  }
}

private fun Double.roundForCache(): Double {
  return kotlin.math.round(this * CacheCoordinatePrecision) / CacheCoordinatePrecision
}