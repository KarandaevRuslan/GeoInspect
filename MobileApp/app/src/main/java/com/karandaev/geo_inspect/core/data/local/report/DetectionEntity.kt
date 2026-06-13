package com.karandaev.geo_inspect.core.data.local.report

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room entity for a single detection stored for an inspection report.
 */
@Entity(
  tableName = "inspection_report_detections",
  foreignKeys = [
    ForeignKey(
      entity = InspectionReportEntity::class,
      parentColumns = ["id"],
      childColumns = ["inspectionReportId"],
      onDelete = ForeignKey.CASCADE
    )
  ],
  indices = [
    Index(value = ["inspectionReportId"])
  ]
)
data class DetectionEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val inspectionReportId: Long,
  val sortOrder: Int,
  val clazz: String,
  val confidence: Double,
  val xMin: Double,
  val yMin: Double,
  val xMax: Double,
  val yMax: Double
)