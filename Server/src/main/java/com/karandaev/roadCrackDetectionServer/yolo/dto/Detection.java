package com.karandaev.roadCrackDetectionServer.yolo.dto;

/**
 * Single object detection returned by the YOLO inference service.
 *
 * @param clazz detected class name. The name {@code clazz} is used because {@code class} is a
 *     reserved keyword in Java
 * @param confidence detection confidence score in the range from {@code 0.0} to {@code 1.0}
 * @param bbox bounding box of the detected object
 */
public record Detection(String clazz, double confidence, NormalizedBBox bbox) {}
