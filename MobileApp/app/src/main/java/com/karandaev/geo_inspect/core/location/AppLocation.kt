package com.karandaev.geo_inspect.core.location

/**
 * App-level location model.
 *
 * This model is independent from Android's [android.location.Location] and can be used safely
 * across UI, domain, and data layers.
 *
 * @param latitude Latitude in degrees.
 * @param longitude Longitude in degrees.
 * @param accuracyMeters Estimated horizontal accuracy in meters, or null if unavailable.
 * @param timestampMillis Location timestamp in milliseconds, or null for synthetic fallback values.
 * @param provider Name of the location provider, or null for synthetic fallback values.
 * @param isDefault Whether this location is an app fallback rather than a real device location.
 */
data class AppLocation(
  val latitude: Double,
  val longitude: Double,
  val accuracyMeters: Float? = null,
  val timestampMillis: Long? = null,
  val provider: String? = null,
  val isDefault: Boolean = false
)