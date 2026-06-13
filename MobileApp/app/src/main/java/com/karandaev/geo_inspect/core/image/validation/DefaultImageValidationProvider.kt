package com.karandaev.geo_inspect.core.image.validation

import com.karandaev.geo_inspect.core.image.validation.config.ImageSecurityProperties
import com.karandaev.geo_inspect.core.image.validation.decode.ImageDecoder
import com.karandaev.geo_inspect.core.image.validation.decode.ImageDimensionProbe
import com.karandaev.geo_inspect.core.image.validation.format.ImageFormatValidator
import com.karandaev.geo_inspect.core.image.validation.format.MagicBytesSniffer
import com.karandaev.geo_inspect.core.image.validation.model.SafeImage
import com.karandaev.geo_inspect.core.image.validation.validation.ImageByteValidator
import java.io.File

/**
 * Default implementation of [ImageValidationProvider].
 *
 * This provider validates a local image file without trusting its filename,
 * extension, or MIME type. The real format is detected from magic bytes.
 *
 * Validation pipeline:
 * 1. Read local file bytes.
 * 2. Validate byte size.
 * 3. Detect image format using magic bytes.
 * 4. Validate that the detected format is allowed.
 * 5. Read image dimensions without full bitmap decoding.
 * 6. Validate dimensions and megapixel limits.
 * 7. Decode the bitmap to verify that Android can read the image.
 *
 * No normalization is performed. Original bytes are returned after validation.
 *
 * @param props Image security and validation configuration.
 * @param sniffer Magic-bytes image format detector.
 * @param byteValidator Raw byte validator.
 * @param formatValidator Detected format validator.
 * @param dimensionProbe Image dimension reader and validator.
 * @param decoder Full image decoder.
 */
class DefaultImageValidationProvider(
  private val props: ImageSecurityProperties = ImageSecurityProperties(),
  private val sniffer: MagicBytesSniffer = MagicBytesSniffer(),
  private val byteValidator: ImageByteValidator = ImageByteValidator(props),
  private val formatValidator: ImageFormatValidator = ImageFormatValidator(props),
  private val dimensionProbe: ImageDimensionProbe = ImageDimensionProbe(props),
  private val decoder: ImageDecoder = ImageDecoder()
) : ImageValidationProvider {

  /**
   * Validates the image located at the provided file path.
   *
   * @param filePath Local image file path.
   * @return Safe image containing original validated bytes, dimensions, and detected format.
   */
  override fun validate(filePath: String): SafeImage {
    val file = File(filePath)

    require(file.exists()) {
      "Image file does not exist."
    }

    require(file.isFile) {
      "Image path does not point to a file."
    }

    val bytes = file.readBytes()

    byteValidator.validateBytes(bytes)

    val format = sniffer.sniff(bytes)
      ?: throw formatValidator.unknownFormat()

    formatValidator.validateAllowedFormat(format)

    val dimensions = dimensionProbe.probeDimensions(bytes)
    dimensionProbe.validateDimensions(dimensions)

    val bitmap = decoder.decode(bytes)
    bitmap.recycle()

    return SafeImage(
      bytes = bytes,
      width = dimensions.width,
      height = dimensions.height,
      format = format.imageName
    )
  }
}