package com.karandaev.geo_inspect.feature.ui.components.detection_preview.dialog.details

import java.util.Locale
import kotlin.math.roundToInt

/**
 * Formats normalized coordinate or normalized area.
 */
internal fun Double.toFixedText(): String {
  return String.format(
    Locale.getDefault(),
    "%.3f",
    this
  )
}

/**
 * Formats confidence value as percentage.
 */
internal fun Double.toConfidenceText(): String {
  return "${(this * 100.0).roundToInt()}%"
}

/**
 * Formats aspect ratio value.
 */
internal fun Float.toFixedText(): String {
  return String.format(
    Locale.getDefault(),
    "%.2f",
    this
  )
}

/**
 * Formats area in pixels.
 */
internal fun Int.toPixelAreaText(): String {
  return String.format(
    Locale.getDefault(),
    "%,d px²",
    this
  )
}