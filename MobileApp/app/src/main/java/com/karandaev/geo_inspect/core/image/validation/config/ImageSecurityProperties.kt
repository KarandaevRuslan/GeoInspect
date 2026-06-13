package com.karandaev.geo_inspect.core.image.validation.config

/**
 * Configuration for image upload validation.
 *
 * @param maxBytes Maximum allowed image size in bytes.
 * @param maxWidth Maximum allowed image width in pixels.
 * @param maxHeight Maximum allowed image height in pixels.
 * @param maxMegapixels Maximum allowed image size in megapixels.
 * @param allowedFormats Image formats accepted by validation.
 */
data class ImageSecurityProperties(
  val maxBytes: Long = DefaultMaxBytes,
  val maxWidth: Int = DefaultMaxWidth,
  val maxHeight: Int = DefaultMaxHeight,
  val maxMegapixels: Int = DefaultMaxMegapixels,
  val allowedFormats: List<String> = DefaultAllowedFormats
) {
  companion object {
    private const val DefaultMaxBytes = 10L * 1024L * 1024L
    private const val DefaultMaxWidth = 4096
    private const val DefaultMaxHeight = 4096
    private const val DefaultMaxMegapixels = 20

    private val DefaultAllowedFormats = listOf(
      "JPEG",
      "PNG",
      "WEBP"
    )
  }
}