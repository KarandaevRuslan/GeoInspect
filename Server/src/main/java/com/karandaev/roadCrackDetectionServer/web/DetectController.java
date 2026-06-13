package com.karandaev.roadCrackDetectionServer.web;

import com.karandaev.roadCrackDetectionServer.image.model.SafeImage;
import com.karandaev.roadCrackDetectionServer.service.ImageValidationService;
import com.karandaev.roadCrackDetectionServer.yolo.YoloClient;
import com.karandaev.roadCrackDetectionServer.yolo.dto.DetectResponse;
import com.karandaev.roadCrackDetectionServer.yolo.dto.YoloInferResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * REST controller for image-based road crack detection.
 *
 * <p>The controller accepts an uploaded image, validates and normalizes it, forwards the normalized
 * image to the YOLO inference client, and returns detected objects to the client.
 */
@RestController
@RequestMapping("/v1")
public class DetectController {

  private final ImageValidationService imageValidationService;
  private final YoloClient yoloClient;

  /**
   * Creates a detection controller.
   *
   * @param imageValidationService service used to validate and normalize uploaded images
   * @param yoloClient client used to call YOLO inference
   */
  public DetectController(ImageValidationService imageValidationService, YoloClient yoloClient) {
    this.imageValidationService = imageValidationService;
    this.yoloClient = yoloClient;
  }

  /**
   * Handles image upload and runs road crack detection on the uploaded file.
   *
   * @param file multipart image file uploaded under the {@code file} part name
   * @return detection response containing image metadata and YOLO detections
   * @throws IOException if the uploaded file bytes cannot be read
   */
  @PostMapping(value = "/detect", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public DetectResponse detect(@RequestPart("file") MultipartFile file) throws IOException {
    byte[] bytes = file.getBytes();

    // 1. Validate safety constraints and normalize the image by re-encoding it.
    SafeImage safe = imageValidationService.validateAndNormalize(bytes);

    // 2. Send the normalized image to the YOLO inference service.
    YoloInferResponse yolo = yoloClient.infer(safe.normalizedBytes(), safe.format());

    // 3. Return normalized image metadata and model detections to the client.
    return new DetectResponse(safe.width(), safe.height(), safe.format(), yolo.detections());
  }
}
