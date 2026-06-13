package com.karandaev.roadCrackDetectionServer.image.normalize;

import com.karandaev.roadCrackDetectionServer.image.exception.ImageValidationException;
import org.springframework.http.HttpStatus;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

/**
 * Normalizes decoded images by redrawing them into a clean buffer and writing them again.
 *
 * <p>This removes original metadata and unusual source-specific sections.
 */
public final class ImageNormalizer {

  /**
   * Redraws an image into a clean buffer and writes it in the requested output format.
   *
   * @param source decoded source image
   * @param outputFormat target ImageIO format name, for example {@code PNG} or {@code JPEG}
   * @return normalized image bytes
   * @throws ImageValidationException if writing fails
   */
  public byte[] normalize(BufferedImage source, String outputFormat) {
    String normalizedOutputFormat = outputFormat.toUpperCase(Locale.ROOT);

    BufferedImage clean =
        createCleanImage(source.getWidth(), source.getHeight(), normalizedOutputFormat);

    Graphics2D graphics = clean.createGraphics();
    try {
      graphics.drawImage(source, 0, 0, null);
    } finally {
      graphics.dispose();
    }

    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      boolean written = ImageIO.write(clean, normalizedOutputFormat, outputStream);

      if (!written) {
        throw new ImageValidationException(
            HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Cannot write normalized image");
      }

      return outputStream.toByteArray();
    } catch (IOException error) {
      throw new ImageValidationException(HttpStatus.INTERNAL_SERVER_ERROR, "Normalization failed");
    }
  }

  private BufferedImage createCleanImage(int width, int height, String outputFormat) {
    int imageType =
        outputFormat.equals("JPEG") ? BufferedImage.TYPE_3BYTE_BGR : BufferedImage.TYPE_INT_ARGB;

    return new BufferedImage(width, height, imageType);
  }
}
