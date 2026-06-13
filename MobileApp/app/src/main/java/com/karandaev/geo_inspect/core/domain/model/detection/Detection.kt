package com.karandaev.geo_inspect.core.domain.model.detection

/**
 * Detected object with its class label, confidence score, and bounding box.
 */
data class Detection(
  val clazz: String,
  val confidence: Double,
  val bbox: NormalizedBBox
)