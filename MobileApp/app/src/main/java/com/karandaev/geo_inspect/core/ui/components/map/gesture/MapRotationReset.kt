package com.karandaev.geo_inspect.core.ui.components.map.gesture

import org.osmdroid.views.MapView

internal fun MapView.resetMapRotation() {
  overlays
    .filterIsInstance<ThresholdRotationGestureOverlay>()
    .forEach { overlay ->
      overlay.resetGesture()
    }

  mapOrientation = 0f
  invalidate()
}