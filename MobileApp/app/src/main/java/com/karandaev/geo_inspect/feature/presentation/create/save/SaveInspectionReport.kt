package com.karandaev.geo_inspect.feature.presentation.create.save

import android.content.Context
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.domain.model.detection.DetectResponse
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import com.karandaev.geo_inspect.core.presentation.reports.InspectionReportsViewModel

/**
 * Saves form data as a new inspection report or updates an existing inspection report.
 *
 * Detection image deletion is intentionally delayed until save.
 */
internal fun saveInspectionReport(
  context: Context,
  inspectionReportsViewModel: InspectionReportsViewModel,
  editedInspectionReport: InspectionReport?,
  title: String,
  content: String,
  point: MapPoint,
  detectResponse: DetectResponse?,
  detectionImagePath: String?,
  removeDetectionImage: Boolean,
  onSaved: () -> Unit
) {
  inspectionReportsViewModel.saveInspectionReport(
    context = context,
    editedInspectionReport = editedInspectionReport,
    title = title,
    content = content,
    point = point,
    detectResponse = detectResponse,
    detectionImagePath = detectionImagePath,
    removeDetectionImage = removeDetectionImage,
    onSaved = onSaved
  )
}