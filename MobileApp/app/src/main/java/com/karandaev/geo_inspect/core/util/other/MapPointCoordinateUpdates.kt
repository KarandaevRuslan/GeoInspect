package com.karandaev.geo_inspect.core.util.other

import com.karandaev.geo_inspect.core.domain.model.location.MapPoint

/**
 * Returns a point with updated latitude.
 *
 * If the current point is null, fallback longitude is used.
 */
fun MapPoint?.withLatitude(
  latitude: Double,
  fallbackLongitude: Double
): MapPoint {
  return MapPoint(
    latitude = latitude,
    longitude = this?.longitude ?: fallbackLongitude
  )
}

/**
 * Returns a point with updated longitude.
 *
 * If the current point is null, fallback latitude is used.
 */
fun MapPoint?.withLongitude(
  longitude: Double,
  fallbackLatitude: Double
): MapPoint {
  return MapPoint(
    latitude = this?.latitude ?: fallbackLatitude,
    longitude = longitude
  )
}