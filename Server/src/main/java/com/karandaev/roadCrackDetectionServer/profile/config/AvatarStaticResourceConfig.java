package com.karandaev.roadCrackDetectionServer.profile.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

/** Serves public avatar files from local disk. */
@Configuration
public class AvatarStaticResourceConfig implements WebMvcConfigurer {

  private final AvatarStorageProperties properties;

  public AvatarStaticResourceConfig(AvatarStorageProperties properties) {
    this.properties = properties;
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    Path avatarDir = Path.of(properties.getDirectory()).toAbsolutePath().normalize();

    registry
        .addResourceHandler("/public/avatars/**")
        .addResourceLocations(avatarDir.toUri().toString());
  }
}
