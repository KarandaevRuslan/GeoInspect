package com.karandaev.geo_inspect.feature.ui.components.detection_preview.dialog.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntSize
import com.karandaev.geo_inspect.core.domain.model.detection.Detection
import kotlin.math.roundToInt

/**
 * UI-ready compact metrics for one detection bounding box.
 */
internal data class DetectionPreviewBoxMetrics(
  val normalizedBoundsText: String,
  val normalizedAreaText: String,
  val absoluteBoundsText: String,
  val absoluteAreaText: String
)

/**
 * Creates compact UI-ready metrics for detection box.
 */
@Composable
internal fun rememberDetectionPreviewBoxMetrics(
  detection: Detection,
  imageSize: IntSize?
): DetectionPreviewBoxMetrics {
  return remember(
    detection,
    imageSize
  ) {
    createDetectionPreviewBoxMetrics(
      detection = detection,
      imageSize = imageSize
    )
  }
}

/**
 * Calculates normalized and absolute detection box metrics.
 */
private fun createDetectionPreviewBoxMetrics(
  detection: Detection,
  imageSize: IntSize?
): DetectionPreviewBoxMetrics {
  val bbox = detection.bbox

  val normalizedWidth = bbox.xMax - bbox.xMin
  val normalizedHeight = bbox.yMax - bbox.yMin
  val normalizedArea = normalizedWidth * normalizedHeight

  val normalizedBoundsText = "x ${bbox.xMin.toFixedText()}–${bbox.xMax.toFixedText()}, " +
    "y ${bbox.yMin.toFixedText()}–${bbox.yMax.toFixedText()}"

  if (imageSize == null) {
    return DetectionPreviewBoxMetrics(
      normalizedBoundsText = normalizedBoundsText,
      normalizedAreaText = normalizedArea.toFixedText(),
      absoluteBoundsText = "—",
      absoluteAreaText = "—"
    )
  }

  val absoluteXMin = (bbox.xMin * imageSize.width).roundToInt()
  val absoluteXMax = (bbox.xMax * imageSize.width).roundToInt()
  val absoluteYMin = (bbox.yMin * imageSize.height).roundToInt()
  val absoluteYMax = (bbox.yMax * imageSize.height).roundToInt()

  val absoluteWidth = absoluteXMax - absoluteXMin
  val absoluteHeight = absoluteYMax - absoluteYMin
  val absoluteArea = absoluteWidth * absoluteHeight

  return DetectionPreviewBoxMetrics(
    normalizedBoundsText = normalizedBoundsText,
    normalizedAreaText = normalizedArea.toFixedText(),
    absoluteBoundsText = "x $absoluteXMin–$absoluteXMax, y $absoluteYMin–$absoluteYMax px",
    absoluteAreaText = absoluteArea.toPixelAreaText()
  )
}