package com.karandaev.geo_inspect.core.location.mapper

import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import org.osmdroid.util.GeoPoint

/**
 * Converts a domain-level [MapPoint] into an OSMDroid [GeoPoint].
 */
fun MapPoint.toGeoPoint(): GeoPoint {
  return GeoPoint(latitude, longitude)
}

/**
 * Converts an OSMDroid [GeoPoint] into a domain-level [MapPoint].
 */
fun GeoPoint.toMapPoint(): MapPoint {
  return MapPoint(
    latitude = latitude,
    longitude = longitude
  )
}