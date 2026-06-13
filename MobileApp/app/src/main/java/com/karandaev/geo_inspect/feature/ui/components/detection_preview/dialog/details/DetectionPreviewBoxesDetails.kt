package com.karandaev.geo_inspect.feature.ui.components.detection_preview.dialog.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.core.domain.model.detection.Detection

/**
 * Displays compact detected bounding boxes list.
 */
@Composable
internal fun DetectionPreviewBoxesDetails(
  detections: List<Detection>,
  imageSize: IntSize?
) {
  DetectionPreviewDetailsSection(
    title = stringResource(R.string.detection_preview_details_boxes_title)
  ) {
    if (detections.isEmpty()) {
      DetectionPreviewDetailsText(
        text = stringResource(R.string.detection_preview_details_no_boxes)
      )
      return@DetectionPreviewDetailsSection
    }

    Column(
      verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      detections.forEachIndexed { index, detection ->
        DetectionPreviewBoxItem(
          index = index,
          detection = detection,
          imageSize = imageSize
        )
      }
    }
  }
}

/**
 * Compact item for a single detected box.
 */
@Composable
private fun DetectionPreviewBoxItem(
  index: Int,
  detection: Detection,
  imageSize: IntSize?
) {
  val metrics = rememberDetectionPreviewBoxMetrics(
    detection = detection,
    imageSize = imageSize
  )

  Surface(
    modifier = Modifier.fillMaxWidth(),
    shape = MaterialTheme.shapes.small,
    color = MaterialTheme.colorScheme.surfaceVariant
  ) {
    Column(
      modifier = Modifier.padding(8.dp),
      verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
      Text(
        text = stringResource(
          id = R.string.detection_preview_details_box_compact_title,
          index + 1,
          detection.clazz,
          detection.confidence.toConfidenceText()
        ),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      DetectionPreviewDetailsText(
        text = stringResource(
          id = R.string.detection_preview_details_box_normalized_compact,
          metrics.normalizedBoundsText,
          metrics.normalizedAreaText
        )
      )

      DetectionPreviewDetailsText(
        text = stringResource(
          id = R.string.detection_preview_details_box_absolute_compact,
          metrics.absoluteBoundsText,
          metrics.absoluteAreaText
        )
      )
    }
  }
}