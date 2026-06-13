package com.karandaev.geo_inspect.core.location.provider

/**
 * Configuration for [AndroidLocationProvider].
 *
 * @param currentLocationTimeoutMillis Maximum time to wait for a fresh location fix.
 * @param cacheMaxAgeMillis Maximum age of the in-memory cached location.
 * @param freshLocationRefreshIntervalMillis Minimum interval between fresh location requests.
 * @param freshLocationRefreshEveryNthCall Forces a fresh request every Nth call.
 */
data class LocationProviderConfig(
  val currentLocationTimeoutMillis: Long = 10_000L,
  val cacheMaxAgeMillis: Long = 2L * 60L * 1000L,
  val freshLocationRefreshIntervalMillis: Long = 2L * 60L * 1000L,
  val freshLocationRefreshEveryNthCall: Int = 100
)