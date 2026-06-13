package com.karandaev.geo_inspect.core.image.validation.exception

/**
 * Exception thrown when uploaded image validation fails.
 *
 * @param code Machine-readable validation error code.
 * @param message Human-readable error message.
 */
class ImageValidationException(
  val code: ImageValidationErrorCode,
  message: String
) : RuntimeException(message)