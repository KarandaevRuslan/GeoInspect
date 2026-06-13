package com.karandaev.geo_inspect.core.domain.repository

import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.domain.model.detection.DetectResponse
import kotlinx.coroutines.flow.Flow

/**
 * Repository abstraction for working with reports.
 *
 * The domain and presentation layers depend on this interface instead of a concrete
 * local database implementation.
 */
interface InspectionReportRepository {

  /**
   * Observes all reports.
   */
  fun observeInspectionReports(): Flow<List<InspectionReport>>

  /**
   * Observes a report by id.
   *
   * Emits null when the report does not exist.
   */
  fun observeInspectionReportById(id: Long): Flow<InspectionReport?>

  /**
   * Returns a report by id.
   *
   * Returns null when the report does not exist.
   */
  suspend fun getInspectionReportById(id: Long): InspectionReport?

  /**
   * Creates a new report.
   *
   * @return Id of the inserted report.
   */
  suspend fun insert(inspectionReport: InspectionReport): Long

  /**
   * Creates multiple new reports.
   *
   * @return Ids of inserted reports.
   */
  suspend fun insertAll(inspectionReports: List<InspectionReport>): List<Long>

  /**
   * Updates an existing report.
   */
  suspend fun update(inspectionReport: InspectionReport)

  /**
   * Updates detect response for an existing report.
   */
  suspend fun updateDetectResponse(
    inspectionReportId: Long,
    detectResponse: DetectResponse?
  )

  /**
   * Deletes a report by id.
   */
  suspend fun deleteById(id: Long)

  /**
   * Replaces all user reports with default seed reports.
   */
  suspend fun resetToDefaults()
}