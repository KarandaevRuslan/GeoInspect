package com.karandaev.geo_inspect.feature.ui.components.detection_preview.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.preview.DetectionPreviewImageBox
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.defaults.DetectionPreviewContentPadding
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.defaults.DetectionPreviewContentSpacing
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.state.DetectionPreviewState

/**
 * Card that displays detection preview header, image area and status.
 *
 * @param state Detection preview state.
 * @param previewHeight Height of image preview area.
 * @param modifier Modifier applied to the card.
 */
@Composable
internal fun DetectionPreviewCard(
  state: DetectionPreviewState,
  previewHeight: Dp,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier.padding(DetectionPreviewContentPadding),
      verticalArrangement = Arrangement.spacedBy(DetectionPreviewContentSpacing)
    ) {
      DetectionPreviewHeader(
        detectionsCount = state.detections.size
      )

      DetectionPreviewImageBox(
        state = state,
        previewHeight = previewHeight
      )

      DetectionPreviewStatusText(
        hasImage = state.hasImage,
        hasDetections = state.hasDetections
      )
    }
  }
}