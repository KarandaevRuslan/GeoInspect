package com.karandaev.geo_inspect.feature.ui.components.detection_preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.karandaev.geo_inspect.core.domain.model.detection.Detection
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.dialog.DetectionPreviewDialog
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.dialog.DetectionPreviewLandscapeDialog
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.state.rememberDetectionPreviewState

/**
 * Dialog detection preview for inspection report image.
 *
 * This composable resolves preview state from direct display inputs only:
 * - detection list;
 * - image path.
 *
 * It does not resolve report ids and does not extract detections from reports.
 *
 * @param detections Detections to draw over the image.
 * @param imagePath Path to image displayed in dialog.
 * @param orientation Dialog layout mode.
 * @param onDismissRequest Called when dialog should be dismissed.
 * @param modifier Modifier applied to dialog card.
 */
@Composable
fun InspectionReportDetectionPreviewDialog(
  detections: List<Detection>,
  imagePath: String?,
  orientation: DetectionPreviewDialogOrientation = DetectionPreviewDialogOrientation.Portrait,
  onDismissRequest: () -> Unit,
  modifier: Modifier = Modifier
) {
  val previewState = rememberDetectionPreviewState(
    detections = detections,
    imagePath = imagePath
  )

  when (orientation) {
    DetectionPreviewDialogOrientation.Portrait -> {
      DetectionPreviewDialog(
        state = previewState,
        onDismissRequest = onDismissRequest,
        modifier = modifier
      )
    }

    DetectionPreviewDialogOrientation.Landscape -> {
      DetectionPreviewLandscapeDialog(
        state = previewState,
        onDismissRequest = onDismissRequest,
        modifier = modifier
      )
    }
  }
}