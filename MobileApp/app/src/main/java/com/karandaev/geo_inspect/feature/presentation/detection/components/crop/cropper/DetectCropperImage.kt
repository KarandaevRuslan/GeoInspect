package com.karandaev.geo_inspect.feature.presentation.detection.components.crop.cropper

import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.IntSize

/**
 * Decodes image bitmap for cropper and remembers it by image path.
 *
 * @param imagePath Original image file path.
 */
@Composable
internal fun rememberDetectCropperImageBitmap(
  imagePath: String
): ImageBitmap? {
  return remember(imagePath) {
    BitmapFactory.decodeFile(imagePath)?.asImageBitmap()
  }
}

/**
 * Converts [ImageBitmap] dimensions to [IntSize].
 */
internal fun ImageBitmap.toIntSize(): IntSize {
  return IntSize(
    width = width,
    height = height
  )
}