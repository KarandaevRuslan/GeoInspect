package com.karandaev.geo_inspect.core.data.local.report

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Room relation model containing an inspection report with its detection results.
 */
data class InspectionReportWithDetections(
  @Embedded
  val report: InspectionReportEntity,

  @Relation(
    parentColumn = "id",
    entityColumn = "inspectionReportId"
  )
  val detections: List<DetectionEntity>
)