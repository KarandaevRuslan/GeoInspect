package com.karandaev.roadCrackDetectionServer.util;

import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Reads a process output stream line by line and writes each line to a logger.
 *
 * <p>This class is typically used to consume stdout or stderr of a spawned process. Reading these
 * streams prevents the child process from blocking when its output buffers become full.
 */
public final class ProcessOutputGobbler implements Runnable {

  private final InputStream in;
  private final Logger log;
  private final String prefix;
  private final Charset charset;

  /**
   * Creates a process output gobbler using the platform default charset.
   *
   * @param in process output stream to read from
   * @param log logger used to write output lines
   * @param prefix prefix added before each logged line
   */
  public ProcessOutputGobbler(InputStream in, Logger log, String prefix) {
    this(in, log, prefix, Charset.defaultCharset());
  }

  /**
   * Creates a process output gobbler using the provided charset.
   *
   * @param in process output stream to read from
   * @param log logger used to write output lines
   * @param prefix prefix added before each logged line
   * @param charset charset used to decode process output bytes
   */
  public ProcessOutputGobbler(InputStream in, Logger log, String prefix, Charset charset) {
    this.in = in;
    this.log = log;
    this.prefix = prefix;
    this.charset = charset;
  }

  /**
   * Continuously reads the configured stream until it ends or the current thread is interrupted.
   */
  @Override
  public void run() {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(in, charset))) {
      String line;
      while (!Thread.currentThread().isInterrupted() && (line = br.readLine()) != null) {
        log.info("{}{}", prefix, line);
      }
    } catch (Exception e) {
      // This commonly happens when the child process stops or the stream is closed.
      // It is expected during normal shutdown, so it is logged at debug level.
      log.debug("{}gobbler stopped: {}", prefix, e.toString());
    }
  }
}
