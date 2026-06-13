package com.karandaev.geo_inspect.core.ui.components.map.controls.focus_menu.model

import androidx.compose.runtime.Immutable
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint

/**
 * Describes a selectable focus destination displayed in the focus menu.
 *
 * @property id Stable item identifier used by lazy lists and click handling.
 * @property title Human-readable destination title.
 * @property point Destination position.
 * @property kind Semantic destination type used for labels and ordering.
 * @property distanceFromOriginMeters Spherical distance from the menu distance origin in meters.
 * @property isOrigin Whether this destination is used as the distance origin.
 */
@Immutable
data class OsmFocusMenuItem(
  val id: String,
  val title: String,
  val point: MapPoint,
  val kind: OsmFocusMenuItemKind,
  val distanceFromOriginMeters: Double,
  val isOrigin: Boolean
)

/**
 * Describes the semantic source of a focus menu item.
 */
@Immutable
enum class OsmFocusMenuItemKind {
  FocusTarget,
  Current,
  InitialCenter,
  Marker
}