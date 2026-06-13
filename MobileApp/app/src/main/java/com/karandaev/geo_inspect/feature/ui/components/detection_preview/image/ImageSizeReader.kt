package com.karandaev.geo_inspect.feature.ui.components.detection_preview.image

import android.graphics.BitmapFactory
import androidx.compose.ui.unit.IntSize
import java.io.File

/**
 * Reads image dimensions without loading the full bitmap.
 */
internal fun File.readImageSize(): IntSize? {
  val options = BitmapFactory.Options().apply {
    inJustDecodeBounds = true
  }

  BitmapFactory.decodeFile(
    absolutePath,
    options
  )

  return if (options.outWidth > 0 && options.outHeight > 0) {
    IntSize(
      width = options.outWidth,
      height = options.outHeight
    )
  } else {
    null
  }
}