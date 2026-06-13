package com.karandaev.roadCrackDetectionServer.image.format;

import java.util.Optional;

/**
 * Detects common image formats by checking their magic bytes.
 *
 * <p>This class does not fully validate image files. It only checks the leading bytes that identify
 * well-known formats such as JPEG, PNG, WEBP, GIF, and BMP.
 */
public final class MagicBytesSniffer {

  /** Supported image formats and their corresponding ImageIO format names. */
  public enum Format {
    JPEG("JPEG"),
    PNG("PNG"),
    WEBP("WEBP"),
    GIF("GIF"),
    BMP("BMP");

    private final String imageIoName;

    /**
     * Creates an image format descriptor.
     *
     * @param imageIoName format name used by ImageIO
     */
    Format(String imageIoName) {
      this.imageIoName = imageIoName;
    }

    /**
     * Returns the ImageIO format name.
     *
     * @return ImageIO-compatible format name
     */
    public String imageIoName() {
      return imageIoName;
    }
  }

  /**
   * Attempts to detect the image format from the supplied byte array.
   *
   * @param bytes image file bytes
   * @return detected image format, or {@link Optional#empty()} if the format is unknown
   */
  public Optional<Format> sniff(byte[] bytes) {
    if (bytes == null || bytes.length < 12) return Optional.empty();

    // PNG signature: 89 50 4E 47 0D 0A 1A 0A.
    if (startsWith(bytes, new byte[] {(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A})) {
      return Optional.of(Format.PNG);
    }

    // JPEG files start with FF D8. The ending marker is not checked here.
    if ((bytes[0] & 0xFF) == 0xFF && (bytes[1] & 0xFF) == 0xD8) {
      return Optional.of(Format.JPEG);
    }

    // WEBP uses a RIFF container with "WEBP" at bytes 8..11.
    if (bytes[0] == 'R'
        && bytes[1] == 'I'
        && bytes[2] == 'F'
        && bytes[3] == 'F'
        && bytes[8] == 'W'
        && bytes[9] == 'E'
        && bytes[10] == 'B'
        && bytes[11] == 'P') {
      return Optional.of(Format.WEBP);
    }

    // GIF signature can be either "GIF87a" or "GIF89a".
    if (bytes[0] == 'G'
        && bytes[1] == 'I'
        && bytes[2] == 'F'
        && bytes[3] == '8'
        && (bytes[4] == '7' || bytes[4] == '9')
        && bytes[5] == 'a') {
      return Optional.of(Format.GIF);
    }

    // BMP files start with "BM".
    if (bytes[0] == 'B' && bytes[1] == 'M') {
      return Optional.of(Format.BMP);
    }

    return Optional.empty();
  }

  /**
   * Checks whether the byte array starts with the given prefix.
   *
   * @param data source byte array
   * @param prefix expected byte prefix
   * @return {@code true} if {@code data} starts with {@code prefix}
   */
  private boolean startsWith(byte[] data, byte[] prefix) {
    if (data.length < prefix.length) return false;
    for (int i = 0; i < prefix.length; i++) {
      if (data[i] != prefix[i]) return false;
    }
    return true;
  }
}
