package com.karandaev.geo_inspect.core.data.json.report

import com.squareup.moshi.JsonClass

/**
 * JSON model representing an inspection report.
 *
 * This model is used only for serialization and deserialization.
 * It should not be used directly by the domain or UI layers.
 */
@JsonClass(generateAdapter = true)
data class InspectionReportJson(
  val id: Long = 0,
  val title: String,
  val content: String,
  val lastUpdatedAtMillis: Long,
  val point: MapPointJson,
  val detectResponse: DetectResponseJson? = null
)