package com.karandaev.geo_inspect.feature.ui.components.detection_preview.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.dialog.details.DetectionPreviewDetailsPanel
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.preview.DetectionPreviewImageBox
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.state.DetectionPreviewState

/**
 * Landscape dialog version of detection preview.
 *
 * Displays enlarged image preview on the left and detection details on the right.
 * Columns use fixed 0.4 / 0.6 width ratio.
 *
 * @param state Detection preview state.
 * @param onDismissRequest Called when dialog should be dismissed.
 * @param modifier Modifier applied to dialog card.
 */
@Composable
internal fun DetectionPreviewLandscapeDialog(
  state: DetectionPreviewState,
  onDismissRequest: () -> Unit,
  modifier: Modifier = Modifier
) {
  val cardInteractionSource = remember {
    MutableInteractionSource()
  }

  DetectionPreviewDialogContainer(
    onDismissRequest = onDismissRequest
  ) {
    val cardWidth = maxWidth * DetectionPreviewLandscapeDialogWidthFraction
    val cardMaxHeight = maxHeight * DetectionPreviewLandscapeDialogMaxHeightFraction

    val cardContentWidth = cardWidth -
      DetectionPreviewDialogContentPadding * 2 -
      DetectionPreviewDialogContentSpacing

    val cardContentMaxHeight = cardMaxHeight -
      DetectionPreviewDialogContentPadding * 2

    val imageAreaWidth = cardContentWidth * DetectionPreviewLandscapeImageWeight
    val previewHeightByAspectRatio = imageAreaWidth / state.imageAspectRatio

    val previewHeight = minOf(
      previewHeightByAspectRatio,
      cardContentMaxHeight
    )

    Card(
      modifier = modifier
        .width(cardWidth)
        .heightIn(max = cardMaxHeight)
        .clickable(
          interactionSource = cardInteractionSource,
          indication = null,
          onClick = {}
        )
    ) {
      Row(
        modifier = Modifier
          .padding(DetectionPreviewDialogContentPadding)
          .height(previewHeight),
        horizontalArrangement = Arrangement.spacedBy(DetectionPreviewDialogContentSpacing),
        verticalAlignment = Alignment.CenterVertically
      ) {
        DetectionPreviewImageBox(
          state = state,
          previewHeight = previewHeight,
          modifier = Modifier.weight(DetectionPreviewLandscapeImageWeight)
        )

        DetectionPreviewDetailsPanel(
          state = state,
          onDismissRequest = onDismissRequest,
          modifier = Modifier
            .weight(DetectionPreviewLandscapeDetailsWeight)
            .fillMaxHeight()
        )
      }
    }
  }
}