package com.karandaev.geo_inspect.core.domain.model.detection

/**
 * Normalized bounding box coordinates of a detected object.
 *
 * All coordinates are relative to the image size and are expected to be in
 * the range [0.0, 1.0].
 */
data class NormalizedBBox(
  val xMin: Double,
  val yMin: Double,
  val xMax: Double,
  val yMax: Double
)