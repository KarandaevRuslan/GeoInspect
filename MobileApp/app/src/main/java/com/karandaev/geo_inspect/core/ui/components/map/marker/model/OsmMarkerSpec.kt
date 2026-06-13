package com.karandaev.geo_inspect.core.ui.components.map.marker.model

import androidx.compose.runtime.Immutable
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint

/**
 * Describes a marker that should be rendered on the map.
 *
 * This model is intentionally independent from OSMDroid marker instances.
 *
 * @property id Stable marker identifier used for click callbacks and diffing.
 * @property point Marker position.
 * @property kind Semantic marker type.
 * @property title Title shown by the underlying map marker.
 * @property snippet Optional short description shown by the underlying map marker.
 * @property sizeDp Marker icon size in density-independent pixels.
 * @property clickable Whether this marker should dispatch click events.
 */
@Immutable
data class OsmMarkerSpec(
  val id: String,
  val point: MapPoint,
  val kind: OsmMarkerKind,
  val title: String,
  val snippet: String? = null,
  val sizeDp: Int = defaultMarkerSizeDp(kind),
  val clickable: Boolean = false
)

private fun defaultMarkerSizeDp(kind: OsmMarkerKind): Int {
  return when (kind) {
    OsmMarkerKind.Note -> 22
    OsmMarkerKind.Selected -> 22
    OsmMarkerKind.Current -> 18
  }
}