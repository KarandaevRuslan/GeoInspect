package com.karandaev.geo_inspect.core.ui.components.map.controls.focus_menu.model

import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

private const val EarthRadiusMeters = 6_371_000.0

/**
 * Calculates the spherical distance between two map points in meters.
 */
internal fun MapPoint.sphericalDistanceTo(other: MapPoint): Double {
  val lat1 = latitude.toRadians()
  val lat2 = other.latitude.toRadians()
  val deltaLatitude = (other.latitude - latitude).toRadians()
  val deltaLongitude = (other.longitude - longitude).toRadians()

  val haversine = sin(deltaLatitude / 2).pow(2) +
    cos(lat1) * cos(lat2) * sin(deltaLongitude / 2).pow(2)

  val angularDistance = 2 * atan2(
    sqrt(haversine),
    sqrt(1 - haversine)
  )

  return EarthRadiusMeters * angularDistance
}

/**
 * Converts degrees to radians.
 */
private fun Double.toRadians(): Double {
  return Math.toRadians(this)
}