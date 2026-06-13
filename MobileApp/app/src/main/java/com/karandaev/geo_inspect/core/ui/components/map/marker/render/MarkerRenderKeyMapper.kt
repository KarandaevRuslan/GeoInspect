package com.karandaev.geo_inspect.core.ui.components.map.marker.render

import com.karandaev.geo_inspect.core.ui.components.map.marker.model.MarkerRenderKey
import com.karandaev.geo_inspect.core.ui.components.map.marker.model.OsmMarkerSpec

/**
 * Converts marker configuration into a value suitable for render diffing.
 */
internal fun OsmMarkerSpec.renderKey(): MarkerRenderKey {
  return MarkerRenderKey(
    id = id,
    latitude = point.latitude,
    longitude = point.longitude,
    kind = kind,
    title = title,
    snippet = snippet,
    sizeDp = sizeDp,
    clickable = clickable
  )
}