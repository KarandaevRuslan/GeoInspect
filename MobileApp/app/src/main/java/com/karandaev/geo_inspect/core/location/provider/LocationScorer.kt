package com.karandaev.geo_inspect.core.location.provider

import android.location.Location
import android.location.LocationManager

private const val DEFAULT_ACCURACY_METERS = 1_000.0
private const val ACCURACY_WEIGHT = 1.0
private const val AGE_WEIGHT = 5.0
private const val NANOS_IN_MILLIS = 1_000_000L

/**
 * Selects the best location from a list of candidates.
 *
 * Locations are scored by accuracy, monotonic age, and provider type.
 */
internal object LocationScorer {

  /**
   * Returns the best location candidate, or null if the list is empty.
   */
  fun chooseBestLocation(
    locations: List<Location>,
    nowElapsedRealtimeMillis: Long
  ): Location? {
    return locations.maxByOrNull { location ->
      location.qualityScore(nowElapsedRealtimeMillis)
    }
  }

  /**
   * Returns true if the location is recent enough.
   */
  fun Location.isFreshEnough(
    nowElapsedRealtimeMillis: Long,
    maxAgeMillis: Long
  ): Boolean {
    return elapsedRealtimeNanos > 0L &&
      nowElapsedRealtimeMillis - elapsedRealtimeMillis() <= maxAgeMillis
  }

  private fun Location.qualityScore(
    nowElapsedRealtimeMillis: Long
  ): Double {
    val ageMinutes = locationAgeMillis(nowElapsedRealtimeMillis) / 60_000.0
    val accuracyMeters = if (hasAccuracy()) {
      accuracy.toDouble()
    } else {
      DEFAULT_ACCURACY_METERS
    }

    return -accuracyMeters * ACCURACY_WEIGHT -
      ageMinutes * AGE_WEIGHT +
      providerBonus(provider)
  }

  private fun Location.locationAgeMillis(
    nowElapsedRealtimeMillis: Long
  ): Long {
    return (nowElapsedRealtimeMillis - elapsedRealtimeMillis())
      .coerceAtLeast(0L)
  }

  private fun Location.elapsedRealtimeMillis(): Long {
    return elapsedRealtimeNanos / NANOS_IN_MILLIS
  }

  private fun providerBonus(provider: String?): Double {
    return when (provider) {
      LocationManager.GPS_PROVIDER -> 30.0
      LocationManager.NETWORK_PROVIDER -> 10.0
      LocationManager.PASSIVE_PROVIDER -> 0.0
      else -> 0.0
    }
  }
}