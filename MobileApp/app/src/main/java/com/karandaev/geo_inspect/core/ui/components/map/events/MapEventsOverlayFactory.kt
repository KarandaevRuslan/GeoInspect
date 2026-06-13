package com.karandaev.geo_inspect.core.ui.components.map.events

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import com.karandaev.geo_inspect.core.location.mapper.toMapPoint
import com.karandaev.geo_inspect.core.ui.components.map.camera.OsmMapCameraConfig
import com.karandaev.geo_inspect.core.ui.components.map.camera.applyInitialCamera
import com.karandaev.geo_inspect.core.ui.components.map.camera.applyWorldVerticalBounds
import com.karandaev.geo_inspect.core.ui.components.map.controls.model.MapPointHandler
import com.karandaev.geo_inspect.core.ui.components.map.gesture.ThresholdRotationGestureOverlay
import com.karandaev.geo_inspect.core.ui.components.map.gesture.disallowParentInterceptDuringMapGestures
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay

@Composable
internal fun rememberOsmMapView(
  context: Context,
  camera: OsmMapCameraConfig,
  currentOnTap: State<MapPointHandler?>,
  currentOnLongPress: State<MapPointHandler?>
): MapView {
  return remember {
    MapView(context).apply {
      setTileSource(TileSourceFactory.MAPNIK)
      setMultiTouchControls(true)
      disallowParentInterceptDuringMapGestures()

      overlays.add(
        ThresholdRotationGestureOverlay().apply {
          isEnabled = true
        }
      )

      @Suppress("DEPRECATION")
      setBuiltInZoomControls(false)

      applyInitialCamera(camera)
      applyWorldVerticalBounds(camera)

      overlays.add(
        createMapEventsOverlay(
          currentOnTap = currentOnTap,
          currentOnLongPress = currentOnLongPress
        )
      )
    }
  }
}

private fun createMapEventsOverlay(
  currentOnTap: State<MapPointHandler?>,
  currentOnLongPress: State<MapPointHandler?>
): MapEventsOverlay {
  return MapEventsOverlay(
    object : MapEventsReceiver {
      override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
        return handleMapPointEvent(
          geoPoint = p,
          handler = currentOnTap.value
        )
      }

      override fun longPressHelper(p: GeoPoint?): Boolean {
        return handleMapPointEvent(
          geoPoint = p,
          handler = currentOnLongPress.value
        )
      }
    }
  )
}

private fun handleMapPointEvent(
  geoPoint: GeoPoint?,
  handler: MapPointHandler?
): Boolean {
  val point = geoPoint?.toMapPoint() ?: return false
  val eventHandler = handler ?: return false

  eventHandler(point)

  return true
}