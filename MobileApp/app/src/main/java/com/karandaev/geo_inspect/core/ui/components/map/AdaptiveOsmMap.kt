package com.karandaev.geo_inspect.core.ui.components.map

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import com.karandaev.geo_inspect.core.util.mappers.toMapPoint
import com.karandaev.geo_inspect.core.location.LocationDefaults
import com.karandaev.geo_inspect.core.ui.components.map.camera.OsmMapCameraConfig
import com.karandaev.geo_inspect.core.ui.components.map.camera.focusOn
import com.karandaev.geo_inspect.core.ui.components.map.controls.model.OsmMapControls
import com.karandaev.geo_inspect.core.ui.components.map.controls.model.OsmMapInteractions
import com.karandaev.geo_inspect.core.ui.components.map.controls.focus_menu.model.buildOsmFocusMenuItems
import com.karandaev.geo_inspect.core.ui.components.map.controls.ui.MapFocusButton
import com.karandaev.geo_inspect.core.ui.components.map.controls.focus_menu.ui.MapFocusMenuOverlay
import com.karandaev.geo_inspect.core.ui.components.map.controls.ui.MapLoadingOverlay
import com.karandaev.geo_inspect.core.ui.components.map.controls.ui.MapZoomControls
import com.karandaev.geo_inspect.core.ui.components.map.events.rememberOsmMapView
import com.karandaev.geo_inspect.core.ui.components.map.lifecycle.MapLifecycleEffect
import com.karandaev.geo_inspect.core.ui.components.map.marker.model.OsmMarkerSpec
import com.karandaev.geo_inspect.core.ui.components.map.marker.style.markerPalette
import com.karandaev.geo_inspect.core.ui.components.map.state.rememberAdaptiveOsmMapState
import com.karandaev.geo_inspect.core.ui.components.map.state.updateAdaptiveOsmMap
import com.karandaev.geo_inspect.core.ui.components.map.viewport.MapContainer
import com.karandaev.geo_inspect.core.ui.components.map.viewport.OsmMapViewport
import com.karandaev.geo_inspect.core.ui.components.map.viewport.mapControlMetricsForViewport
import org.osmdroid.views.MapView

private const val ZOOM_ANIMATION_DURATION_MILLIS = 150L

private fun MapView.zoomInByStep() {
  val nextZoom = (zoomLevelDouble + 1.0)
    .coerceAtMost(maxZoomLevel)

  controller.zoomTo(nextZoom, ZOOM_ANIMATION_DURATION_MILLIS)
}

private fun MapView.zoomOutByStep() {
  val nextZoom = (zoomLevelDouble - 1.0)
    .coerceAtLeast(minZoomLevel)

  controller.zoomTo(nextZoom, ZOOM_ANIMATION_DURATION_MILLIS)
}

/**
 * Universal Compose wrapper around an OSMDroid map.
 *
 * This composable is the high-level entry point for map rendering. It wires together
 * the OSMDroid [MapView], Compose layout, marker rendering, camera updates, lifecycle
 * handling, map gestures, loading state, and optional overlay controls.
 *
 * Feature-specific behavior should be passed through configuration models and callbacks
 * instead of creating separate map composables for each screen.
 *
 * @param markers Marker descriptions that should be rendered on the map.
 * @param modifier Modifier applied to the outer map container.
 * @param viewport Layout mode that defines how the map is sized and decorated.
 * @param camera Initial camera position, zoom bounds, and focus zoom configuration.
 * @param controls Optional overlay control configuration, such as zoom and focus buttons.
 * @param interactions Map interaction callbacks for taps, long presses, and marker clicks.
 * @param focusTarget Optional point used by focus requests and the default focus button action.
 * If null, the focus button falls back to [OsmMapCameraConfig.initialCenter].
 * @param focusRequest Monotonic value that triggers a camera focus when changed.
 * @param centerOnMarkerChange Whether the map should recenter to [focusTarget] after marker changes.
 * @param isLoading Whether to display a loading overlay above the map.
 * @param mapRef Callback that exposes the underlying OSMDroid [MapView] for interop scenarios.
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun AdaptiveOsmMap(
  markers: List<OsmMarkerSpec>,
  modifier: Modifier = Modifier,
  viewport: OsmMapViewport = OsmMapViewport.Fill,
  camera: OsmMapCameraConfig = OsmMapCameraConfig(
    initialCenter = LocationDefaults.location.toMapPoint()
  ),
  controls: OsmMapControls = OsmMapControls(),
  interactions: OsmMapInteractions = OsmMapInteractions(),
  focusTarget: MapPoint? = null,
  focusRequest: Int = 0,
  centerOnMarkerChange: Boolean = false,
  isLoading: Boolean = false,
  mapRef: (MapView) -> Unit = {}
) {
  val context = LocalContext.current

  val currentOnTap = rememberUpdatedState(interactions.onTap)
  val currentOnLongPress = rememberUpdatedState(interactions.onLongPress)
  val currentOnMarkerClick = rememberUpdatedState(interactions.onMarkerClick)
  val currentMapRef = rememberUpdatedState(mapRef)

  val palette = markerPalette()
  val mapState = rememberAdaptiveOsmMapState(focusRequest = focusRequest)

  var isFocusMenuVisible by remember { mutableStateOf(false) }

  val mapView = rememberOsmMapView(
    context = context,
    camera = camera,
    currentOnTap = currentOnTap,
    currentOnLongPress = currentOnLongPress
  )

  LaunchedEffect(mapView) {
    currentMapRef.value(mapView)
  }

  BoxWithConstraints(
    modifier = modifier
  ) {
    val metrics = mapControlMetricsForViewport(
      viewport = viewport,
      maxWidth = maxWidth,
      maxHeight = maxHeight
    )

    MapContainer(
      viewport = viewport,
      modifier = Modifier.fillMaxSize()
    ) {
      AndroidView(
        factory = { mapView },
        modifier = Modifier.fillMaxSize(),
        update = { view ->
          currentMapRef.value(view)

          view.updateAdaptiveOsmMap(
            markers = markers,
            palette = palette,
            camera = camera,
            focusTarget = focusTarget,
            focusRequest = focusRequest,
            centerOnMarkerChange = centerOnMarkerChange,
            currentOnMarkerClick = currentOnMarkerClick,
            state = mapState
          )
        }
      )

      if (isLoading) {
        MapLoadingOverlay(metrics = metrics)
      }

      if (controls.showZoomControls) {
        MapZoomControls(
          metrics = metrics,
          onZoomIn = {
            mapView.zoomInByStep()
          },
          onZoomOut = {
            mapView.zoomOutByStep()
          }
        )
      }

      val focusButtonTarget = focusTarget ?: camera.initialCenter

      if (controls.showFocusButton) {
        MapFocusButton(
          text = controls.focusButtonText,
          metrics = metrics,
          onClick = {
            mapView.focusOnDefaultTarget(
              point = focusButtonTarget,
              camera = camera
            )

            controls.onFocusClick?.invoke()
          },
          onLongClick = {
            isFocusMenuVisible = true
          }
        )
      }

      if (isFocusMenuVisible) {
        MapFocusMenuOverlay(
          items = buildOsmFocusMenuItems(
            focusTarget = focusTarget,
            initialCenter = camera.initialCenter,
            markers = markers
          ),
          showSearch = viewport is OsmMapViewport.Fill,
          onItemClick = { item ->
            isFocusMenuVisible = false

            mapView.focusOnDefaultTarget(
              point = item.point,
              camera = camera
            )

            controls.onFocusClick?.invoke()
          },
          onDismiss = {
            isFocusMenuVisible = false
          }
        )
      }
    }
  }

  MapLifecycleEffect(mapView = mapView)
}

private fun MapView.focusOnDefaultTarget(
  point: MapPoint,
  camera: OsmMapCameraConfig
) {
  focusOn(
    point = point,
    zoom = camera.focusZoom,
    minZoom = camera.minZoom,
    maxZoom = camera.maxZoom
  )
}