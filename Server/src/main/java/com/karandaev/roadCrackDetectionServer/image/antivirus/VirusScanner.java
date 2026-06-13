package com.karandaev.roadCrackDetectionServer.image.antivirus;

/** Contract for scanning uploaded file bytes before they are processed by the application. */
public interface VirusScanner {

  /**
   * Scans the provided bytes and throws an exception if the content must be rejected.
   *
   * @param bytes file bytes to scan
   */
  void scanOrThrow(byte[] bytes);
}
