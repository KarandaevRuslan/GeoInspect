package com.karandaev.geo_inspect.core.inspection_api.dto.detect

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Remote normalized bounding box returned by the road crack detection API.
 *
 * All coordinates are relative to the image size and are expected to be in
 * the range [0.0, 1.0].
 *
 * @param xMin Normalized left X coordinate.
 * @param yMin Normalized top Y coordinate.
 * @param xMax Normalized right X coordinate.
 * @param yMax Normalized bottom Y coordinate.
 */
@JsonClass(generateAdapter = true)
data class NormalizedBBoxDto(
  @Json(name = "xMin")
  val xMin: Double,

  @Json(name = "yMin")
  val yMin: Double,

  @Json(name = "xMax")
  val xMax: Double,

  @Json(name = "yMax")
  val yMax: Double
)