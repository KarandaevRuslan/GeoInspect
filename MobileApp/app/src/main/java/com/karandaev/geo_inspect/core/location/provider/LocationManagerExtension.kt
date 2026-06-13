package com.karandaev.geo_inspect.core.location.provider

import android.content.Context
import android.location.Location
import android.location.LocationManager

/**
 * Returns the system [LocationManager], or null if unavailable.
 */
internal fun Context.locationManagerOrNull(): LocationManager? {
  return getSystemService(Context.LOCATION_SERVICE) as? LocationManager
}

/**
 * Safely checks whether the provider is enabled.
 */
internal fun LocationManager.isProviderEnabledSafe(provider: String): Boolean {
  return runCatching {
    isProviderEnabled(provider)
  }.getOrDefault(false)
}

/**
 * Safely reads the last known location for the given provider.
 */
internal fun LocationManager.getLastKnownLocationSafe(provider: String): Location? {
  return runCatching {
    @Suppress("MissingPermission")
    getLastKnownLocation(provider)
  }.getOrNull()
}