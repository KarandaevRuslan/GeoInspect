package com.karandaev.geo_inspect.feature.presentation.create.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import com.karandaev.geo_inspect.core.domain.model.location.mapPointOrNull

/**
 * Creates and remembers an initial selected point from optional coordinates.
 *
 * @param latitude Optional latitude.
 * @param longitude Optional longitude.
 * @return Map point when both coordinates are valid, otherwise null.
 */
@Composable
internal fun rememberInitialSelectedPoint(
  latitude: Double?,
  longitude: Double?
): MapPoint? {
  return remember(latitude, longitude) {
    mapPointOrNull(
      latitude = latitude,
      longitude = longitude
    )
  }
}