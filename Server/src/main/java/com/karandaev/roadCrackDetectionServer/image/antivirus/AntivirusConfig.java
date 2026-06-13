package com.karandaev.roadCrackDetectionServer.image.antivirus;

import com.karandaev.roadCrackDetectionServer.image.config.ImageSecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for antivirus scanning.
 *
 * <p>The configuration creates either a real ClamAV-based scanner or a no-op scanner, depending on
 * the {@code image-security.antivirus.enabled} property.
 *
 * <p>When both {@code image-security.antivirus.enabled} and {@code
 * image-security.antivirus.start-clamd} are set to {@code true}, the configuration also creates a
 * {@link ClamdLifecycle} bean that starts and stops a local {@code clamd} process together with the
 * application.
 */
@Configuration
@EnableConfigurationProperties(ImageSecurityProperties.class)
public class AntivirusConfig {

  /**
   * Creates the virus scanner used by image validation.
   *
   * @param props image security configuration properties
   * @return ClamAV scanner when antivirus scanning is enabled, otherwise a no-op scanner
   */
  @Bean
  public VirusScanner virusScanner(ImageSecurityProperties props) {
    var av = props.getAntivirus();
    if (!av.isEnabled()) {
      return new NoopVirusScanner();
    }
    return new ClamAvVirusScanner(av.getHost(), av.getPort(), av.getTimeoutMs());
  }

  /**
   * Creates a lifecycle bean that starts a local {@code clamd} process on application startup.
   *
   * @param props image security configuration properties
   * @return lifecycle manager for the local {@code clamd} process
   */
  @Bean
  @ConditionalOnProperty(
      prefix = "image-security.antivirus",
      name = {"enabled", "start-clamd"},
      havingValue = "true")
  public ClamdLifecycle clamdLifecycle(ImageSecurityProperties props) {
    var av = props.getAntivirus();
    return new ClamdLifecycle(
        av.getClamdExe(), av.getClamdConf(), av.getHost(), av.getPort(), av.getStartupTimeoutMs());
  }
}
