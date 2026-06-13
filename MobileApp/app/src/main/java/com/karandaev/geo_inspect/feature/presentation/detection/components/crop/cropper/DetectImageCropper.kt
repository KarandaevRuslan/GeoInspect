package com.karandaev.geo_inspect.feature.presentation.detection.components.crop.cropper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.core.image.crop.NormalizedCropBox
import com.karandaev.geo_inspect.core.image.source_file.ImageSourceFile

private val DetectImageCropperMaxWidth = 320.dp
private val DetectImageCropperHeight = 220.dp

/**
 * Image cropper for detection images.
 *
 * The user moves and zooms the image behind a fixed rectangular viewport.
 *
 * @param imageSource Selected image source.
 * @param enabled Whether gestures are enabled.
 * @param onCropBoxChange Called when crop box changes.
 * @param modifier Modifier applied to the root.
 */
@Composable
internal fun DetectImageCropper(
  imageSource: ImageSourceFile,
  enabled: Boolean,
  onCropBoxChange: (NormalizedCropBox?) -> Unit,
  modifier: Modifier = Modifier
) {
  val imagePath = imageSource.originalSourcePath
  val imageBitmap = rememberDetectCropperImageBitmap(imagePath)

  if (imageBitmap == null) {
    Text(
      text = stringResource(R.string.detect_cropper_image_load_error),
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.error
    )
    return
  }

  val imageSize = imageBitmap.toIntSize()
  val cropperState = rememberDetectCropperState(imagePath)
  val contentDescription = stringResource(R.string.detect_cropper_content_description)

  var viewportSize by remember(imagePath) {
    mutableStateOf(IntSize.Zero)
  }

  var restoredViewportSize by remember(imagePath) {
    mutableStateOf(IntSize.Zero)
  }

  LaunchedEffect(
    imagePath,
    viewportSize
  ) {
    if (!viewportSize.isValid() || restoredViewportSize == viewportSize) {
      return@LaunchedEffect
    }

    val cropBox = imageSource.cropBox

    if (cropBox == null) {
      cropperState.reset()
    } else {
      cropperState.restoreFromCropBox(
        cropBox = cropBox,
        imageSize = imageSize,
        viewportSize = viewportSize
      )
    }

    restoredViewportSize = viewportSize
  }

  LaunchedEffect(
    imagePath,
    imageSource.cropBox
  ) {
    if (imageSource.cropBox == null) {
      cropperState.reset()
    }
  }

  LaunchedEffect(
    imagePath,
    viewportSize,
    cropperState.scale,
    cropperState.offset
  ) {
    if (viewportSize.isValid()) {
      onCropBoxChange(
        calculateDetectCropBox(
          imageSize = imageSize,
          viewportSize = viewportSize,
          scale = cropperState.scale,
          offset = cropperState.offset
        )
      )
    }
  }

  Column(
    modifier = modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(DetectCropperContentSpacing)
  ) {
    DetectCropperCanvas(
      imageBitmap = imageBitmap,
      imageSize = imageSize,
      cropperState = cropperState,
      enabled = enabled,
      contentDescription = contentDescription,
      onViewportSizeChange = { size ->
        viewportSize = size
      },
      modifier = Modifier
        .widthIn(max = DetectImageCropperMaxWidth)
        .fillMaxWidth()
        .height(DetectImageCropperHeight)
    )

    Text(
      text = stringResource(R.string.detect_cropper_gesture_hint),
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}