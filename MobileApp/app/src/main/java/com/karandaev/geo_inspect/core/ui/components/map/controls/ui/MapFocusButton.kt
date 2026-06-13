package com.karandaev.geo_inspect.core.ui.components.map.controls.ui

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Displays a focus button over the map.
 *
 * The caller owns the focus target and behavior. A regular click focuses the
 * default destination, while a long click may open the focus destination menu.
 */
@Composable
internal fun BoxScope.MapFocusButton(
  text: String,
  metrics: MapControlMetrics,
  onClick: () -> Unit,
  onLongClick: () -> Unit = {}
) {
  MapOverlayButton(
    text = text,
    size = metrics.primaryButtonSize,
    modifier = Modifier
      .align(Alignment.BottomStart)
      .padding(
        start = metrics.edgePadding,
        bottom = metrics.edgePadding
      ),
    onClick = onClick,
    onLongClick = onLongClick
  )
}