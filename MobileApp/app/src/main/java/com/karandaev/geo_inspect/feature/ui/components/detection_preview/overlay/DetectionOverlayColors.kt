package com.karandaev.geo_inspect.feature.ui.components.detection_preview.overlay

import androidx.compose.ui.graphics.Color
import com.karandaev.geo_inspect.core.domain.model.detection.Detection
import java.util.Locale

/**
 * Returns deterministic color for detection class.
 *
 * @param clazz Detection class label.
 */
internal fun detectionClassColor(
  clazz: String
): Color {
  val hue = (clazz.hashCode() and 0xFFFF) % 360
  val hsv = floatArrayOf(
    hue.toFloat(),
    0.85f,
    0.90f
  )

  return Color(
    android.graphics.Color.HSVToColor(hsv)
  )
}

/**
 * Formats detection label.
 */
internal fun Detection.toLabel(): String {
  return String.format(
    Locale.US,
    "%s %.2f",
    clazz,
    confidence
  )
}