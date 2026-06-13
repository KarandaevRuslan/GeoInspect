package com.karandaev.geo_inspect.core.weather.openmeteo

import com.karandaev.geo_inspect.core.weather.WeatherInfo
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Thread-safe in-memory cache for weather results.
 *
 * Cached values expire after [ttlMillis]. When the cache exceeds [maxSize],
 * the oldest entries are removed.
 */
class WeatherCache(
  private val ttlMillis: Long,
  private val maxSize: Int
) {
  private val mutex = Mutex()
  private val values = mutableMapOf<WeatherCacheKey, CachedWeather>()

  /**
   * Returns cached weather for [key], or null if missing or expired.
   */
  suspend fun get(key: WeatherCacheKey): WeatherInfo? {
    val nowMillis = System.currentTimeMillis()

    return mutex.withLock {
      val cached = values[key] ?: return@withLock null

      if (nowMillis - cached.createdAtMillis <= ttlMillis) {
        cached.weatherInfo
      } else {
        values.remove(key)
        null
      }
    }
  }

  /**
   * Stores weather information for [key].
   */
  suspend fun put(
    key: WeatherCacheKey,
    weatherInfo: WeatherInfo
  ) {
    mutex.withLock {
      values[key] = CachedWeather(
        weatherInfo = weatherInfo,
        createdAtMillis = System.currentTimeMillis()
      )

      trimIfNeeded()
    }
  }

  private fun trimIfNeeded() {
    if (values.size <= maxSize) return

    val keysToRemove = values.entries
      .sortedBy { entry -> entry.value.createdAtMillis }
      .take(values.size - maxSize)
      .map { entry -> entry.key }

    keysToRemove.forEach { key ->
      values.remove(key)
    }
  }

  private data class CachedWeather(
    val weatherInfo: WeatherInfo,
    val createdAtMillis: Long
  )
}