package com.karandaev.geo_inspect.core.ui.components.map.controls.model

import androidx.compose.runtime.Immutable

/**
 * Configures optional controls shown above the map.
 *
 * @property showZoomControls Whether zoom-in and zoom-out controls are visible.
 * @property showFocusButton Whether a focus/location button is visible.
 * @property focusButtonText Text displayed inside the focus button.
 * @property onFocusClick Optional custom action for the focus button.
 */
@Immutable
data class OsmMapControls(
  val showZoomControls: Boolean = true,
  val showFocusButton: Boolean = false,
  val focusButtonText: String = "⌖",
  val onFocusClick: (() -> Unit)? = null
)