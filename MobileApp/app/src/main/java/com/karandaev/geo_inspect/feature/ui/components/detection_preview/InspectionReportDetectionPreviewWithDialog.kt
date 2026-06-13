package com.karandaev.geo_inspect.feature.ui.components.detection_preview

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.karandaev.geo_inspect.core.domain.model.detection.Detection
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.defaults.DetectionPreviewHeight

/**
 * Controlled detection preview with optional dialog.
 *
 * This composable does not own dialog visibility state.
 * Caller decides when dialog is visible and how it is dismissed.
 *
 * @param detections Detections to display.
 * @param imagePath Optional local image path.
 * @param isDialogVisible Whether enlarged dialog should be shown.
 * @param dialogOrientation Dialog layout mode.
 * @param onPreviewClick Called when preview card is clicked.
 * @param onDismissDialogRequest Called when dialog should be dismissed.
 * @param modifier Modifier applied to the preview card.
 * @param previewHeight Height of the image preview area.
 */
@Composable
fun InspectionReportDetectionPreviewWithDialog(
  detections: List<Detection>,
  imagePath: String?,
  isDialogVisible: Boolean,
  dialogOrientation: DetectionPreviewDialogOrientation,
  onPreviewClick: () -> Unit,
  onDismissDialogRequest: () -> Unit,
  modifier: Modifier = Modifier,
  previewHeight: Dp = DetectionPreviewHeight
) {
  InspectionReportDetectionPreview(
    detections = detections,
    imagePath = imagePath,
    previewHeight = previewHeight,
    modifier = modifier.clickable(
      onClick = onPreviewClick
    )
  )

  if (isDialogVisible) {
    InspectionReportDetectionPreviewDialog(
      detections = detections,
      imagePath = imagePath,
      orientation = dialogOrientation,
      onDismissRequest = onDismissDialogRequest
    )
  }
}