package com.karandaev.roadCrackDetectionServer.yolo.dto;

import java.util.List;

/**
 * API response returned to the client after image detection.
 *
 * @param imageWidth normalized image width in pixels
 * @param imageHeight normalized image height in pixels
 * @param imageFormat normalized image format, for example {@code PNG} or {@code JPEG}
 * @param detections list of detected objects
 */
public record DetectResponse(
    int imageWidth, int imageHeight, String imageFormat, List<Detection> detections) {}
