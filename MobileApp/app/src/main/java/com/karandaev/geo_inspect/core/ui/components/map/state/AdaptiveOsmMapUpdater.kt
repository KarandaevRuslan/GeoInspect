package com.karandaev.geo_inspect.core.ui.components.map.state

import androidx.compose.runtime.State
import com.karandaev.geo_inspect.core.ui.components.map.camera.OsmMapCameraConfig
import com.karandaev.geo_inspect.core.ui.components.map.camera.focusOn
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import com.karandaev.geo_inspect.core.location.mapper.toGeoPoint
import com.karandaev.geo_inspect.core.ui.components.map.gesture.resetMapRotation
import com.karandaev.geo_inspect.core.ui.components.map.marker.model.MarkerPalette
import com.karandaev.geo_inspect.core.ui.components.map.marker.model.OsmMarkerSpec
import com.karandaev.geo_inspect.core.ui.components.map.marker.render.renderKey
import com.karandaev.geo_inspect.core.ui.components.map.marker.render.renderMarkers
import org.osmdroid.views.MapView

/**
 * Synchronizes Compose map inputs with the underlying OSMDroid [MapView].
 */
internal fun MapView.updateAdaptiveOsmMap(
  markers: List<OsmMarkerSpec>,
  palette: MarkerPalette,
  camera: OsmMapCameraConfig,
  focusTarget: MapPoint?,
  focusRequest: Int,
  centerOnMarkerChange: Boolean,
  currentOnMarkerClick: State<(String) -> Unit>,
  state: AdaptiveOsmMapState
) {
  val nextMarkersKey = markers.map { it.renderKey() }
  val markersChanged = state.lastMarkersKey.value != nextMarkersKey
  val paletteChanged = state.lastPalette.value != palette
  val focusRequested = state.lastHandledFocusRequest.intValue != focusRequest

  handleCameraUpdate(
    camera = camera,
    focusTarget = focusTarget,
    focusRequest = focusRequest,
    focusRequested = focusRequested,
    markersChanged = markersChanged,
    centerOnMarkerChange = centerOnMarkerChange,
    state = state
  )

  if (markersChanged || paletteChanged) {
    renderMarkers(
      markers = markers,
      palette = palette,
      currentOnMarkerClick = currentOnMarkerClick
    )

    state.lastMarkersKey.value = nextMarkersKey
    state.lastPalette.value = palette
  }
}

private fun MapView.handleCameraUpdate(
  camera: OsmMapCameraConfig,
  focusTarget: MapPoint?,
  focusRequest: Int,
  focusRequested: Boolean,
  markersChanged: Boolean,
  centerOnMarkerChange: Boolean,
  state: AdaptiveOsmMapState
) {
  if (focusRequested) {
    focusTarget?.let { point ->
      focusOn(
        point = point,
        zoom = camera.focusZoom,
        minZoom = camera.minZoom,
        maxZoom = camera.maxZoom
      )

      state.lastHandledFocusRequest.intValue = focusRequest
    }
  } else if (markersChanged && centerOnMarkerChange) {
    focusTarget?.let { point ->
      resetMapRotation()
      controller.setCenter(point.toGeoPoint())
    }
  }
}