package com.karandaev.geo_inspect.core.ui.components.map.controls.focus_menu.ui.formatter

import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import com.karandaev.geo_inspect.core.ui.components.map.controls.focus_menu.model.OsmFocusMenuItem
import com.karandaev.geo_inspect.core.ui.components.map.controls.focus_menu.model.OsmFocusMenuItemKind
import com.karandaev.geo_inspect.core.util.formatters.formatCoordinates
import java.util.Locale
import kotlin.math.roundToInt

/**
 * Returns a short UI label for the focus destination item.
 */
internal fun OsmFocusMenuItem.focusMenuLabel(): String {
  return if (isOrigin) {
    "${kind.focusMenuLabel()} · origin"
  } else {
    kind.focusMenuLabel()
  }
}

/**
 * Formats a map point for compact display in the focus menu.
 */
internal fun MapPoint.focusMenuPositionText(): String {
  return formatCoordinates(latitude, longitude)
}

/**
 * Formats the spherical distance from the menu distance origin.
 */
internal fun OsmFocusMenuItem.focusMenuDistanceText(): String {
  if (isOrigin) {
    return "Origin"
  }

  return distanceFromOriginMeters.formatDistanceFromOrigin()
}

/**
 * Returns a short UI label for the focus destination kind.
 */
private fun OsmFocusMenuItemKind.focusMenuLabel(): String {
  return when (this) {
    OsmFocusMenuItemKind.FocusTarget -> "target"
    OsmFocusMenuItemKind.Current -> "current"
    OsmFocusMenuItemKind.InitialCenter -> "center"
    OsmFocusMenuItemKind.Marker -> "marker"
  }
}

/**
 * Formats a distance from the menu distance origin.
 */
private fun Double.formatDistanceFromOrigin(): String {
  return when {
    this < 1_000.0 -> "${roundToInt()} m from origin"
    else -> String.format(
      Locale.US,
      "%.2f km from origin",
      this / 1_000.0
    )
  }
}