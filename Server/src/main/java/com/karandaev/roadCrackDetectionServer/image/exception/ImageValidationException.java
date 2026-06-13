package com.karandaev.roadCrackDetectionServer.image.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when uploaded image validation or normalization fails.
 *
 * <p>The exception carries an HTTP status that should be returned to the client.
 */
@Getter
public class ImageValidationException extends RuntimeException {
  private final HttpStatus status;

  /**
   * Creates an image validation exception.
   *
   * @param status HTTP status that represents the validation failure
   * @param message human-readable error message
   */
  public ImageValidationException(HttpStatus status, String message) {
    super(message);
    this.status = status;
  }
}
