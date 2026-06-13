package com.karandaev.roadCrackDetectionServer.image.model;

/**
 * Result of successful image validation and normalization.
 *
 * @param normalizedBytes bytes of the re-encoded image
 * @param width image width in pixels
 * @param height image height in pixels
 * @param format normalization output format, for example {@code PNG} or {@code JPEG}
 */
public record SafeImage(byte[] normalizedBytes, int width, int height, String format) {}
