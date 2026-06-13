package com.karandaev.geo_inspect.core.ui.components.map.viewport

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.core.ui.components.map.controls.ui.MapControlMetrics
import com.karandaev.geo_inspect.core.ui.components.map.controls.ui.controlMetricsFor

/**
 * Returns control metrics for the current map viewport and available size.
 */
internal fun mapControlMetricsForViewport(
  viewport: OsmMapViewport,
  maxWidth: Dp,
  maxHeight: Dp
): MapControlMetrics {
  val isCompact =
    viewport is OsmMapViewport.Card ||
      maxHeight < 280.dp ||
      maxWidth < 360.dp

  return controlMetricsFor(compact = isCompact)
}