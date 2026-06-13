package com.karandaev.roadCrackDetectionServer;

import com.karandaev.roadCrackDetectionServer.image.config.ImageSecurityProperties;
import com.karandaev.roadCrackDetectionServer.profile.config.AvatarStorageProperties;
import com.karandaev.roadCrackDetectionServer.ratelimit.RateLimitProperties;
import com.karandaev.roadCrackDetectionServer.yolo.process.YoloProcessProperties;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.util.Collections;

/** Main entry point for the Road Crack Detection Spring Boot application. */
@EnableConfigurationProperties({
  ImageSecurityProperties.class,
  YoloProcessProperties.class,
  RateLimitProperties.class,
  AvatarStorageProperties.class
})
@SpringBootApplication
public class App {

  /**
   * Starts the Spring Boot application.
   *
   * @param args command-line arguments passed to the application
   */
  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  /**
   * Prints local network server URLs after application startup.
   *
   * @param environment Spring environment containing server configuration
   * @return startup runner
   */
  @Bean
  public ApplicationRunner printLocalNetworkUrls(Environment environment) {
    return args -> {
      String port = environment.getProperty("server.port", "8080");
      boolean sslEnabled =
          Boolean.parseBoolean(environment.getProperty("server.ssl.enabled", "false"));
      String scheme = sslEnabled ? "https" : "http";

      System.out.println();
      System.out.println("Road Crack Detection Server is running:");

      Collections.list(NetworkInterface.getNetworkInterfaces()).stream()
          .filter(this::isUsableNetworkInterface)
          .flatMap(
              networkInterface -> Collections.list(networkInterface.getInetAddresses()).stream())
          .filter(address -> address instanceof Inet4Address)
          .filter(address -> !address.isLoopbackAddress())
          .map(address -> scheme + "://" + address.getHostAddress() + ":" + port)
          .distinct()
          .forEach(
              url -> {
                System.out.println("  API base URL: " + url);
                System.out.println("  Health URL:   " + url + "/public/health");
              });

      System.out.println();
    };
  }

  private boolean isUsableNetworkInterface(NetworkInterface networkInterface) {
    try {
      return networkInterface.isUp()
          && !networkInterface.isLoopback()
          && !networkInterface.isVirtual();
    } catch (Exception e) {
      return false;
    }
  }
}
