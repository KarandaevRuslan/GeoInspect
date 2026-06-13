package com.karandaev.geo_inspect.core.location.mapper

import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import com.karandaev.geo_inspect.core.domain.model.location.mapPointOrNull
import com.karandaev.geo_inspect.core.location.AppLocation
import com.karandaev.geo_inspect.core.location.LocatedPlace

/**
 * Converts an [AppLocation] into a domain-level [com.karandaev.geo_inspect.core.domain.model.location.MapPoint].
 */
fun AppLocation.toMapPoint(): MapPoint {
  return MapPoint(
    latitude = latitude,
    longitude = longitude
  )
}

/**
 * Converts a [LocatedPlace] into a domain-level [MapPoint].
 */
fun LocatedPlace.toMapPoint(): MapPoint {
  return MapPoint(
    latitude = latitude,
    longitude = longitude
  )
}

/**
 * Creates a [MapPoint] from a nullable [AppLocation].
 */
fun mapPointOrNull(location: AppLocation?): MapPoint? {
  return location?.let {
    mapPointOrNull(
      latitude = it.latitude,
      longitude = it.longitude
    )
  }
}

/**
 * Creates a [MapPoint] from a nullable [LocatedPlace].
 */
fun mapPointOrNull(place: LocatedPlace?): MapPoint? {
  return place?.let {
    mapPointOrNull(
      latitude = it.latitude,
      longitude = it.longitude
    )
  }
}