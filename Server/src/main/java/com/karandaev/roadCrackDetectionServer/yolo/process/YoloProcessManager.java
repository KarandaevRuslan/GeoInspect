package com.karandaev.roadCrackDetectionServer.yolo.process;

import com.karandaev.roadCrackDetectionServer.util.ProcessOutputGobbler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Manages the lifecycle of an external Python YOLO inference service.
 *
 * <p>When enabled, this component starts the configured Python process during Spring application
 * startup, waits until its health endpoint becomes available, and stops the process during
 * application shutdown.
 *
 * <p>The process stdout and stderr streams are consumed asynchronously to prevent the child process
 * from blocking when its output buffers become full.
 */
@Component
public class YoloProcessManager implements SmartLifecycle {
  private static final Logger log = LoggerFactory.getLogger(YoloProcessManager.class);

  private final YoloProcessProperties props;
  private volatile boolean running = false;

  private Process process;
  private Future<?> stdoutTask;
  private Future<?> stderrTask;

  /**
   * Creates a YOLO process manager.
   *
   * @param props YOLO process configuration properties
   */
  public YoloProcessManager(YoloProcessProperties props) {
    this.props = props;
  }

  /** Starts the external YOLO Python process and waits until its health endpoint is ready. */
  @Override
  public void start() {
    if (!props.isEnabled()) {
      log.info("YOLO process manager disabled");
      running = true;
      return;
    }
    if (running) return;

    try {
      startProcess();
      waitForHealth();
      running = true;
      log.info("YOLO python process is up");
    } catch (Exception e) {
      stopProcessHard();
      throw new IllegalStateException("Failed to start YOLO python service: " + e.getMessage(), e);
    }
  }

  /**
   * Validates configuration and starts the configured Python process.
   *
   * @throws Exception if the working directory, Python executable, or process startup fails
   */
  private void startProcess() throws Exception {
    File workdir = new File(props.getWorkdir());
    if (!workdir.isDirectory()) {
      throw new IllegalStateException("workdir not found: " + workdir.getAbsolutePath());
    }

    if (props.getPythonExe() == null || props.getPythonExe().isBlank()) {
      throw new IllegalStateException("python-exe is not set");
    }

    String pythonExe =
        props.getPythonExe().trim().replace("/", File.separator).replace("\\", File.separator);

    File pythonFile = new File(workdir, pythonExe);

    log.info("pythonFile abs={}", pythonFile.getAbsolutePath());
    log.info("pythonFile exists={}", pythonFile.isFile());

    if (!pythonFile.isFile()) {
      throw new IllegalStateException("python.exe not found: " + pythonFile.getAbsolutePath());
    }

    var cmd = new ArrayList<String>();
    cmd.add(pythonFile.getAbsolutePath());
    if (props.getArgs() != null) cmd.addAll(props.getArgs());

    ProcessBuilder pb = new ProcessBuilder(cmd);
    pb.directory(workdir);
    pb.redirectErrorStream(false);

    log.info(
        "Starting YOLO python process: workdir={}, cmd={}",
        workdir.getAbsolutePath(),
        String.join(" ", cmd));

    process = pb.start();

    var pool = Executors.newFixedThreadPool(2);
    stdoutTask =
        pool.submit(new ProcessOutputGobbler(process.getInputStream(), log, "[py-stdout] "));
    stderrTask =
        pool.submit(new ProcessOutputGobbler(process.getErrorStream(), log, "[py-stderr] "));
  }

  /** Waits until the YOLO service health endpoint returns a successful HTTP response. */
  private void waitForHealth() {
    // RestTemplate is used here as a simple blocking HTTP client for polling /health.
    SimpleClientHttpRequestFactory f = new SimpleClientHttpRequestFactory();
    f.setConnectTimeout(1000);
    f.setReadTimeout(1000);
    RestTemplate rt = new RestTemplate(f);

    Instant deadline = Instant.now().plusMillis(props.getStartupTimeoutMs());

    while (Instant.now().isBefore(deadline)) {
      // Stop waiting immediately if the Python process exits before becoming healthy.
      if (process != null && !process.isAlive()) {
        int code = process.exitValue();
        throw new IllegalStateException("Python process exited early with code " + code);
      }

      try {
        var resp = rt.getForEntity(props.getHealthUrl(), String.class);
        if (resp.getStatusCode().is2xxSuccessful()) return;
      } catch (Exception ignored) {
        // The service may still be starting.
      }

      try {
        Thread.sleep(200);
      } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
        throw new IllegalStateException("Interrupted while waiting for health");
      }
    }

    throw new IllegalStateException("Health check timeout: " + props.getHealthUrl());
  }

  /** Stops the external YOLO process if process management is enabled. */
  @Override
  public void stop() {
    if (!props.isEnabled()) {
      running = false;
      return;
    }
    stopProcessGracefully();
    running = false;
  }

  /** Attempts to stop the Python process gracefully and falls back to a forced stop if needed. */
  private void stopProcessGracefully() {
    if (process == null) return;

    log.info("Stopping YOLO python process...");
    process.destroy();

    Instant deadline = Instant.now().plusMillis(props.getShutdownTimeoutMs());
    while (process.isAlive() && Instant.now().isBefore(deadline)) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }

    if (process.isAlive()) {
      log.warn("YOLO python process did not stop in time, killing...");
      stopProcessHard();
    }
  }

  /** Forces the Python process to stop and cancels output reader tasks. */
  private void stopProcessHard() {
    try {
      if (process != null && process.isAlive()) {
        process.destroyForcibly();
      }
    } catch (Exception ignored) {
    }

    process = null;
    if (stdoutTask != null) stdoutTask.cancel(true);
    if (stderrTask != null) stderrTask.cancel(true);
  }

  /**
   * Returns whether the process manager is currently running.
   *
   * @return {@code true} if the manager is running
   */
  @Override
  public boolean isRunning() {
    return running;
  }

  /**
   * Returns the lifecycle phase.
   *
   * <p>A lower phase starts earlier and stops later. This manager starts early so the YOLO service
   * is available before dependent components receive traffic.
   *
   * @return lifecycle phase value
   */
  @Override
  public int getPhase() {
    return Integer.MIN_VALUE + 1000;
  }

  /**
   * Enables automatic startup by the Spring container.
   *
   * @return {@code true}
   */
  @Override
  public boolean isAutoStartup() {
    return true;
  }

  /**
   * Stops the manager and then invokes the Spring lifecycle callback.
   *
   * @param callback callback invoked after stop completes
   */
  @Override
  public void stop(Runnable callback) {
    stop();
    callback.run();
  }
}
