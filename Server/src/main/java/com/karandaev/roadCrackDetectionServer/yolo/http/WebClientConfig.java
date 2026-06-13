package com.karandaev.roadCrackDetectionServer.yolo.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/** WebClient configuration for communicating with the YOLO inference service. */
@Configuration
public class WebClientConfig {

  /**
   * Creates a WebClient preconfigured with the YOLO service base URL.
   *
   * @param baseUrl base URL of the YOLO inference service
   * @return configured WebClient instance
   */
  @Bean
  public WebClient yoloWebClient(@Value("${yolo.base-url}") String baseUrl) {
    return WebClient.builder().baseUrl(baseUrl).build();
  }
}
