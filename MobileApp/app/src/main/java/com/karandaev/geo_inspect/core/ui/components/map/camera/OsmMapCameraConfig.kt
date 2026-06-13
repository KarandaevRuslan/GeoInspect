package com.karandaev.geo_inspect.core.ui.components.map.camera

import androidx.compose.runtime.Immutable
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint

/**
 * Defines the initial and allowed camera state for an OSMDroid map.
 *
 * @property initialCenter Center used when the map is first created.
 * @property initialZoom Initial zoom level.
 * @property minZoom Lowest allowed zoom level.
 * @property maxZoom Highest allowed zoom level.
 * @property focusZoom Zoom level used for explicit focus actions.
 */
@Immutable
data class OsmMapCameraConfig(
  val initialCenter: MapPoint,
  val initialZoom: Double = 13.0,
  val minZoom: Double = 3.0,
  val maxZoom: Double = 19.0,
  val focusZoom: Double = 15.0
)