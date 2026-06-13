package com.karandaev.geo_inspect.feature.ui.components.detection_preview.card

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.karandaev.geo_inspect.R

/**
 * Status text under detection preview.
 *
 * @param hasImage Whether report image exists.
 * @param hasDetections Whether report has detections.
 */
@Composable
internal fun DetectionPreviewStatusText(
  hasImage: Boolean,
  hasDetections: Boolean
) {
  val text = when {
    !hasDetections -> {
      stringResource(R.string.inspection_report_detection_preview_no_detections)
    }

    !hasImage -> {
      stringResource(R.string.inspection_report_detection_preview_no_image)
    }

    else -> {
      stringResource(R.string.inspection_report_detection_preview_ready)
    }
  }

  Text(
    text = text,
    style = MaterialTheme.typography.bodySmall,
    color = MaterialTheme.colorScheme.onSurfaceVariant
  )
}