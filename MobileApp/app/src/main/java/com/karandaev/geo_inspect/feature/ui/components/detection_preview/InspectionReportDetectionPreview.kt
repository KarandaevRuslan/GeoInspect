package com.karandaev.geo_inspect.feature.ui.components.detection_preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.karandaev.geo_inspect.core.domain.model.detection.Detection
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.card.DetectionPreviewCard
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.defaults.DetectionPreviewHeight
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.state.rememberDetectionPreviewState

/**
 * Displays detection preview.
 *
 * This component is UI-only:
 * - it does not know about inspection reports;
 * - it does not resolve image paths by report id;
 * - it does not extract detections from domain objects.
 *
 * @param detections Detections to display.
 * @param imagePath Optional local image path.
 * @param modifier Modifier applied to the card.
 * @param previewHeight Height of the image preview area.
 */
@Composable
fun InspectionReportDetectionPreview(
  detections: List<Detection>,
  imagePath: String?,
  modifier: Modifier = Modifier,
  previewHeight: Dp = DetectionPreviewHeight
) {
  val previewState = rememberDetectionPreviewState(
    detections = detections,
    imagePath = imagePath
  )

  DetectionPreviewCard(
    state = previewState,
    previewHeight = previewHeight,
    modifier = modifier
  )
}