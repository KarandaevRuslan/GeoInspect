package com.karandaev.roadCrackDetectionServer.yolo.http;

import com.karandaev.roadCrackDetectionServer.yolo.YoloClient;
import com.karandaev.roadCrackDetectionServer.yolo.YoloClientException;
import com.karandaev.roadCrackDetectionServer.yolo.dto.YoloInferResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

/**
 * HTTP implementation of {@link YoloClient}.
 *
 * <p>This client sends normalized image bytes to the configured YOLO inference service and expects
 * a JSON response containing detection results.
 */
@Component
public class YoloHttpClient implements YoloClient {

  private final WebClient webClient;
  private final String detectPath;
  private final Duration timeout;

  /**
   * Creates an HTTP YOLO client.
   *
   * @param yoloWebClient WebClient configured with the YOLO service base URL
   * @param detectPath path of the YOLO detection endpoint
   * @param timeoutMs request timeout in milliseconds
   */
  public YoloHttpClient(
      WebClient yoloWebClient,
      @Value("${yolo.detect-path}") String detectPath,
      @Value("${yolo.timeout-ms}") long timeoutMs) {
    this.webClient = yoloWebClient;
    this.detectPath = detectPath;
    this.timeout = Duration.ofMillis(timeoutMs);
  }

  /**
   * Sends an image to the YOLO inference service and returns detected objects.
   *
   * @param imageBytes normalized image bytes
   * @param imageFormat normalized image format, for example {@code PNG}, {@code JPEG}, or {@code
   *     WEBP}
   * @return YOLO inference response
   * @throws YoloClientException if the HTTP call fails, times out, or cannot be decoded
   */
  @Override
  public YoloInferResponse infer(byte[] imageBytes, String imageFormat) {
    MediaType contentType =
        switch (imageFormat.toUpperCase()) {
          case "JPEG", "JPG" -> MediaType.IMAGE_JPEG;
          case "PNG" -> MediaType.IMAGE_PNG;
          case "WEBP" -> MediaType.valueOf("image/webp");
          default -> MediaType.APPLICATION_OCTET_STREAM;
        };

    try {
      YoloInferResponse response =
          webClient
              .post()
              .uri(detectPath)
              .contentType(contentType)
              .accept(MediaType.APPLICATION_JSON)
              .bodyValue(imageBytes)
              .retrieve()
              .bodyToMono(YoloInferResponse.class)
              .timeout(timeout)
              .block();

      if (response == null) {
        throw new YoloClientException("YOLO inference service returned an empty response");
      }

      return response;
    } catch (YoloClientException e) {
      throw e;
    } catch (Exception e) {
      throw new YoloClientException("YOLO inference call failed", e);
    }
  }
}
