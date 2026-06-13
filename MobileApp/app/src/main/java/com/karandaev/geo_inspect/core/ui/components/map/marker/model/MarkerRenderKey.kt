package com.karandaev.geo_inspect.core.ui.components.map.marker.model

/**
 * Stable render key used to detect meaningful marker changes.
 */
internal data class MarkerRenderKey(
  val id: String,
  val latitude: Double,
  val longitude: Double,
  val kind: OsmMarkerKind,
  val title: String,
  val snippet: String?,
  val sizeDp: Int,
  val clickable: Boolean
)