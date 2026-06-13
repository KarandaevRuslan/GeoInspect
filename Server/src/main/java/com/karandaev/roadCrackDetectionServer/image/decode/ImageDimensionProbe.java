package com.karandaev.roadCrackDetectionServer.image.decode;

import com.karandaev.roadCrackDetectionServer.image.exception.ImageValidationException;
import com.karandaev.roadCrackDetectionServer.image.config.ImageSecurityProperties;
import org.springframework.http.HttpStatus;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

/** Reads and validates image dimensions without fully decoding image pixels. */
public final class ImageDimensionProbe {

  private final ImageSecurityProperties props;

  /**
   * Creates an image dimension probe.
   *
   * @param props image security configuration
   */
  public ImageDimensionProbe(ImageSecurityProperties props) {
    this.props = props;
  }

  /**
   * Reads image dimensions using ImageIO readers without fully decoding pixel data.
   *
   * @param bytes raw image bytes
   * @return detected image dimensions
   * @throws ImageValidationException if dimensions cannot be read
   */
  public Dimension probeDimensions(byte[] bytes) {
    try (ImageInputStream inputStream =
        ImageIO.createImageInputStream(new ByteArrayInputStream(bytes))) {

      if (inputStream == null) {
        throw new ImageValidationException(
            HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Cannot read image stream");
      }

      Iterator<ImageReader> readers = ImageIO.getImageReaders(inputStream);
      if (!readers.hasNext()) {
        throw new ImageValidationException(
            HttpStatus.UNSUPPORTED_MEDIA_TYPE, "No ImageReader for this format");
      }

      ImageReader reader = readers.next();
      try {
        reader.setInput(inputStream, true, true);

        return new Dimension(reader.getWidth(0), reader.getHeight(0));
      } finally {
        reader.dispose();
      }
    } catch (IOException error) {
      throw new ImageValidationException(
          HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Corrupted image header");
    }
  }

  /**
   * Validates image dimensions against configured width, height, and megapixel limits.
   *
   * @param dimensions detected image dimensions
   * @throws ImageValidationException if dimensions are invalid or too large
   */
  public void validateDimensions(Dimension dimensions) {
    int width = dimensions.width;
    int height = dimensions.height;

    if (width <= 0 || height <= 0) {
      throw new ImageValidationException(
          HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Invalid image dimensions");
    }

    if (width > props.getMaxWidth() || height > props.getMaxHeight()) {
      throw new ImageValidationException(
          HttpStatus.PAYLOAD_TOO_LARGE, "Image dimensions too large");
    }

    long pixels = (long) width * (long) height;
    long maxPixels = (long) props.getMaxMegapixels() * 1_000_000L;

    if (pixels > maxPixels) {
      throw new ImageValidationException(
          HttpStatus.PAYLOAD_TOO_LARGE, "Image megapixels too large");
    }
  }
}
