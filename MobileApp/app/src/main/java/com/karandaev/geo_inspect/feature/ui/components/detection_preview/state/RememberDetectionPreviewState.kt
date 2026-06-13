package com.karandaev.geo_inspect.feature.ui.components.detection_preview.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.karandaev.geo_inspect.core.domain.model.detection.Detection
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.defaults.DetectionPreviewFallbackAspectRatio
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.image.readImageSize
import java.io.File

/**
 * Creates detection preview state from already prepared display data.
 *
 * This function does not resolve report-specific paths and does not know anything
 * about inspection reports. It only prepares image metadata for rendering.
 *
 * @param detections Detections to display.
 * @param imagePath Optional local image path.
 */
@Composable
internal fun rememberDetectionPreviewState(
  detections: List<Detection>,
  imagePath: String?
): DetectionPreviewState {
  val imageFile = remember(imagePath) {
    imagePath
      ?.takeIf { path -> path.isNotBlank() }
      ?.let { path -> File(path) }
      ?.takeIf { file -> file.exists() && file.isFile }
  }

  val imageSize = remember(imageFile?.absolutePath) {
    imageFile?.readImageSize()
  }

  val imageAspectRatio = imageSize?.let { size ->
    size.width.toFloat() / size.height.toFloat()
  } ?: DetectionPreviewFallbackAspectRatio

  return DetectionPreviewState(
    imageFile = imageFile,
    imageSize = imageSize,
    imageAspectRatio = imageAspectRatio,
    detections = detections
  )
}