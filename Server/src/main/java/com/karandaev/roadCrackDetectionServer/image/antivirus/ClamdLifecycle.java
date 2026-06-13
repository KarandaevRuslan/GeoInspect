package com.karandaev.roadCrackDetectionServer.image.antivirus;

import com.karandaev.roadCrackDetectionServer.util.ProcessOutputGobbler;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.Duration;

/**
 * Starts and stops a local {@code clamd} process together with the Spring Boot application.
 *
 * <p>This lifecycle component is useful when the application should manage ClamAV itself instead of
 * connecting to an already running daemon.
 */
public class ClamdLifecycle {

  private static final Logger log = LoggerFactory.getLogger(ClamdLifecycle.class);

  private final String clamdExe;
  private final String clamdConf;
  private final String host;
  private final int port;
  private final int startupTimeoutMs;

  private Process clamd;
  private Thread clamdOutThread;

  /**
   * Creates a lifecycle manager for a local {@code clamd} process.
   *
   * @param clamdExe path to the {@code clamd} executable
   * @param clamdConf path to the {@code clamd.conf} configuration file
   * @param host host where {@code clamd} is expected to listen
   * @param port TCP port where {@code clamd} is expected to listen
   * @param startupTimeoutMs maximum time to wait for the TCP port to become available
   */
  public ClamdLifecycle(
      String clamdExe, String clamdConf, String host, int port, int startupTimeoutMs) {
    this.clamdExe = clamdExe;
    this.clamdConf = clamdConf;
    this.host = host;
    this.port = port;
    this.startupTimeoutMs = startupTimeoutMs;
  }

  /**
   * Starts the local {@code clamd} process after the Spring application is ready.
   *
   * @throws IOException if the process cannot be started
   */
  @EventListener(ApplicationReadyEvent.class)
  public void start() throws IOException {
    log.info("Starting clamd. user.dir={}", System.getProperty("user.dir"));

    if (clamdExe == null || clamdExe.isBlank()) {
      throw new IllegalStateException(
          "image-security.antivirus.clamd-exe is required when start-clamd=true");
    }
    if (clamdConf == null || clamdConf.isBlank()) {
      throw new IllegalStateException(
          "image-security.antivirus.clamd-conf is required when start-clamd=true");
    }

    String exe = clamdExe.trim();
    String conf = clamdConf.trim();

    File exeFile = new File(exe);
    File confFile = new File(conf);

    log.info("clamdExeRaw=[{}], len={}", clamdExe, clamdExe.length());
    log.info(
        "clamdExe abs={}, exists={}, isFile={}",
        exeFile.getAbsolutePath(),
        exeFile.exists(),
        exeFile.isFile());

    log.info("clamdConfRaw=[{}], len={}", clamdConf, clamdConf.length());
    log.info(
        "clamdConf abs={}, exists={}, isFile={}",
        confFile.getAbsolutePath(),
        confFile.exists(),
        confFile.isFile());

    if (!exeFile.isFile()) {
      throw new IllegalStateException("clamd exe not found: " + exeFile.getAbsolutePath());
    }
    if (!confFile.isFile()) {
      throw new IllegalStateException("clamd conf not found: " + confFile.getAbsolutePath());
    }

    log.info(
        "Launching clamd: exe={}, -c {}", exeFile.getAbsolutePath(), confFile.getAbsolutePath());

    // Merge stderr into stdout so both normal output and errors are consumed by one gobbler.
    clamd =
        new ProcessBuilder(exeFile.getAbsolutePath(), "-c", confFile.getAbsolutePath())
            .redirectErrorStream(true)
            .start();

    try {
      log.info("clamd started. pid={}", clamd.pid());
    } catch (Throwable t) {
      log.info("clamd started. pid=<unavailable>");
    }

    // Consume process output on a separate daemon thread to prevent output buffer blocking.
    clamdOutThread =
        new Thread(
            new ProcessOutputGobbler(clamd.getInputStream(), log, "[clamd] "),
            "clamd-output-gobbler");
    clamdOutThread.setDaemon(true);
    clamdOutThread.start();

    waitForTcpPort(host, port, Duration.ofMillis(startupTimeoutMs), clamd);

    log.info("clamd is reachable on {}:{}", host, port);
  }

  /** Stops the managed {@code clamd} process when the Spring application is shutting down. */
  @PreDestroy
  public void stop() {
    log.info("Stopping clamd...");
    if (clamd != null && clamd.isAlive()) {
      clamd.destroy();
      log.info("clamd destroy() sent");
    }
    if (clamdOutThread != null) {
      clamdOutThread.interrupt();
    }
  }

  /**
   * Waits until the given TCP port becomes reachable or the process exits early.
   *
   * @param host target host
   * @param port target TCP port
   * @param timeout maximum wait duration
   * @param proc process that is expected to open the port
   */
  private static void waitForTcpPort(String host, int port, Duration timeout, Process proc) {
    long end = System.currentTimeMillis() + timeout.toMillis();

    while (System.currentTimeMillis() < end) {
      // If the process exits before opening the port, startup failed.
      if (proc != null && !proc.isAlive()) {
        int code = proc.exitValue();
        throw new IllegalStateException("clamd exited early with code " + code);
      }

      try (Socket s = new Socket()) {
        // Try to establish a TCP connection to confirm that clamd is accepting requests.
        s.connect(new InetSocketAddress(host, port), 500);
        return;
      } catch (Exception ignored) {
        try {
          Thread.sleep(200);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          throw new IllegalStateException("Interrupted while waiting for clamd TCP port");
        }
      }
    }

    throw new IllegalStateException(
        "clamd did not open " + host + ":" + port + " within " + timeout);
  }
}
