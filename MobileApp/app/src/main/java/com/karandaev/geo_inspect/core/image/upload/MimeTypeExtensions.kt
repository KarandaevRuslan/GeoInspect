package com.karandaev.geo_inspect.core.image.upload

/**
 * Returns a file extension for the provided MIME type.
 */
internal fun String.toFileExtension(): String {
  return when (lowercase()) {
    "image/jpeg" -> "jpg"
    "image/png" -> "png"
    "image/webp" -> "webp"
    "application/pdf" -> "pdf"
    "text/plain" -> "txt"
    else -> "bin"
  }
}

/**
 * Returns a MIME type for the provided file extension.
 */
internal fun String.toMimeTypeOrNull(): String? {
  return when (lowercase()) {
    "jpg", "jpeg" -> "image/jpeg"
    "png" -> "image/png"
    "webp" -> "image/webp"
    "pdf" -> "application/pdf"
    "txt" -> "text/plain"
    "bin" -> "application/octet-stream"
    else -> null
  }
}