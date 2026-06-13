package com.karandaev.geo_inspect.core.location.place

import com.karandaev.geo_inspect.core.location.AppLocation
import com.karandaev.geo_inspect.core.util.formatters.formatCoordinates

/**
 * Provides human-readable place names for geographic locations.
 *
 * Implementations may use Android Geocoder, network services, cached data,
 * or test fixtures. Callers should depend on this interface instead of a concrete
 * Android implementation.
 */
interface PlaceNameProvider {

  /**
   * Returns a human-readable place name for the provided coordinates.
   *
   * Returns an empty string when the place name cannot be resolved.
   *
   * @param latitude Latitude in degrees.
   * @param longitude Longitude in degrees.
   */
  suspend fun getPlaceName(
    latitude: Double,
    longitude: Double
  ): String

  /**
   * Returns a human-readable place name for the provided app location.
   *
   * Returns formatted coordinates immediately when [location] represents an app fallback.
   */
  suspend fun getPlaceName(location: AppLocation): String {
    if (location.isDefault) {
      return formatCoordinates(
        latitude = location.latitude,
        longitude = location.longitude
      )
    }

    return getPlaceName(
      latitude = location.latitude,
      longitude = location.longitude
    )
  }

  /**
   * Returns a place name for the provided coordinates, falling back to formatted coordinates.
   *
   * This is useful for UI states where an empty location label should not be shown.
   */
  suspend fun getPlaceNameOrDefault(
    latitude: Double,
    longitude: Double
  ): String {
    return getPlaceName(latitude, longitude)
      .ifBlank { formatCoordinates(latitude, longitude) }
  }

  /**
   * Returns a place name for the provided app location, falling back to formatted coordinates.
   */
  suspend fun getPlaceNameOrDefault(location: AppLocation): String {
    return getPlaceName(location)
      .ifBlank {
        formatCoordinates(
          latitude = location.latitude,
          longitude = location.longitude
        )
      }
  }
}