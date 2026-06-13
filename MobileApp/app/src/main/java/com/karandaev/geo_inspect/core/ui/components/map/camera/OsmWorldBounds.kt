package com.karandaev.geo_inspect.core.ui.components.map.camera

import org.osmdroid.views.MapView
import kotlin.math.ln
import kotlin.math.max

/**
 * Applies world bounds that prevent vertical map repetition and vertical overscroll.
 *
 * The minimum zoom is also adjusted so the projected world height is not smaller
 * than the current map viewport height.
 */
internal fun MapView.applyWorldVerticalBounds(
  camera: OsmMapCameraConfig
) {
  setVerticalMapRepetitionEnabled(false)

  setScrollableAreaLimitLatitude(
    MapView.getTileSystem().maxLatitude,
    MapView.getTileSystem().minLatitude,
    0
  )

  val viewportHeightPx = height

  if (viewportHeightPx <= 0) {
    post {
      applyWorldVerticalBounds(camera)
    }
    return
  }

  minZoomLevel = max(
    camera.minZoom,
    worldFitMinZoom(viewportHeightPx = viewportHeightPx)
  )

  maxZoomLevel = camera.maxZoom

  if (zoomLevelDouble < minZoomLevel) {
    controller.setZoom(minZoomLevel)
  }
}

/**
 * Calculates the minimum zoom where the full projected world height still fills
 * the current viewport height.
 */
private fun MapView.worldFitMinZoom(
  viewportHeightPx: Int
): Double {
  val tileSizePx = tileProvider.tileSource.tileSizePixels.toDouble()
  val rawZoom = ln(viewportHeightPx / tileSizePx) / ln(2.0)

  return rawZoom.coerceAtLeast(0.0)
}