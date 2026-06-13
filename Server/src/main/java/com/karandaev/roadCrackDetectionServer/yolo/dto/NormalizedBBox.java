package com.karandaev.roadCrackDetectionServer.yolo.dto;

/**
 * Normalized bounding box coordinates of a detected object.
 *
 * <p>All coordinates are relative to the image size and are expected to be in the range {@code
 * [0.0, 1.0]}.
 *
 * @param xMin normalized left X coordinate of the bounding box
 * @param yMin normalized top Y coordinate of the bounding box
 * @param xMax normalized right X coordinate of the bounding box
 * @param yMax normalized bottom Y coordinate of the bounding box
 */
public record NormalizedBBox(double xMin, double yMin, double xMax, double yMax) {}
