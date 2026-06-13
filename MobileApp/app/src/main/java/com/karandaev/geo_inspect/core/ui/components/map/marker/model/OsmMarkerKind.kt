package com.karandaev.geo_inspect.core.ui.components.map.marker.model

import androidx.compose.runtime.Immutable

/**
 * Identifies the semantic role of a marker on the map.
 */
@Immutable
enum class OsmMarkerKind {
  Note,
  Selected,
  Current
}