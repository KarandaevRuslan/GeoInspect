package com.karandaev.geo_inspect.feature.ui.components.detection_preview.overlay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.platform.LocalDensity

/**
 * Pixel values used by detection overlay drawing.
 */
@Immutable
internal data class DetectionOverlayStyle(
  val strokePx: Float,
  val labelTextPx: Float,
  val labelHorizontalPaddingPx: Float,
  val labelVerticalPaddingPx: Float,
  val labelSpacingPx: Float
)

/**
 * Remembers detection overlay drawing style in pixels.
 */
@Composable
internal fun rememberDetectionOverlayStyle(): DetectionOverlayStyle {
  val density = LocalDensity.current

  return with(density) {
    DetectionOverlayStyle(
      strokePx = DetectionOverlayStrokeWidth.toPx(),
      labelTextPx = DetectionOverlayLabelTextSize.toPx(),
      labelHorizontalPaddingPx = DetectionOverlayLabelHorizontalPadding.toPx(),
      labelVerticalPaddingPx = DetectionOverlayLabelVerticalPadding.toPx(),
      labelSpacingPx = DetectionOverlayLabelSpacing.toPx()
    )
  }
}