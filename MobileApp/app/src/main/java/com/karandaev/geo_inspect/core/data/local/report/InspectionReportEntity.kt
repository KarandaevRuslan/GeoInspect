package com.karandaev.geo_inspect.core.data.local.report

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for reports stored in the local database.
 */
@Entity(tableName = "inspection_reports")
data class InspectionReportEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val title: String,
  val content: String,
  val lastUpdatedAtMillis: Long,
  val latitude: Double,
  val longitude: Double
)