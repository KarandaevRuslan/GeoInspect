package com.karandaev.roadCrackDetectionServer.image.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Configuration properties for image upload security.
 *
 * <p>Properties are loaded from the {@code image-security} prefix in {@code application.yml} or
 * {@code application.properties}.
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "image-security")
public class ImageSecurityProperties {
  private long maxBytes = 10 * 1024 * 1024;
  private int maxWidth = 4096;
  private int maxHeight = 4096;
  private int maxMegapixels = 20;
  private List<String> allowedFormats = List.of("JPEG", "PNG", "WEBP");
  private String normalizeFormat = "PNG";

  private Antivirus antivirus = new Antivirus();

  /** Configuration properties for optional ClamAV-based antivirus scanning. */
  @Setter
  @Getter
  public static class Antivirus {
    private boolean enabled = false;

    private String host = "127.0.0.1";
    private int port = 3310;
    private int timeoutMs = 2000;

    private boolean startClamd = false;

    private String clamdExe;
    private String clamdConf;

    private int startupTimeoutMs = 15000;
  }
}
