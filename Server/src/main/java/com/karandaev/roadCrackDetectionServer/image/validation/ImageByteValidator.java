package com.karandaev.roadCrackDetectionServer.image.validation;

import com.karandaev.roadCrackDetectionServer.image.config.ImageSecurityProperties;
import com.karandaev.roadCrackDetectionServer.image.exception.ImageValidationException;
import org.springframework.http.HttpStatus;

/** Validates raw uploaded image bytes before any expensive processing. */
public final class ImageByteValidator {

  private final ImageSecurityProperties props;

  /**
   * Creates an image byte validator.
   *
   * @param props image security configuration
   */
  public ImageByteValidator(ImageSecurityProperties props) {
    this.props = props;
  }

  /**
   * Validates that uploaded bytes are present and do not exceed the configured size limit.
   *
   * @param bytes raw uploaded file bytes
   * @throws ImageValidationException if the file is empty or too large
   */
  public void validateBytes(byte[] bytes) {
    if (bytes == null || bytes.length == 0) {
      throw new ImageValidationException(HttpStatus.BAD_REQUEST, "Empty file");
    }

    if (bytes.length > props.getMaxBytes()) {
      throw new ImageValidationException(HttpStatus.PAYLOAD_TOO_LARGE, "File too large");
    }
  }
}
