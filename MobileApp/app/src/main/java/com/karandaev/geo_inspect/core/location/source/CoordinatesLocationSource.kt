package com.karandaev.geo_inspect.core.location.source

import com.karandaev.geo_inspect.core.location.AppLocation
import com.karandaev.geo_inspect.core.location.LocatedPlace
import com.karandaev.geo_inspect.core.location.place.PlaceNameProvider

/**
 * Location source backed by explicitly provided coordinates.
 *
 * Place name is optional: when it cannot be resolved, [LocatedPlace.placeName]
 * is allowed to be blank.
 */
class CoordinatesLocationSource(
  private val latitude: Double,
  private val longitude: Double,
  private val placeNameProvider: PlaceNameProvider
) : LocationSource {

  override suspend fun resolve(): LocatedPlace {
    val location = AppLocation(
      latitude = latitude,
      longitude = longitude
    )

    return LocatedPlace(
      location = location,
      placeName = placeNameProvider.getPlaceName(
        latitude = latitude,
        longitude = longitude
      )
    )
  }
}