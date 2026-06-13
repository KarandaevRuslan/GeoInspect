package com.karandaev.geo_inspect.feature.presentation.home.components

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import com.karandaev.geo_inspect.core.domain.model.InspectionReport

internal fun LazyListScope.homeInspectionReportItems(
  inspectionReports: List<InspectionReport>,
  deletingInspectionReportIds: Set<Long>,
  onInspectionReportClick: (Long) -> Unit,
  onInspectionReportEdit: (Long) -> Unit,
  onInspectionReportExport: (InspectionReport) -> Unit,
  onInspectionReportDeleteRequest: (Long) -> Unit,
  onInspectionReportDeleteAnimationFinished: (Long) -> Unit
) {
  items(
    items = inspectionReports,
    key = { inspectionReport -> inspectionReport.id }
  ) { inspectionReport ->
    AnimatedInspectionReportCard(
      inspectionReport = inspectionReport,
      isDeleting = deletingInspectionReportIds.contains(inspectionReport.id),
      onClick = {
        onInspectionReportClick(inspectionReport.id)
      },
      onEdit = {
        onInspectionReportEdit(inspectionReport.id)
      },
      onExport = {
        onInspectionReportExport(inspectionReport)
      },
      onDeleteRequest = {
        onInspectionReportDeleteRequest(inspectionReport.id)
      },
      onDeleteAnimationFinished = {
        onInspectionReportDeleteAnimationFinished(inspectionReport.id)
      }
    )
  }
}