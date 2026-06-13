package com.karandaev.geo_inspect.core.location.resolver

import com.karandaev.geo_inspect.core.location.AppLocation
import com.karandaev.geo_inspect.core.location.LocatedPlace

/**
 * Resolves app locations together with human-readable place names.
 *
 * This abstraction combines location acquisition and reverse geocoding behind one API.
 */
interface LocationResolver {

  /**
   * Returns the best available real device location with its place name.
   *
   * Returns null when a real device location cannot be obtained.
   */
  suspend fun getLocatedPlace(): LocatedPlace?

  /**
   * Returns the best available real device location with its place name,
   * or the app default location with the default place name.
   */
  suspend fun getLocatedPlaceOrDefault(): LocatedPlace

  /**
   * Resolves a place name for the provided location and returns the combined model.
   */
  suspend fun resolvePlace(location: AppLocation): LocatedPlace
}