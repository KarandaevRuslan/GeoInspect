package com.karandaev.geo_inspect.feature.ui.components.detection_preview.preview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import coil.compose.AsyncImage
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.placeholder.DetectionPreviewPlaceholder
import java.io.File

/**
 * Displays report image or placeholder when image is missing.
 *
 * @param imageFile Local report image file.
 * @param imageAspectRatio Aspect ratio used to fit placeholder.
 * @param containerSize Preview container size.
 * @param modifier Modifier applied to image area.
 */
@Composable
internal fun DetectionPreviewImage(
  imageFile: File?,
  imageAspectRatio: Float,
  containerSize: IntSize,
  modifier: Modifier = Modifier
) {
  if (imageFile != null) {
    AsyncImage(
      model = imageFile,
      contentDescription = stringResource(
        R.string.inspection_report_detection_preview_image_content_description
      ),
      modifier = modifier.fillMaxSize(),
      contentScale = ContentScale.Fit
    )
  } else {
    DetectionPreviewPlaceholder(
      containerSize = containerSize,
      imageAspectRatio = imageAspectRatio,
      modifier = modifier.fillMaxSize()
    )
  }
}