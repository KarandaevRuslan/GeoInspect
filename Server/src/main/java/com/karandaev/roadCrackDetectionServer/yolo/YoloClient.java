package com.karandaev.roadCrackDetectionServer.yolo;

import com.karandaev.roadCrackDetectionServer.yolo.dto.YoloInferResponse;

/** Client abstraction for YOLO inference. */
public interface YoloClient {

  /**
   * Runs object detection on the provided image bytes.
   *
   * @param imageBytes normalized image bytes
   * @param imageFormat normalized image format, for example {@code PNG} or {@code JPEG}
   * @return YOLO inference response containing detected objects
   */
  YoloInferResponse infer(byte[] imageBytes, String imageFormat);
}
