package com.karandaev.geo_inspect.core.location.resolver

import com.karandaev.geo_inspect.core.location.AppLocation
import com.karandaev.geo_inspect.core.location.LocatedPlace
import com.karandaev.geo_inspect.core.location.provider.DeviceLocationProvider
import com.karandaev.geo_inspect.core.location.place.PlaceNameProvider

/**
 * Default implementation of [LocationResolver].
 *
 * This class combines [DeviceLocationProvider] and [PlaceNameProvider] to return a single
 * UI-friendly model containing both coordinates and a human-readable place name.
 *
 * @param locationProvider Provider used to obtain device or fallback locations.
 * @param placeNameProvider Provider used to resolve place names from coordinates.
 */
class DefaultLocationResolver(
  private val locationProvider: DeviceLocationProvider,
  private val placeNameProvider: PlaceNameProvider
) : LocationResolver {

  /**
   * Returns the best available real device location with its place name.
   *
   * Returns null if the device location cannot be resolved.
   */
  override suspend fun getLocatedPlace(): LocatedPlace? {
    val location = locationProvider.getLocation() ?: return null
    return resolvePlace(location)
  }

  /**
   * Returns the best available real device location with its place name,
   * or the configured default location with the configured default place name.
   */
  override suspend fun getLocatedPlaceOrDefault(): LocatedPlace {
    val location = locationProvider.getLocationOrDefault()
    return resolvePlace(location)
  }

  /**
   * Resolves a place name for [location] and returns a combined model.
   */
  override suspend fun resolvePlace(location: AppLocation): LocatedPlace {
    val placeName = placeNameProvider.getPlaceNameOrDefault(location)

    return LocatedPlace(
      location = location,
      placeName = placeName
    )
  }
}