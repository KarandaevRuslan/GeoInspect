package com.karandaev.geo_inspect.core.json.import_json.reports

import com.karandaev.geo_inspect.core.data.json.report.InspectionReportJson
import com.karandaev.geo_inspect.core.data.json.report.toDomain
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.json.JSON_SCHEMA_VERSION
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi

/**
 * Parses exported reports JSON using Moshi.
 *
 * @param json Raw JSON document content.
 * @param moshi Moshi instance used for JSON deserialization.
 * @param preserveIds Whether imported reports should keep exported ids.
 * Usually false is safer for importing into an existing database.
 * @return Parsed reports.
 */
fun parseInspectionReportsImportJson(
  json: String,
  moshi: Moshi,
  preserveIds: Boolean = false
): List<InspectionReport> {
  val adapter = moshi.adapter(InspectionReportsImportJson::class.java)
  val importJson = adapter.fromJson(json)
    ?: error("Invalid reports import JSON")

  validateSchemaVersion(importJson.schemaVersion)

  return when {
    importJson.inspectionReports != null -> {
      importJson.inspectionReports.map { reportJson ->
        reportJson.toDomain(
          preserveId = preserveIds
        )
      }
    }

    importJson.inspectionReport != null -> {
      listOf(
        importJson.inspectionReport.toDomain(
          preserveId = preserveIds
        )
      )
    }

    else -> {
      error("JSON does not contain reports")
    }
  }
}

/**
 * Validates notes import schema version.
 */
private fun validateSchemaVersion(schemaVersion: Int) {
  if (schemaVersion != JSON_SCHEMA_VERSION) {
    error("Unsupported notes schema version: $schemaVersion")
  }
}

/**
 * JSON model representing notes import document.
 *
 * Supports both single-note and multi-note import formats.
 */
@JsonClass(generateAdapter = true)
internal data class InspectionReportsImportJson(
  val schemaVersion: Int,
  val inspectionReport: InspectionReportJson? = null,
  val inspectionReports: List<InspectionReportJson>? = null
)

/**
 * Converts a JSON report model to a domain report model.
 *
 * @param preserveId Whether the imported report should keep its exported id.
 */
private fun InspectionReportJson.toDomain(
  preserveId: Boolean
): InspectionReport {
  return toDomain().copy(
    id = if (preserveId) id else 0L
  )
}