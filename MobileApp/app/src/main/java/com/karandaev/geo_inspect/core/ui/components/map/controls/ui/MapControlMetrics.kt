package com.karandaev.geo_inspect.core.ui.components.map.controls.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Size and spacing values for map overlay controls.
 *
 * @property edgePadding Distance between controls and map edges.
 * @property buttonSize Default overlay button size.
 * @property primaryButtonSize Size for the primary focus/location button.
 */
@Immutable
internal data class MapControlMetrics(
  val edgePadding: Dp,
  val buttonSize: Dp,
  val primaryButtonSize: Dp
)

/**
 * Returns control metrics appropriate for compact or full-size map layouts.
 */
internal fun controlMetricsFor(
  compact: Boolean
): MapControlMetrics {
  return if (compact) {
    MapControlMetrics(
      edgePadding = 8.dp,
      buttonSize = 40.dp,
      primaryButtonSize = 40.dp
    )
  } else {
    MapControlMetrics(
      edgePadding = 20.dp,
      buttonSize = 40.dp,
      primaryButtonSize = 56.dp
    )
  }
}