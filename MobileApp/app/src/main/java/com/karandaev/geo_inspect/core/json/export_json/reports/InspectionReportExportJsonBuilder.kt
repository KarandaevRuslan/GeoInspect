package com.karandaev.geo_inspect.core.json.export_json.reports

import com.karandaev.geo_inspect.core.data.json.report.InspectionReportJson
import com.karandaev.geo_inspect.core.data.json.report.toJson
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.json.JSON_SCHEMA_VERSION
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi

/**
 * Builds a formatted JSON export payload for a single report using Moshi.
 *
 * @param inspectionReport Report that should be exported.
 * @param moshi Moshi instance used for JSON serialization.
 * @return Pretty-printed JSON string ready to be written to storage.
 */
fun buildInspectionReportExportJson(
  inspectionReport: InspectionReport,
  moshi: Moshi
): String {
  val adapter = moshi.adapter(InspectionReportExportJson::class.java).indent("  ")

  return adapter.toJson(
    InspectionReportExportJson(
      schemaVersion = JSON_SCHEMA_VERSION,
      exportedAtMillis = System.currentTimeMillis(),
      inspectionReport = inspectionReport.toJson()
    )
  )
}

/**
 * Builds a formatted JSON export payload for multiple reports using Moshi.
 *
 * @param inspectionReports Reports that should be exported.
 * @param moshi Moshi instance used for JSON serialization.
 * @return Pretty-printed JSON string ready to be written to storage.
 */
fun buildInspectionReportExportJson(
  inspectionReports: List<InspectionReport>,
  moshi: Moshi
): String {
  val adapter = moshi.adapter(InspectionReportsExportJson::class.java).indent("  ")

  return adapter.toJson(
    InspectionReportsExportJson(
      schemaVersion = JSON_SCHEMA_VERSION,
      exportedAtMillis = System.currentTimeMillis(),
      inspectionReports = inspectionReports.map { note ->
        note.toJson()
      }
    )
  )
}

/**
 * JSON model representing a single-report export document.
 */
@JsonClass(generateAdapter = true)
internal data class InspectionReportExportJson(
  val schemaVersion: Int,
  val exportedAtMillis: Long,
  val inspectionReport: InspectionReportJson
)

/**
 * JSON model representing a multi-report export document.
 */
@JsonClass(generateAdapter = true)
internal data class InspectionReportsExportJson(
  val schemaVersion: Int,
  val exportedAtMillis: Long,
  val inspectionReports: List<InspectionReportJson>
)