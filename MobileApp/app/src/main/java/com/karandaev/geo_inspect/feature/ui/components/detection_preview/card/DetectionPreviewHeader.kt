package com.karandaev.geo_inspect.feature.ui.components.detection_preview.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R

/**
 * Header for detection preview card.
 *
 * @param detectionsCount Number of detections in report.
 */
@Composable
internal fun DetectionPreviewHeader(
  detectionsCount: Int
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(2.dp)
  ) {
    Text(
      text = stringResource(R.string.inspection_report_detection_preview_title),
      style = MaterialTheme.typography.titleMedium,
      color = MaterialTheme.colorScheme.onSurface
    )

    Text(
      text = stringResource(
        R.string.inspection_report_detection_preview_count,
        detectionsCount
      ),
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}