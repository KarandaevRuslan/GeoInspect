package com.karandaev.geo_inspect.core.data.json.report

import com.squareup.moshi.JsonClass

/**
 * JSON model representing normalized detection bounding box.
 */
@JsonClass(generateAdapter = true)
data class NormalizedBBoxJson(
  val xMin: Double,
  val yMin: Double,
  val xMax: Double,
  val yMax: Double
)