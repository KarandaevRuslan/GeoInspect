package com.karandaev.roadCrackDetectionServer.image.decode;

import com.karandaev.roadCrackDetectionServer.image.exception.ImageValidationException;
import org.springframework.http.HttpStatus;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/** Fully decodes image bytes into a buffered image after lightweight validation has passed. */
public final class ImageDecoder {

  /**
   * Fully decodes image bytes.
   *
   * @param bytes raw image bytes
   * @return decoded image
   * @throws ImageValidationException if ImageIO cannot decode the image
   */
  public BufferedImage decode(byte[] bytes) {
    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
      BufferedImage image = ImageIO.read(inputStream);

      if (image == null) {
        throw new ImageValidationException(
            HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Image cannot be decoded");
      }

      return image;
    } catch (IOException error) {
      throw new ImageValidationException(
          HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Failed to decode image");
    }
  }
}
