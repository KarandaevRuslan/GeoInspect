package com.karandaev.geo_inspect.core.domain.model.location

import androidx.compose.runtime.Immutable

/**
 * Represents a geographical coordinate used by the map layer.
 *
 * This model keeps map-related APIs independent from OSMDroid types.
 *
 * @property latitude Latitude in decimal degrees.
 * @property longitude Longitude in decimal degrees.
 */
@Immutable
data class MapPoint(
  val latitude: Double,
  val longitude: Double
)