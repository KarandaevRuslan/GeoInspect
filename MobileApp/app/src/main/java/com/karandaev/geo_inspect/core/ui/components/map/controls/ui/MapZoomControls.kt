package com.karandaev.geo_inspect.core.ui.components.map.controls.ui

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Displays zoom controls over the map.
 *
 * The caller owns the actual zoom behavior.
 * Zoom buttons support both regular clicks and press-and-hold repeating.
 */
@Composable
internal fun BoxScope.MapZoomControls(
  metrics: MapControlMetrics,
  onZoomIn: () -> Unit,
  onZoomOut: () -> Unit
) {
  Column(
    modifier = Modifier
      .align(Alignment.CenterEnd)
      .padding(end = metrics.edgePadding),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    MapOverlayButton(
      text = "+",
      size = metrics.buttonSize,
      modifier = Modifier.padding(vertical = 4.dp),
      onClick = onZoomIn,
      onPressRepeat = onZoomIn
    )

    MapOverlayButton(
      text = "−",
      size = metrics.buttonSize,
      modifier = Modifier.padding(vertical = 4.dp),
      onClick = onZoomOut,
      onPressRepeat = onZoomOut
    )
  }
}