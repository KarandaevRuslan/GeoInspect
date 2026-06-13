package com.karandaev.geo_inspect.feature.ui.components.detection_preview.dialog.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.dialog.details.palette.DetectionPreviewPaletteRow
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.dialog.details.palette.rememberDetectionPreviewImagePalette
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.dialog.toFixedText
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.state.DetectionPreviewState

/**
 * Displays compact image-related preview details.
 */
@Composable
internal fun DetectionPreviewImageDetails(
  state: DetectionPreviewState
) {
  val palette = rememberDetectionPreviewImagePalette(
    imageFile = state.imageFile
  )

  DetectionPreviewDetailsSection(
    title = stringResource(R.string.detection_preview_details_image_title)
  ) {
    Column(
      verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      DetectionPreviewDetailsText(
        text = state.imageSize?.let { size ->
          stringResource(
            id = R.string.detection_preview_details_image_size,
            size.width,
            size.height
          )
        } ?: stringResource(R.string.detection_preview_details_image_size_unknown)
      )

      DetectionPreviewDetailsText(
        text = stringResource(
          id = R.string.detection_preview_details_aspect_ratio,
          state.imageAspectRatio.toFixedText()
        )
      )

      DetectionPreviewPaletteRow(
        palette = palette
      )
    }
  }
}