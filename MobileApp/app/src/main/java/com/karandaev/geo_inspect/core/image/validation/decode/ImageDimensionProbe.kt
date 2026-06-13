package com.karandaev.geo_inspect.core.image.validation.decode

import android.graphics.BitmapFactory
import com.karandaev.geo_inspect.core.image.validation.config.ImageSecurityProperties
import com.karandaev.geo_inspect.core.image.validation.exception.ImageValidationErrorCode
import com.karandaev.geo_inspect.core.image.validation.exception.ImageValidationException

/**
 * Reads and validates image dimensions without fully decoding image pixels.
 */
class ImageDimensionProbe(
  private val props: ImageSecurityProperties
) {

  /**
   * Reads image dimensions without decoding the full bitmap into memory.
   *
   * @param bytes Raw image bytes.
   * @return Detected image dimensions.
   * @throws ImageValidationException if dimensions cannot be read.
   */
  fun probeDimensions(bytes: ByteArray): ImageDimensions {
    val options = BitmapFactory.Options().apply {
      inJustDecodeBounds = true
    }

    BitmapFactory.decodeByteArray(
      bytes,
      0,
      bytes.size,
      options
    )

    if (options.outWidth <= 0 || options.outHeight <= 0) {
      throw ImageValidationException(
        code = ImageValidationErrorCode.InvalidImageDimensions,
        message = "Invalid image dimensions."
      )
    }

    return ImageDimensions(
      width = options.outWidth,
      height = options.outHeight
    )
  }

  /**
   * Validates image dimensions against configured width, height, and megapixel limits.
   *
   * @param dimensions Detected image dimensions.
   * @throws ImageValidationException if dimensions are invalid or too large.
   */
  fun validateDimensions(dimensions: ImageDimensions) {
    val width = dimensions.width
    val height = dimensions.height

    if (width <= 0 || height <= 0) {
      throw ImageValidationException(
        code = ImageValidationErrorCode.InvalidImageDimensions,
        message = "Invalid image dimensions."
      )
    }

    if (width > props.maxWidth || height > props.maxHeight) {
      throw ImageValidationException(
        code = ImageValidationErrorCode.ImageDimensionsTooLarge,
        message = "Image dimensions are too large."
      )
    }

    val pixels = width.toLong() * height.toLong()
    val maxPixels = props.maxMegapixels.toLong() * PixelsInMegapixel

    if (pixels > maxPixels) {
      throw ImageValidationException(
        code = ImageValidationErrorCode.ImageMegapixelsTooLarge,
        message = "Image megapixels are too large."
      )
    }
  }

  private companion object {
    const val PixelsInMegapixel = 1_000_000L
  }
}