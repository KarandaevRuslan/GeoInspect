package com.karandaev.geo_inspect.core.location.source

import com.karandaev.geo_inspect.core.location.LocatedPlace

/**
 * Source of location data for [com.karandaev.geo_inspect.core.presentation.location.LocationViewModel].
 *
 * Implementations may resolve current device location or build location data
 * from explicitly provided coordinates.
 */
fun interface LocationSource {

  /**
   * Resolves location data.
   *
   * Returns null when location cannot be resolved.
   */
  suspend fun resolve(): LocatedPlace?
}