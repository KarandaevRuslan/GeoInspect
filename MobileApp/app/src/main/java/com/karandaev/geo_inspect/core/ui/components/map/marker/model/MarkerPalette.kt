package com.karandaev.geo_inspect.core.ui.components.map.marker.model

/**
 * Color palette used when rendering semantic marker types.
 *
 * @property noteColor Fill color for note markers.
 * @property selectedColor Fill color for selected-location markers.
 * @property currentColor Fill color for current-location markers.
 * @property strokeColor Border color used around marker icons.
 */
internal data class MarkerPalette(
  val noteColor: Int,
  val selectedColor: Int,
  val currentColor: Int,
  val strokeColor: Int
)