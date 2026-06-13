package com.karandaev.geo_inspect.feature.presentation.create.state

import com.karandaev.geo_inspect.core.domain.model.InspectionReport

internal fun createInspectionReportFormViewModelKey(
  editedInspectionReport: InspectionReport?,
  prefilledLatitude: Double?,
  prefilledLongitude: Double?
): String {
  return if (editedInspectionReport != null) {
    "edit-note-form-${editedInspectionReport.id}"
  } else {
    "create-note-form-${prefilledLatitude.toKeyPart("no-lat")}-${prefilledLongitude.toKeyPart("no-lon")}"
  }
}

private fun Double?.toKeyPart(
  fallback: String
): String {
  return this?.toString() ?: fallback
}