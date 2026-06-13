package com.karandaev.geo_inspect.core.ui.components.map.controls.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Displays a loading indicator above the map content.
 */
@Composable
internal fun BoxScope.MapLoadingOverlay(
  metrics: MapControlMetrics
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .padding(metrics.edgePadding),
    contentAlignment = Center
  ) {
    Surface(
      shape = RoundedCornerShape(999.dp),
      tonalElevation = 4.dp,
      shadowElevation = 4.dp,
      color = MaterialTheme.colorScheme.surface
    ) {
      CircularProgressIndicator(
        modifier = Modifier
          .padding(16.dp)
          .size(28.dp)
      )
    }
  }
}