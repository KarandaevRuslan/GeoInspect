package com.karandaev.geo_inspect.feature.ui.components.detection_preview.dialog.details.palette

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.cancellation.CancellationException

private const val DetectionPreviewPaletteMaxBitmapSize = 320
private const val DetectionPreviewPaletteMaxColors = 6

/**
 * Extracts representative color palette from image file.
 *
 * Palette extraction runs off the main thread.
 *
 * @param imageFile Image file to analyze.
 */
@Composable
internal fun rememberDetectionPreviewImagePalette(
  imageFile: File?
): DetectionPreviewImagePalette? {
  val imagePath = imageFile?.absolutePath

  val paletteState = produceState<DetectionPreviewImagePalette?>(
    initialValue = null,
    key1 = imagePath
  ) {
    value = withContext(Dispatchers.Default) {
      imageFile?.extractDetectionPreviewImagePalette()
    }
  }

  return paletteState.value
}

/**
 * Extracts palette from this image file.
 */
private fun File.extractDetectionPreviewImagePalette(): DetectionPreviewImagePalette? {
  return try {
    val bitmap = decodeSampledBitmap() ?: return null

    try {
      val palette = Palette
        .from(bitmap)
        .maximumColorCount(DetectionPreviewPaletteMaxColors)
        .generate()

      DetectionPreviewImagePalette(
        colors = palette.swatches
          .sortedByDescending { swatch -> swatch.population }
          .take(DetectionPreviewPaletteMaxColors)
          .map { swatch ->
            DetectionPreviewPaletteColor(
              colorArgb = swatch.rgb,
              hexText = swatch.rgb.toHexColorText()
            )
          }
      )
    } finally {
      bitmap.recycle()
    }
  } catch (error: CancellationException) {
    throw error
  } catch (_: Throwable) {
    null
  }
}

/**
 * Decodes scaled bitmap for palette extraction.
 */
private fun File.decodeSampledBitmap(): Bitmap? {
  val boundsOptions = BitmapFactory.Options().apply {
    inJustDecodeBounds = true
  }

  BitmapFactory.decodeFile(
    absolutePath,
    boundsOptions
  )

  val decodeOptions = BitmapFactory.Options().apply {
    inSampleSize = boundsOptions.calculateInSampleSize()
  }

  return BitmapFactory.decodeFile(
    absolutePath,
    decodeOptions
  )
}

/**
 * Calculates sample size to keep palette bitmap small.
 */
private fun BitmapFactory.Options.calculateInSampleSize(): Int {
  var sampleSize = 1

  while (
    outWidth / sampleSize > DetectionPreviewPaletteMaxBitmapSize ||
    outHeight / sampleSize > DetectionPreviewPaletteMaxBitmapSize
  ) {
    sampleSize *= 2
  }

  return sampleSize
}

/**
 * Formats ARGB color as RGB HEX.
 */
private fun Int.toHexColorText(): String {
  return "#%06X".format(0xFFFFFF and this)
}