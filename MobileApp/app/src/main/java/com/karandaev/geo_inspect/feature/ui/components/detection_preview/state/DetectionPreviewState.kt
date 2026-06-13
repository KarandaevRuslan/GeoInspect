package com.karandaev.geo_inspect.feature.ui.components.detection_preview.state

import androidx.compose.ui.unit.IntSize
import com.karandaev.geo_inspect.core.domain.model.detection.Detection
import java.io.File

/**
 * UI-ready state for inspection report detection preview.
 *
 * @param imageFile Local report image file, or null when image is missing.
 * @param imageSize Local image dimensions, or null when image size cannot be read.
 * @param imageAspectRatio Aspect ratio used for image fitting and bbox drawing.
 * @param detections Detection list to display.
 */
internal data class DetectionPreviewState(
  val imageFile: File?,
  val imageSize: IntSize?,
  val imageAspectRatio: Float,
  val detections: List<Detection>
) {

  /**
   * Returns whether report image exists in local storage.
   */
  val hasImage: Boolean
    get() = imageFile != null

  /**
   * Returns whether report has detections to display.
   */
  val hasDetections: Boolean
    get() = detections.isNotEmpty()
}