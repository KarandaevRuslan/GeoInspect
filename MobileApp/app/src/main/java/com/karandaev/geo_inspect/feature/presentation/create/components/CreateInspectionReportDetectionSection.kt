package com.karandaev.geo_inspect.feature.presentation.create.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.core.domain.model.detection.DetectResponse
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.DetectionPreviewDialogOrientation
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.InspectionReportDetectionPreviewWithDialog

/**
 * Detection section for create/edit report screen.
 *
 * Displays accepted detection result and allows user to open, preview or clear detection flow.
 */
@Composable
internal fun CreateInspectionReportDetectionSection(
  detectionImagePath: String?,
  detectResponse: DetectResponse?,
  isDetectionPreviewDialogVisible: Boolean,
  detectionPreviewDialogOrientation: DetectionPreviewDialogOrientation,
  onDetectionPreviewClick: () -> Unit,
  onDismissDetectionPreviewDialogRequest: () -> Unit,
  onDetectClick: () -> Unit,
  onClearDetectionClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val hasDetectionResult = detectResponse != null ||
    !detectionImagePath.isNullOrBlank()

  Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Text(
      text = stringResource(R.string.create_detection_section_title),
      style = MaterialTheme.typography.labelLarge,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    if (hasDetectionResult) {
      InspectionReportDetectionPreviewWithDialog(
        detections = detectResponse?.detections.orEmpty(),
        imagePath = detectionImagePath,
        isDialogVisible = isDetectionPreviewDialogVisible,
        dialogOrientation = detectionPreviewDialogOrientation,
        onPreviewClick = onDetectionPreviewClick,
        onDismissDialogRequest = onDismissDetectionPreviewDialogRequest
      )

      OutlinedButton(
        onClick = onDetectClick,
        modifier = Modifier.fillMaxWidth()
      ) {
        Icon(
          imageVector = Icons.Default.ImageSearch,
          contentDescription = null,
          modifier = Modifier.size(18.dp)
        )

        Text(
          text = stringResource(R.string.create_detection_run_again),
          modifier = Modifier.padding(start = 8.dp)
        )
      }

      OutlinedButton(
        onClick = onClearDetectionClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(
          contentColor = MaterialTheme.colorScheme.error
        )
      ) {
        Icon(
          imageVector = Icons.Default.Delete,
          contentDescription = null,
          modifier = Modifier.size(18.dp)
        )

        Text(
          text = stringResource(R.string.create_detection_clear),
          modifier = Modifier.padding(start = 8.dp)
        )
      }
    } else {
      Button(
        onClick = onDetectClick,
        modifier = Modifier.fillMaxWidth()
      ) {
        Icon(
          imageVector = Icons.Default.ImageSearch,
          contentDescription = null,
          modifier = Modifier.size(18.dp)
        )

        Text(
          text = stringResource(R.string.create_detection_add),
          modifier = Modifier.padding(start = 8.dp)
        )
      }
    }
  }
}