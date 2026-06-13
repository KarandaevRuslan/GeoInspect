package com.karandaev.geo_inspect.core.ui.components.map.camera

import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import com.karandaev.geo_inspect.core.location.mapper.toGeoPoint
import com.karandaev.geo_inspect.core.ui.components.map.gesture.resetMapRotation
import org.osmdroid.views.MapView

/**
 * Applies bounded zoom and moves the map camera to the given point.
 */
internal fun MapView.focusOn(
  point: MapPoint,
  zoom: Double,
  minZoom: Double,
  maxZoom: Double
) {
  val targetZoom = zoom.coerceIn(
    minimumValue = minZoomLevel,
    maximumValue = maxZoomLevel
  )

  controller.stopAnimation(false)
  controller.stopPanning()
  resetMapRotation()

  controller.animateTo(
    point.toGeoPoint(),
    targetZoom,
    350L
  )
}

/**
 * Applies camera zoom limits to this map.
 */
internal fun MapView.applyCameraBounds(
  camera: OsmMapCameraConfig
) {
  applyWorldVerticalBounds(camera)
}

/**
 * Applies the initial camera state.
 */
internal fun MapView.applyInitialCamera(
  camera: OsmMapCameraConfig
) {
  applyCameraBounds(camera)

  controller.setZoom(
    camera.initialZoom.coerceIn(
      minimumValue = minZoomLevel,
      maximumValue = maxZoomLevel
    )
  )

  controller.setCenter(camera.initialCenter.toGeoPoint())
}