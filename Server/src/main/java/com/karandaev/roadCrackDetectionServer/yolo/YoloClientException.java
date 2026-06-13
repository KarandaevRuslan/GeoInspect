package com.karandaev.roadCrackDetectionServer.yolo;

/** Runtime exception thrown when a YOLO client call fails. */
public class YoloClientException extends RuntimeException {

  /**
   * Creates a YOLO client exception.
   *
   * @param message error message
   */
  public YoloClientException(String message) {
    super(message);
  }

  /**
   * Creates a YOLO client exception with a root cause.
   *
   * @param message error message
   * @param cause original exception
   */
  public YoloClientException(String message, Throwable cause) {
    super(message, cause);
  }
}
