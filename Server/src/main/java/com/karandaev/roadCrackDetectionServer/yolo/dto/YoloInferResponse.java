package com.karandaev.roadCrackDetectionServer.yolo.dto;

import java.util.List;

/**
 * Response returned by the YOLO inference service.
 *
 * @param detections list of detected objects
 */
public record YoloInferResponse(List<Detection> detections) {}
