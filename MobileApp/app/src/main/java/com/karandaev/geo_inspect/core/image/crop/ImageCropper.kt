package com.karandaev.geo_inspect.core.image.crop

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.karandaev.geo_inspect.core.image.cache.CroppedImageCacheDirectoryName
import com.karandaev.geo_inspect.core.image.cache.CroppedImageFilePrefix
import com.karandaev.geo_inspect.core.image.source_file.ImageSourceFile
import java.io.File
import java.io.FileOutputStream
import kotlin.math.roundToInt

private const val CroppedFileExtension = "jpg"
private const val CropJpegQuality = 95

/**
 * Creates cropped image files from local source images.
 */
class ImageCropper {

  fun crop(
    context: Context,
    imageSource: ImageSourceFile,
    cropBox: NormalizedCropBox
  ): ImageSourceFile {
    val bitmap = BitmapFactory.decodeFile(imageSource.originalSourcePath)
      ?: error("Could not decode source image.")

    try {
      val cropRect = cropBox.toPixelRect(
        imageWidth = bitmap.width,
        imageHeight = bitmap.height
      )

      val croppedBitmap = Bitmap.createBitmap(
        bitmap,
        cropRect.x,
        cropRect.y,
        cropRect.width,
        cropRect.height
      )

      try {
        val directory = File(
          context.cacheDir,
          CroppedImageCacheDirectoryName
        ).apply {
          mkdirs()
        }

        val croppedFile = File(
          directory,
          "$CroppedImageFilePrefix${System.currentTimeMillis()}.$CroppedFileExtension"
        )

        FileOutputStream(croppedFile).use { output ->
          val isCompressed = croppedBitmap.compress(
            Bitmap.CompressFormat.JPEG,
            CropJpegQuality,
            output
          )

          check(isCompressed) {
            "Could not save cropped image."
          }
        }

        return imageSource.copy(
          sourcePath = croppedFile.absolutePath,
          cropBox = cropBox
        )
      } finally {
        croppedBitmap.recycle()
      }
    } finally {
      bitmap.recycle()
    }
  }

  private fun NormalizedCropBox.toPixelRect(
    imageWidth: Int,
    imageHeight: Int
  ): PixelCropRect {
    val left = (xMin * imageWidth).roundToInt().coerceIn(0, imageWidth - 1)
    val top = (yMin * imageHeight).roundToInt().coerceIn(0, imageHeight - 1)
    val right = (xMax * imageWidth).roundToInt().coerceIn(left + 1, imageWidth)
    val bottom = (yMax * imageHeight).roundToInt().coerceIn(top + 1, imageHeight)

    return PixelCropRect(
      x = left,
      y = top,
      width = right - left,
      height = bottom - top
    )
  }

  private data class PixelCropRect(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int
  )
}