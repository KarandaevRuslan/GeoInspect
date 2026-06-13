package com.karandaev.roadCrackDetectionServer.errors;

import com.karandaev.roadCrackDetectionServer.image.exception.ImageValidationException;
import com.karandaev.roadCrackDetectionServer.yolo.YoloClientException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;
import java.util.Map;

/**
 * Global REST exception handler for application-level errors.
 *
 * <p>This class converts selected exceptions into consistent JSON HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles authentication failures caused by invalid or missing credentials.
   *
   * @param ex authentication exception thrown by Spring Security
   * @return HTTP 401 response with a JSON error body
   */
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<?> badCredentials(BadCredentialsException ex) {
    return ResponseEntity.status(401).body(error("unauthorized", ex.getMessage()));
  }

  /**
   * Handles authorization failures.
   *
   * @param ex access denied exception
   * @return HTTP 403 response with a JSON error body
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<?> accessDenied(AccessDeniedException ex) {
    return ResponseEntity.status(403).body(error("forbidden", "Access denied"));
  }

  /**
   * Handles image validation errors and returns the HTTP status stored in the exception.
   *
   * @param ex image validation exception
   * @return HTTP response with a JSON error body
   */
  @ExceptionHandler(ImageValidationException.class)
  public ResponseEntity<?> imageValidation(ImageValidationException ex) {
    return ResponseEntity.status(ex.getStatus()).body(error("invalid_image", ex.getMessage()));
  }

  /**
   * Handles multipart upload size violations.
   *
   * @param ex upload size exception
   * @return HTTP 413 response with a JSON error body
   */
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<?> maxUploadSizeExceeded(MaxUploadSizeExceededException ex) {
    return ResponseEntity.status(413).body(error("file_too_large", "Uploaded file is too large"));
  }

  /**
   * Handles YOLO inference service failures.
   *
   * @param ex YOLO client exception
   * @return HTTP 502 response with a JSON error body
   */
  @ExceptionHandler(YoloClientException.class)
  public ResponseEntity<?> yoloClient(YoloClientException ex) {
    return ResponseEntity.status(502).body(error("yolo_unavailable", ex.getMessage()));
  }

  /**
   * Handles local file I/O failures, for example avatar file write/delete errors.
   *
   * @param ex I/O exception
   * @return HTTP 500 response with a JSON error body
   */
  @ExceptionHandler(IOException.class)
  public ResponseEntity<?> io(IOException ex) {
    return ResponseEntity.status(500).body(error("io_error", "File operation failed"));
  }

  /**
   * Handles invalid security-sensitive operations, for example invalid file paths.
   *
   * @param ex security exception
   * @return HTTP 400 response with a JSON error body
   */
  @ExceptionHandler(SecurityException.class)
  public ResponseEntity<?> security(SecurityException ex) {
    return ResponseEntity.status(400).body(error("invalid_request", ex.getMessage()));
  }

  /**
   * Handles unexpected server errors.
   *
   * @param ex unexpected exception
   * @return HTTP 500 response with a JSON error body
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> generic(Exception ex) {
    return ResponseEntity.status(500).body(error("internal_error", "Internal server error"));
  }

  private Map<String, String> error(String error, String message) {
    return Map.of(
        "error", error,
        "message", message);
  }
}
