package com.karandaev.geo_inspect.core.location.source

import com.karandaev.geo_inspect.core.location.LocatedPlace
import com.karandaev.geo_inspect.core.location.resolver.LocationResolver

/**
 * Location source backed by current device location resolving.
 */
class CurrentLocationSource(
  private val locationResolver: LocationResolver
) : LocationSource {

  override suspend fun resolve(): LocatedPlace? {
    return locationResolver.getLocatedPlace()
  }
}