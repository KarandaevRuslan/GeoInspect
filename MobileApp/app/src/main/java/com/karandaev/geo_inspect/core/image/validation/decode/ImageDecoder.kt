package com.karandaev.geo_inspect.core.image.validation.decode

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.karandaev.geo_inspect.core.image.validation.exception.ImageValidationErrorCode
import com.karandaev.geo_inspect.core.image.validation.exception.ImageValidationException

/**
 * Fully decodes image bytes into a [Bitmap] after lightweight validation has passed.
 */
class ImageDecoder {

  /**
   * Fully decodes image bytes.
   *
   * @param bytes Raw image bytes.
   * @return Decoded bitmap.
   * @throws ImageValidationException if Android cannot decode the image.
   */
  fun decode(bytes: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(
      bytes,
      0,
      bytes.size
    ) ?: throw ImageValidationException(
      code = ImageValidationErrorCode.DecodeFailed,
      message = "Image cannot be decoded."
    )
  }
}