package com.karandaev.geo_inspect.core.ui.components.map.viewport

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

/**
 * Provides the outer Compose container for the map.
 *
 * The container applies viewport-specific sizing and decoration while keeping
 * map content composition independent from presentation mode.
 */
@Composable
internal fun MapContainer(
  viewport: OsmMapViewport,
  modifier: Modifier = Modifier,
  content: @Composable BoxScope.() -> Unit
) {
  val containerModifier = when (viewport) {
    OsmMapViewport.Fill -> {
      modifier.fillMaxSize()
    }

    is OsmMapViewport.Card -> {
      modifier
        .fillMaxWidth()
        .height(viewport.height)
        .clip(RoundedCornerShape(viewport.cornerRadius))
        .border(
          border = BorderStroke(
            width = viewport.borderWidth,
            color = MaterialTheme.colorScheme.outlineVariant
          ),
          shape = RoundedCornerShape(viewport.cornerRadius)
        )
    }
  }

  Box(
    modifier = containerModifier,
    content = content
  )
}