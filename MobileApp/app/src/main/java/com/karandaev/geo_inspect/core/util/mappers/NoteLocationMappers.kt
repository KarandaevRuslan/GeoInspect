package com.karandaev.geo_inspect.core.util.mappers

import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import com.karandaev.geo_inspect.core.location.LocatedPlace

/**
 * Converts note coordinates to a map-level [com.karandaev.geo_inspect.core.domain.model.location.MapPoint].
 */
fun InspectionReport.toMapPoint(): MapPoint {
  return MapPoint(
    latitude = latitude,
    longitude = longitude
  )
}

/**
 * Converts note coordinates to a [LocatedPlace].
 */
fun InspectionReport.toLocatedPlace(): LocatedPlace {
  return toMapPoint().toLocatedPlace(
    placeName = title.ifBlank { "Note location" }
  )
}