package com.karandaev.geo_inspect.core.data.json.report

import com.squareup.moshi.JsonClass

/**
 * JSON model representing a geographic point.
 *
 * This model keeps JSON serialization independent from domain models.
 */
@JsonClass(generateAdapter = true)
data class MapPointJson(
  val latitude: Double,
  val longitude: Double
)