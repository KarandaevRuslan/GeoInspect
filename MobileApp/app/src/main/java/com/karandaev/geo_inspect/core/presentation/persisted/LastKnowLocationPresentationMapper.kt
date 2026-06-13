package com.karandaev.geo_inspect.core.presentation.persisted

import com.karandaev.geo_inspect.core.domain.model.persisted.LastKnownLocation
import com.karandaev.geo_inspect.core.location.AppLocation
import com.karandaev.geo_inspect.core.location.LocatedPlace

/**
 * Converts persisted last known location state into a location object used by the location layer.
 */
internal fun LastKnownLocation.toLocatedPlace(): LocatedPlace {
  return LocatedPlace(
    location = AppLocation(
      latitude = point.latitude,
      longitude = point.longitude,
      timestampMillis = updatedAt
    ),
    placeName = placeName
  )
}