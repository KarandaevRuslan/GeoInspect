package com.karandaev.geo_inspect.core.location.provider

import com.karandaev.geo_inspect.core.location.AppLocation
import com.karandaev.geo_inspect.core.location.LocationDefaults

/**
 * Provides app-level location values.
 *
 * Implementations may use Android system services, Google Play Services, mock data,
 * or any other source. Callers receive [AppLocation] instead of Android framework location types.
 */
interface DeviceLocationProvider {

  /**
   * Returns the best real device location available now.
   *
   * Returns null when location permission is missing, providers are disabled,
   * or no usable location can be resolved.
   */
  suspend fun getLocation(): AppLocation?

  /**
   * Returns the best real device location if available, otherwise returns the app default location.
   */
  suspend fun getLocationOrDefault(): AppLocation {
    return getLocation() ?: LocationDefaults.location
  }
}