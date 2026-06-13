package com.karandaev.geo_inspect.core.image.validation.format

/**
 * Detects common image formats by checking their magic bytes.
 *
 * This class does not fully validate image files. It only checks the leading bytes
 * that identify well-known formats such as JPEG, PNG, WEBP, GIF, and BMP.
 */
class MagicBytesSniffer {

  /**
   * Supported image formats and their corresponding format names.
   *
   * @param imageName Common image format name.
   */
  enum class Format(
    val imageName: String
  ) {
    JPEG("JPEG"),
    PNG("PNG"),
    WEBP("WEBP"),
    GIF("GIF"),
    BMP("BMP")
  }

  /**
   * Attempts to detect the image format from the supplied byte array.
   *
   * @param bytes Image file bytes.
   * @return Detected image format, or `null` if the format is unknown.
   */
  fun sniff(bytes: ByteArray?): Format? {
    if (bytes == null || bytes.size < MIN_SIGNATURE_BYTES) {
      return null
    }

    return when {
      bytes.startsWith(PNG_SIGNATURE) -> Format.PNG
      bytes.isJpeg() -> Format.JPEG
      bytes.isWebp() -> Format.WEBP
      bytes.isGif() -> Format.GIF
      bytes.isBmp() -> Format.BMP
      else -> null
    }
  }

  private fun ByteArray.isJpeg(): Boolean {
    return size >= 2 &&
      (this[0].toInt() and BYTE_MASK) == JPEG_FIRST_BYTE &&
      (this[1].toInt() and BYTE_MASK) == JPEG_SECOND_BYTE
  }

  private fun ByteArray.isWebp(): Boolean {
    return size >= MIN_SIGNATURE_BYTES &&
      this[0] == 'R'.code.toByte() &&
      this[1] == 'I'.code.toByte() &&
      this[2] == 'F'.code.toByte() &&
      this[3] == 'F'.code.toByte() &&
      this[8] == 'W'.code.toByte() &&
      this[9] == 'E'.code.toByte() &&
      this[10] == 'B'.code.toByte() &&
      this[11] == 'P'.code.toByte()
  }

  private fun ByteArray.isGif(): Boolean {
    return size >= GIF_SIGNATURE_SIZE &&
      this[0] == 'G'.code.toByte() &&
      this[1] == 'I'.code.toByte() &&
      this[2] == 'F'.code.toByte() &&
      this[3] == '8'.code.toByte() &&
      (this[4] == '7'.code.toByte() || this[4] == '9'.code.toByte()) &&
      this[5] == 'a'.code.toByte()
  }

  private fun ByteArray.isBmp(): Boolean {
    return size >= 2 &&
      this[0] == 'B'.code.toByte() &&
      this[1] == 'M'.code.toByte()
  }

  private fun ByteArray.startsWith(prefix: ByteArray): Boolean {
    if (size < prefix.size) {
      return false
    }

    for (index in prefix.indices) {
      if (this[index] != prefix[index]) {
        return false
      }
    }

    return true
  }

  private companion object {
    const val MIN_SIGNATURE_BYTES = 12
    const val GIF_SIGNATURE_SIZE = 6

    const val BYTE_MASK = 0xFF
    const val JPEG_FIRST_BYTE = 0xFF
    const val JPEG_SECOND_BYTE = 0xD8

    val PNG_SIGNATURE = byteArrayOf(
      0x89.toByte(),
      0x50,
      0x4E,
      0x47,
      0x0D,
      0x0A,
      0x1A,
      0x0A
    )
  }
}