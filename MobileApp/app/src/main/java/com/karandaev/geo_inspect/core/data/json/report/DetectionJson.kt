package com.karandaev.geo_inspect.core.data.json.report

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * JSON model representing a single detected object.
 */
@JsonClass(generateAdapter = true)
data class DetectionJson(
  @Json(name = "class")
  val clazz: String,
  val confidence: Double,
  val bbox: NormalizedBBoxJson
)