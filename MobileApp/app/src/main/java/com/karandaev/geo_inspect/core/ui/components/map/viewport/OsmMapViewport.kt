package com.karandaev.geo_inspect.core.ui.components.map.viewport

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Describes how the map should be laid out inside Compose UI.
 */
@Immutable
sealed interface OsmMapViewport {
  /**
   * Makes the map fill all space provided by its parent.
   */
  data object Fill : OsmMapViewport

  /**
   * Displays the map as a compact card suitable for forms, previews, and detail screens.
   *
   * @property height Fixed card height.
   * @property cornerRadius Corner radius applied to the card.
   * @property borderWidth Border width around the card.
   */
  data class Card(
    val height: Dp = 180.dp,
    val cornerRadius: Dp = 12.dp,
    val borderWidth: Dp = 1.dp
  ) : OsmMapViewport
}