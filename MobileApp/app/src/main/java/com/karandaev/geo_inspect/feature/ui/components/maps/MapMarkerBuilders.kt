package com.karandaev.geo_inspect.feature.ui.components.maps

import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import com.karandaev.geo_inspect.core.ui.components.map.marker.model.OsmMarkerKind
import com.karandaev.geo_inspect.core.ui.components.map.marker.model.OsmMarkerSpec
import com.karandaev.geo_inspect.core.ui.components.map.marker.utils.noteSnippet

internal fun buildBrowseMapMarkers(
  inspectionReports: List<InspectionReport>,
  currentLocationPoint: MapPoint?
): List<OsmMarkerSpec> {
  return buildList {
    addNoteMarkers(inspectionReports = inspectionReports)

    currentLocationPoint?.let { point ->
      addCurrentLocationMarker(point = point)
    }
  }
}

internal fun buildMiniMapMarkers(
  selectedPoint: MapPoint?,
  currentLocationPoint: MapPoint?
): List<OsmMarkerSpec> {
  return buildList {
    currentLocationPoint?.let { point ->
      addCurrentLocationMarker(point = point)
    }

    selectedPoint?.let { point ->
      addSelectedLocationMarker(point = point)
    }
  }
}

private fun MutableList<OsmMarkerSpec>.addNoteMarkers(
  inspectionReports: List<InspectionReport>
) {
  inspectionReports.forEach { note ->
    add(
      OsmMarkerSpec(
        id = noteMarkerId(note.id),
        point = MapPoint(
          latitude = note.latitude,
          longitude = note.longitude
        ),
        kind = OsmMarkerKind.Note,
        title = note.title,
        snippet = noteSnippet(note.content),
        clickable = true
      )
    )
  }
}

private fun MutableList<OsmMarkerSpec>.addCurrentLocationMarker(
  point: MapPoint
) {
  add(
    OsmMarkerSpec(
      id = CURRENT_LOCATION_MARKER_ID,
      point = point,
      kind = OsmMarkerKind.Current,
      title = "Current location"
    )
  )
}

private fun MutableList<OsmMarkerSpec>.addSelectedLocationMarker(
  point: MapPoint
) {
  add(
    OsmMarkerSpec(
      id = SELECTED_LOCATION_MARKER_ID,
      point = point,
      kind = OsmMarkerKind.Selected,
      title = "Selected location"
    )
  )
}