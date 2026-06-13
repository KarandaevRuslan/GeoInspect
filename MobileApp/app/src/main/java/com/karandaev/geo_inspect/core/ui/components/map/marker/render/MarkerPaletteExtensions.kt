package com.karandaev.geo_inspect.core.ui.components.map.marker.render

import com.karandaev.geo_inspect.core.ui.components.map.marker.model.MarkerPalette
import com.karandaev.geo_inspect.core.ui.components.map.marker.model.OsmMarkerKind

/**
 * Returns the fill color associated with a marker kind.
 */
internal fun MarkerPalette.fillColorFor(kind: OsmMarkerKind): Int {
  return when (kind) {
    OsmMarkerKind.Note -> noteColor
    OsmMarkerKind.Selected -> selectedColor
    OsmMarkerKind.Current -> currentColor
  }
}