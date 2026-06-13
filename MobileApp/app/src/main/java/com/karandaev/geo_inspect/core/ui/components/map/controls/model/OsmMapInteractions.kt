package com.karandaev.geo_inspect.core.ui.components.map.controls.model

import androidx.compose.runtime.Immutable
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint

internal typealias MapPointHandler = (MapPoint) -> Unit

/**
 * Defines user interactions emitted by the map.
 *
 * @property onTap Called when the map surface is tapped.
 * @property onLongPress Called when the map surface is long-pressed.
 * @property onMarkerClick Called when a clickable marker is selected.
 */
@Immutable
data class OsmMapInteractions(
  val onTap: MapPointHandler? = null,
  val onLongPress: MapPointHandler? = null,
  val onMarkerClick: (String) -> Unit = {}
)