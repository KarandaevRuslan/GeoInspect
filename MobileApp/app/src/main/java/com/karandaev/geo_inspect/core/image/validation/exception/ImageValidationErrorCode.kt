package com.karandaev.geo_inspect.core.image.validation.exception

/**
 * Validation error codes for uploaded images.
 */
enum class ImageValidationErrorCode {

  /**
   * The provided file is empty.
   */
  EmptyFile,

  /**
   * The provided file is too large.
   */
  FileTooLarge,

  /**
   * The image format is unknown or unsupported.
   */
  UnsupportedMediaType,

  /**
   * The decoded image dimensions are invalid.
   */
  InvalidImageDimensions,

  /**
   * The image dimensions exceed configured limits.
   */
  ImageDimensionsTooLarge,

  /**
   * The image megapixel count exceeds configured limits.
   */
  ImageMegapixelsTooLarge,

  /**
   * The image cannot be decoded.
   */
  DecodeFailed
}