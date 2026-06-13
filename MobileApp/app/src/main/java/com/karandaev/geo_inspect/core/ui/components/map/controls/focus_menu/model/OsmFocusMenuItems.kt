package com.karandaev.geo_inspect.core.ui.components.map.controls.focus_menu.model

import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import com.karandaev.geo_inspect.core.ui.components.map.marker.model.OsmMarkerKind
import com.karandaev.geo_inspect.core.ui.components.map.marker.model.OsmMarkerSpec

/**
 * Builds focus menu items in the order expected by the map UI.
 *
 * The distance origin is the first current marker when available, otherwise the
 * focus target when available, otherwise the initial center.
 *
 * The final order is:
 * 1. Focus target.
 * 2. Current markers.
 * 3. Initial center.
 * 4. Other markers sorted by distance from the distance origin.
 *
 * Items with the same coordinates are shown only once.
 */
fun buildOsmFocusMenuItems(
  focusTarget: MapPoint?,
  initialCenter: MapPoint,
  markers: List<OsmMarkerSpec>
): List<OsmFocusMenuItem> {
  val distanceOrigin = markers.firstCurrentPoint()
    ?: focusTarget
    ?: initialCenter

  val distanceOriginKey = distanceOrigin.toMapPointKey()
  val usedPoints = mutableSetOf<MapPointKey>()

  return buildList {
    focusTarget?.let { point ->
      addIfNotDuplicate(
        item = point.toFocusMenuItem(
          id = "focus-target",
          title = "Focus target",
          kind = OsmFocusMenuItemKind.FocusTarget,
          distanceOrigin = distanceOrigin,
          distanceOriginKey = distanceOriginKey
        ),
        usedPoints = usedPoints
      )
    }

    markers
      .currentMarkersSortedByDistanceFrom(distanceOrigin)
      .forEach { marker ->
        addIfNotDuplicate(
          item = marker.toFocusMenuItem(
            id = "current-${marker.id}",
            kind = OsmFocusMenuItemKind.Current,
            distanceOrigin = distanceOrigin,
            distanceOriginKey = distanceOriginKey
          ),
          usedPoints = usedPoints
        )
      }

    addIfNotDuplicate(
      item = initialCenter.toFocusMenuItem(
        id = "initial-center",
        title = "Initial center",
        kind = OsmFocusMenuItemKind.InitialCenter,
        distanceOrigin = distanceOrigin,
        distanceOriginKey = distanceOriginKey
      ),
      usedPoints = usedPoints
    )

    markers
      .nonCurrentMarkersSortedByDistanceFrom(distanceOrigin)
      .forEach { marker ->
        addIfNotDuplicate(
          item = marker.toFocusMenuItem(
            id = "marker-${marker.id}",
            kind = OsmFocusMenuItemKind.Marker,
            distanceOrigin = distanceOrigin,
            distanceOriginKey = distanceOriginKey
          ),
          usedPoints = usedPoints
        )
      }
  }
}

/**
 * Returns the first current marker point if it exists.
 */
private fun List<OsmMarkerSpec>.firstCurrentPoint(): MapPoint? {
  return firstOrNull { marker -> marker.kind == OsmMarkerKind.Current }?.point
}

/**
 * Returns current markers sorted by spherical distance from the origin.
 */
private fun List<OsmMarkerSpec>.currentMarkersSortedByDistanceFrom(
  origin: MapPoint
): Sequence<OsmMarkerSpec> {
  return asSequence()
    .filter { marker -> marker.kind == OsmMarkerKind.Current }
    .sortedBy { marker -> marker.point.sphericalDistanceTo(origin) }
}

/**
 * Returns non-current markers sorted by spherical distance from the origin.
 */
private fun List<OsmMarkerSpec>.nonCurrentMarkersSortedByDistanceFrom(
  origin: MapPoint
): Sequence<OsmMarkerSpec> {
  return asSequence()
    .filterNot { marker -> marker.kind == OsmMarkerKind.Current }
    .sortedBy { marker -> marker.point.sphericalDistanceTo(origin) }
}

/**
 * Converts a marker to a focus menu item.
 */
private fun OsmMarkerSpec.toFocusMenuItem(
  id: String,
  kind: OsmFocusMenuItemKind,
  distanceOrigin: MapPoint,
  distanceOriginKey: MapPointKey
): OsmFocusMenuItem {
  return point.toFocusMenuItem(
    id = id,
    title = title,
    kind = kind,
    distanceOrigin = distanceOrigin,
    distanceOriginKey = distanceOriginKey
  )
}

/**
 * Converts a map point to a focus menu item.
 */
private fun MapPoint.toFocusMenuItem(
  id: String,
  title: String,
  kind: OsmFocusMenuItemKind,
  distanceOrigin: MapPoint,
  distanceOriginKey: MapPointKey
): OsmFocusMenuItem {
  return OsmFocusMenuItem(
    id = id,
    title = title,
    point = this,
    kind = kind,
    distanceFromOriginMeters = sphericalDistanceTo(distanceOrigin),
    isOrigin = toMapPointKey() == distanceOriginKey
  )
}

/**
 * Adds an item only when its coordinates were not added before.
 */
private fun MutableList<OsmFocusMenuItem>.addIfNotDuplicate(
  item: OsmFocusMenuItem,
  usedPoints: MutableSet<MapPointKey>
) {
  if (usedPoints.add(item.point.toMapPointKey())) {
    add(item)
  }
}