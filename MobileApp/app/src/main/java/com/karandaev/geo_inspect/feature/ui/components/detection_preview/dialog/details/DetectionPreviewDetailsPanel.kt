package com.karandaev.geo_inspect.feature.ui.components.detection_preview.dialog.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.dialog.DetectionPreviewDetailsSectionSpacing
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.state.DetectionPreviewState

/**
 * Details panel for landscape detection preview dialog.
 *
 * Shows compact image metrics, detected boxes list and close action.
 */
@Composable
internal fun DetectionPreviewDetailsPanel(
  state: DetectionPreviewState,
  onDismissRequest: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier.verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(DetectionPreviewDetailsSectionSpacing)
  ) {
    DetectionPreviewDetailsHeader(
      detectionsCount = state.detections.size,
      onDismissRequest = onDismissRequest
    )

    DetectionPreviewImageDetails(
      state = state
    )

    DetectionPreviewBoxesDetails(
      detections = state.detections,
      imageSize = state.imageSize
    )
  }
}