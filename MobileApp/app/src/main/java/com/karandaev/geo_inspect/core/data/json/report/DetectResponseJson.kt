package com.karandaev.geo_inspect.core.data.json.report

import com.squareup.moshi.JsonClass

/**
 * JSON model representing detection response.
 */
@JsonClass(generateAdapter = true)
data class DetectResponseJson(
  val detections: List<DetectionJson> = emptyList()
)