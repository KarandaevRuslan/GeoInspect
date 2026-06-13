package com.karandaev.geo_inspect.core.domain.model.detection

/**
 * Detection response containing detected objects.
 */
data class DetectResponse(
  val detections: List<Detection>
)