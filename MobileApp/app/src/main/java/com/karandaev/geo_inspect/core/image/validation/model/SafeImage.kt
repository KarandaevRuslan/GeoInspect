package com.karandaev.geo_inspect.core.image.validation.model

/**
 * Result of successful image validation.
 *
 * @param bytes Validated image bytes.
 * @param width Image width in pixels.
 * @param height Image height in pixels.
 * @param format Detected image format, for example `PNG` or `JPEG`.
 */
data class SafeImage(
  val bytes: ByteArray,
  val width: Int,
  val height: Int,
  val format: String
) {

  /**
   * Compares byte arrays by content instead of reference.
   */
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is SafeImage) return false

    return bytes.contentEquals(other.bytes) &&
      width == other.width &&
      height == other.height &&
      format == other.format
  }

  /**
   * Calculates hash code using byte array content.
   */
  override fun hashCode(): Int {
    var result = bytes.contentHashCode()
    result = 31 * result + width
    result = 31 * result + height
    result = 31 * result + format.hashCode()
    return result
  }
}