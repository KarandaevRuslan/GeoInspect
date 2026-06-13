package com.karandaev.geo_inspect.core.ui.components.map.marker.render

import androidx.compose.runtime.State
import com.karandaev.geo_inspect.core.location.mapper.toGeoPoint
import com.karandaev.geo_inspect.core.ui.components.map.marker.model.MarkerPalette
import com.karandaev.geo_inspect.core.ui.components.map.marker.model.OsmMarkerSpec
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

/**
 * Renders the provided marker set into this [MapView].
 *
 * Existing rendered markers are replaced so the OSMDroid overlay state stays aligned
 * with Compose state.
 */
internal fun MapView.renderMarkers(
  markers: List<OsmMarkerSpec>,
  palette: MarkerPalette,
  currentOnMarkerClick: State<(String) -> Unit>
) {
  closeRenderedMarkerInfoWindows()
  removeRenderedMarkers()

  markers.forEach { spec ->
    overlays.add(createMarker(spec, palette, currentOnMarkerClick))
  }

  invalidate()
}

private fun MapView.closeRenderedMarkerInfoWindows() {
  overlays
    .filterIsInstance<Marker>()
    .forEach { marker -> marker.closeInfoWindow() }
}

private fun MapView.removeRenderedMarkers() {
  overlays.removeAll { overlay -> overlay is Marker }
}

private fun MapView.createMarker(
  spec: OsmMarkerSpec,
  palette: MarkerPalette,
  currentOnMarkerClick: State<(String) -> Unit>
): Marker {
  return Marker(this).apply {
    position = spec.point.toGeoPoint()
    icon = circleMarkerDrawable(
      context = context,
      fillColor = palette.fillColorFor(spec.kind),
      strokeColor = palette.strokeColor,
      sizeDp = spec.sizeDp
    )

    title = spec.title
    subDescription = spec.snippet

    setAnchor(
      Marker.ANCHOR_CENTER,
      Marker.ANCHOR_CENTER
    )

    if (spec.clickable) {
      setOnMarkerClickListener { marker, _ ->
        marker.closeInfoWindow()
        currentOnMarkerClick.value(spec.id)
        true
      }
    }
  }
}