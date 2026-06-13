package com.karandaev.geo_inspect.core.image.validation

import com.karandaev.geo_inspect.core.image.validation.model.SafeImage

/**
 * Validates an image file by its local file path.
 */
interface ImageValidationProvider {

  /**
   * Validates the image located at the provided file path.
   *
   * @param filePath Local image file path.
   * @return Validated image metadata and bytes.
   */
  fun validate(filePath: String): SafeImage
}