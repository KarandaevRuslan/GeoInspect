package com.karandaev.geo_inspect.core.inspection_api.dto.detect

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Remote road crack detection item.
 *
 * @param clazz Detection class returned by the backend.
 * @param confidence Detection confidence score.
 * @param bbox Detection bounding box.
 */
@JsonClass(generateAdapter = true)
data class DetectionDto(
  @Json(name = "clazz")
  val clazz: String,

  @Json(name = "confidence")
  val confidence: Double,

  @Json(name = "bbox")
  val bbox: NormalizedBBoxDto
)