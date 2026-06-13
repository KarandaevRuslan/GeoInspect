package com.karandaev.roadCrackDetectionServer.image.antivirus;

/**
 * Virus scanner implementation that does nothing.
 *
 * <p>This implementation is used when antivirus scanning is disabled in configuration.
 */
public class NoopVirusScanner implements VirusScanner {

  /**
   * Performs no scanning and always accepts the provided bytes.
   *
   * @param bytes file bytes to scan
   */
  @Override
  public void scanOrThrow(byte[] bytes) {
    // Intentionally empty.
  }
}
