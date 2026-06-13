package com.karandaev.geo_inspect.feature.ui.components.detection_preview.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.preview.DetectionPreviewImageBox
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.state.DetectionPreviewState

/**
 * Portrait dialog version of detection preview.
 *
 * Displays only enlarged image area with detection overlay.
 * Both background click and image click close the dialog.
 *
 * @param state Detection preview state.
 * @param onDismissRequest Called when dialog should be dismissed.
 * @param modifier Modifier applied to dialog card.
 */
@Composable
internal fun DetectionPreviewDialog(
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
    val previewWidth = maxWidth * DetectionPreviewDialogWidthFraction
    val previewHeightByAspectRatio = previewWidth / state.imageAspectRatio
    val maxPreviewHeight = maxHeight * DetectionPreviewDialogMaxHeightFraction
    val previewHeight = minOf(
      previewHeightByAspectRatio,
      maxPreviewHeight
    )

    Card(
      modifier = modifier
        .fillMaxWidth(DetectionPreviewDialogWidthFraction)
        .clickable(
          interactionSource = cardInteractionSource,
          indication = null,
          onClick = onDismissRequest
        )
    ) {
      DetectionPreviewImageBox(
        state = state,
        previewHeight = previewHeight
      )
    }
  }
}