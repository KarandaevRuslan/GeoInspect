package com.karandaev.roadCrackDetectionServer.image.format;

import com.karandaev.roadCrackDetectionServer.image.config.ImageSecurityProperties;
import com.karandaev.roadCrackDetectionServer.image.exception.ImageValidationException;
import org.springframework.http.HttpStatus;

import javax.imageio.ImageIO;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/** Validates image formats detected from magic bytes and normalization output formats. */
public final class ImageFormatValidator {

  private final ImageSecurityProperties props;

  /**
   * Creates an image format validator.
   *
   * @param props image security configuration
   */
  public ImageFormatValidator(ImageSecurityProperties props) {
    this.props = props;
  }

  /**
   * Creates an exception used when magic bytes do not match any known image format.
   *
   * @return unsupported media type exception
   */
  public ImageValidationException unknownFormat() {
    return new ImageValidationException(
        HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unknown file signature");
  }

  /**
   * Validates that the detected magic-byte format is allowed by configuration.
   *
   * <p>This method validates the real detected format, not the filename extension, multipart
   * content type, or user-provided metadata.
   *
   * @param format format detected from magic bytes
   * @throws ImageValidationException if the detected format is not allowed
   */
  public void validateAllowedFormat(MagicBytesSniffer.Format format) {
    String detectedFormat = normalizeFormatName(format.imageIoName());

    boolean allowed =
        props.getAllowedFormats().stream()
            .map(this::normalizeFormatName)
            .anyMatch(allowedFormat -> allowedFormat.equals(detectedFormat));

    if (!allowed) {
      throw new ImageValidationException(
          HttpStatus.UNSUPPORTED_MEDIA_TYPE,
          "Unsupported image format: "
              + detectedFormat
              + ". Supported formats: "
              + supportedFormatsMessage());
    }
  }

  /**
   * Validates that the configured normalization format is supported by ImageIO writers.
   *
   * @param outputFormat configured output format, for example {@code JPEG} or {@code PNG}
   * @throws ImageValidationException if ImageIO cannot write this format
   */
  public void validateNormalizationFormat(String outputFormat) {
    String normalizedOutputFormat = normalizeFormatName(outputFormat);
    Set<String> writableFormats = writableImageIoFormats();

    if (!writableFormats.contains(normalizedOutputFormat)) {
      throw new ImageValidationException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "Normalization format is not supported by ImageIO: " + normalizedOutputFormat);
    }
  }

  private String supportedFormatsMessage() {
    return props.getAllowedFormats().stream()
        .map(this::normalizeFormatName)
        .distinct()
        .collect(Collectors.joining(", "));
  }

  private Set<String> writableImageIoFormats() {
    return Arrays.stream(ImageIO.getWriterFormatNames())
        .map(this::normalizeFormatName)
        .collect(Collectors.toSet());
  }

  private String normalizeFormatName(String format) {
    return format.trim().toUpperCase(Locale.ROOT);
  }
}
