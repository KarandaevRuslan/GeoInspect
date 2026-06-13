package com.karandaev.geo_inspect.core.image.validation.validation

import com.karandaev.geo_inspect.core.image.validation.config.ImageSecurityProperties
import com.karandaev.geo_inspect.core.image.validation.exception.ImageValidationErrorCode
import com.karandaev.geo_inspect.core.image.validation.exception.ImageValidationException

/**
 * Validates raw uploaded image bytes before any expensive processing.
 *
 * @param props Image security configuration.
 */
class ImageByteValidator(
  private val props: ImageSecurityProperties
) {

  /**
   * Validates that uploaded bytes are present and do not exceed the configured size limit.
   *
   * @param bytes Raw uploaded file bytes.
   *
   * @throws ImageValidationException if the file is empty or too large.
   */
  fun validateBytes(bytes: ByteArray?) {
    if (bytes == null || bytes.isEmpty()) {
      throw ImageValidationException(
        code = ImageValidationErrorCode.EmptyFile,
        message = "Empty file."
      )
    }

    if (bytes.size > props.maxBytes) {
      throw ImageValidationException(
        code = ImageValidationErrorCode.FileTooLarge,
        message = "File is too large."
      )
    }
  }
}