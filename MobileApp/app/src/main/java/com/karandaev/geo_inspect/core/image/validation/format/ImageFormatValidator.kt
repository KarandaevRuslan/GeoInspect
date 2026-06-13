package com.karandaev.geo_inspect.core.image.validation.format

import com.karandaev.geo_inspect.core.image.validation.config.ImageSecurityProperties
import com.karandaev.geo_inspect.core.image.validation.exception.ImageValidationErrorCode
import com.karandaev.geo_inspect.core.image.validation.exception.ImageValidationException
import java.util.Locale

/**
 * Validates image formats detected from magic bytes.
 *
 * This validator checks the real detected file format, not the file name,
 * extension, content type, or any user-provided metadata.
 *
 * @param props Image security configuration.
 */
class ImageFormatValidator(
  private val props: ImageSecurityProperties
) {

  /**
   * Creates an exception used when magic bytes do not match any known image format.
   *
   * @return Unsupported media type validation exception.
   */
  fun unknownFormat(): ImageValidationException {
    return ImageValidationException(
      code = ImageValidationErrorCode.UnsupportedMediaType,
      message = "Unknown file signature."
    )
  }

  /**
   * Validates that the detected magic-byte format is allowed by configuration.
   *
   * @param format Format detected from magic bytes.
   *
   * @throws ImageValidationException if the detected format is not allowed.
   */
  fun validateAllowedFormat(format: MagicBytesSniffer.Format) {
    val detectedFormat = format.imageName.normalizeFormatName()

    val isAllowed = props.allowedFormats
      .map { allowedFormat -> allowedFormat.normalizeFormatName() }
      .any { allowedFormat -> allowedFormat == detectedFormat }

    if (!isAllowed) {
      throw ImageValidationException(
        code = ImageValidationErrorCode.UnsupportedMediaType,
        message = "Unsupported image format: $detectedFormat. Supported formats: ${supportedFormatsMessage()}."
      )
    }
  }

  private fun supportedFormatsMessage(): String {
    return props.allowedFormats
      .map { format -> format.normalizeFormatName() }
      .distinct()
      .joinToString(separator = ", ")
  }

  private fun String.normalizeFormatName(): String {
    return trim().uppercase(Locale.ROOT)
  }
}