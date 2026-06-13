package com.karandaev.geo_inspect.core.image.compression

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.graphics.scale
import com.karandaev.geo_inspect.core.image.cache.CompressedImageFilePrefix
import java.io.File
import java.io.FileOutputStream
import kotlin.math.roundToInt

private const val DefaultCompressionQuality = 85
private const val DefaultMaxImageSizePx = 768
private const val CompressedFileExtension = "jpg"

/**
 * Compresses image files into JPEG format.
 *
 * The output image is resized to fit into [DefaultMaxImageSizePx] x [DefaultMaxImageSizePx]
 * while preserving the original aspect ratio.
 *
 * The output file is always created as JPEG, regardless of the source image format.
 * If the source file cannot be decoded as an image, compression is skipped.
 *
 * @param quality JPEG compression quality from 0 to 100.
 * @param maxImageSizePx Maximum width or height of the compressed image in pixels.
 */
class JpegImageFileCompressor(
  private val quality: Int = DefaultCompressionQuality,
  private val maxImageSizePx: Int = DefaultMaxImageSizePx
) : FileCompressor {

  /**
   * Compresses the image located at the provided path into a resized JPEG file.
   *
   * The compressed file is created in the same parent directory as the source file.
   * If the source file cannot be decoded as an image, this method returns `null`.
   *
   * @param sourcePath Path to the source image file.
   *
   * @return Resized compressed JPEG file, or `null` when the source file cannot be decoded.
   *
   * @throws IllegalArgumentException if quality is outside the 0..100 range.
   * @throws IllegalArgumentException if [maxImageSizePx] is not positive.
   * @throws IllegalArgumentException if the source file does not exist.
   * @throws IllegalStateException if the image cannot be compressed.
   */
  override fun compress(sourcePath: String): File? {
    require(quality in 0..100) {
      "JPEG compression quality must be between 0 and 100."
    }

    require(maxImageSizePx > 0) {
      "Maximum image size must be greater than 0."
    }

    val sourceFile = File(sourcePath)

    require(sourceFile.exists()) {
      "Source file does not exist."
    }

    val bitmap = BitmapFactory.decodeFile(sourceFile.absolutePath)
      ?: return null

    val resizedBitmap = bitmap.resizeToFit(
      maxSizePx = maxImageSizePx
    )

    val compressedFile = File(
      sourceFile.parentFile,
      "$CompressedImageFilePrefix${sourceFile.nameWithoutExtension}.$CompressedFileExtension"
    )

    FileOutputStream(compressedFile).use { output ->
      val isCompressed = resizedBitmap.compress(
        Bitmap.CompressFormat.JPEG,
        quality,
        output
      )

      check(isCompressed) {
        "Could not compress image file."
      }
    }

    if (resizedBitmap !== bitmap) {
      bitmap.recycle()
    }

    resizedBitmap.recycle()

    return compressedFile
  }

  /**
   * Resizes this bitmap to fit into a square with [maxSizePx] sides.
   *
   * If both bitmap dimensions are already smaller than or equal to [maxSizePx],
   * the original bitmap is returned without creating a copy.
   */
  private fun Bitmap.resizeToFit(maxSizePx: Int): Bitmap {
    val largestSide = maxOf(width, height)

    if (largestSide <= maxSizePx) {
      return this
    }

    val scale = maxSizePx.toFloat() / largestSide.toFloat()
    val targetWidth = (width * scale).roundToInt()
    val targetHeight = (height * scale).roundToInt()

    return this.scale(targetWidth, targetHeight)
  }
}