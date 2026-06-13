package com.karandaev.roadCrackDetectionServer.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

/** REST controller that exposes public server health status. */
@RestController
public class HealthController {

  /**
   * Returns basic server health status.
   *
   * <p>This endpoint is public and can be used by clients to check whether the configured backend
   * base URL is reachable.
   *
   * @return server health status
   */
  @GetMapping("/public/health")
  public Map<String, Object> health() {
    return Map.of("status", "ok", "timestamp", Instant.now().toString());
  }
}
