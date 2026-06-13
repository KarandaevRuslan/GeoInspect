package com.karandaev.geo_inspect.core.data.local.report

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * DAO for accessing inspection reports in the local database.
 */
@Dao
interface InspectionReportDao {

  /**
   * Observes all reports ordered by last update time.
   */
  @Transaction
  @Query("SELECT * FROM inspection_reports ORDER BY lastUpdatedAtMillis DESC")
  fun observeInspectionReports(): Flow<List<InspectionReportWithDetections>>

  /**
   * Observes a report by id.
   */
  @Transaction
  @Query("SELECT * FROM inspection_reports WHERE id = :id")
  fun observeInspectionReportById(id: Long): Flow<InspectionReportWithDetections?>

  /**
   * Returns a report by id.
   */
  @Transaction
  @Query("SELECT * FROM inspection_reports WHERE id = :id")
  suspend fun getInspectionReportById(id: Long): InspectionReportWithDetections?

  /**
   * Inserts a report with its detections.
   */
  @Transaction
  suspend fun insert(inspectionReport: InspectionReportWithDetections): Long {
    val reportId = insertReport(inspectionReport.report)

    replaceDetections(
      inspectionReportId = reportId,
      detections = inspectionReport.detections
    )

    return reportId
  }

  /**
   * Inserts multiple reports with their detections.
   */
  @Transaction
  suspend fun insertAll(
    inspectionReports: List<InspectionReportWithDetections>
  ): List<Long> {
    return inspectionReports.map { inspectionReport ->
      insert(inspectionReport)
    }
  }

  /**
   * Updates a report and replaces its detections.
   */
  @Transaction
  suspend fun update(inspectionReport: InspectionReportWithDetections) {
    updateReport(inspectionReport.report)

    replaceDetections(
      inspectionReportId = inspectionReport.report.id,
      detections = inspectionReport.detections
    )
  }

  /**
   * Replaces detect response for a report.
   */
  @Transaction
  suspend fun updateDetectResponse(
    inspectionReportId: Long,
    detections: List<DetectionEntity>
  ) {
    replaceDetections(
      inspectionReportId = inspectionReportId,
      detections = detections
    )
  }

  /**
   * Deletes a report by id.
   */
  @Query("DELETE FROM inspection_reports WHERE id = :id")
  suspend fun deleteById(id: Long)

  /**
   * Deletes all reports.
   *
   * Detection rows are deleted automatically by foreign key cascade.
   */
  @Query("DELETE FROM inspection_reports")
  suspend fun deleteAll()

  /**
   * Replaces all reports in one transaction.
   */
  @Transaction
  suspend fun replaceAll(
    inspectionReports: List<InspectionReportWithDetections>
  ) {
    deleteAll()
    insertAll(inspectionReports)
  }

  /**
   * Inserts only the report entity.
   */
  @Insert(onConflict = OnConflictStrategy.ABORT)
  suspend fun insertReport(inspectionReport: InspectionReportEntity): Long

  /**
   * Inserts detection entities.
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertDetections(detections: List<DetectionEntity>): List<Long>

  /**
   * Updates only the report entity.
   */
  @Update
  suspend fun updateReport(inspectionReport: InspectionReportEntity)

  /**
   * Deletes detections for the provided report.
   */
  @Query("DELETE FROM inspection_report_detections WHERE inspectionReportId = :inspectionReportId")
  suspend fun deleteDetectionsByReportId(inspectionReportId: Long)

  /**
   * Replaces all detections for the provided report.
   */
  @Transaction
  suspend fun replaceDetections(
    inspectionReportId: Long,
    detections: List<DetectionEntity>
  ) {
    deleteDetectionsByReportId(inspectionReportId)

    if (detections.isNotEmpty()) {
      insertDetections(
        detections.map { detection ->
          detection.copy(
            id = 0,
            inspectionReportId = inspectionReportId
          )
        }
      )
    }
  }
}