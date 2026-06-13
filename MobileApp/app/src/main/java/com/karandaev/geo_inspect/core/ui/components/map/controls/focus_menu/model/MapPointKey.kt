package com.karandaev.geo_inspect.core.ui.components.map.controls.focus_menu.model

import com.karandaev.geo_inspect.core.domain.model.location.MapPoint

/**
 * Converts a map point to a key used for coordinate-based deduplication.
 */
internal fun MapPoint.toMapPointKey(): MapPointKey {
  return MapPointKey(
    latitude = latitude,
    longitude = longitude
  )
}

/**
 * Coordinate key for removing duplicate focus destinations.
 */
internal data class MapPointKey(
  val latitude: Double,
  val longitude: Double
)