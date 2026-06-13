package com.karandaev.roadCrackDetectionServer.profile.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/** Avatar storage configuration. */
@Setter
@Getter
@ConfigurationProperties(prefix = "avatar-storage")
public class AvatarStorageProperties {

  /** Directory where avatar files are stored. Example: /home/user/road-crack-server-data/avatars */
  private String directory;

  /** Public base URL used by Android clients. Example: https://192.168.1.10:8443 */
  private String publicBaseUrl;
}
