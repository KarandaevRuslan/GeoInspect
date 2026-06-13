package com.karandaev.geo_inspect.core.data.repository

import com.karandaev.geo_inspect.core.data.local.report.DefaultInspectionReportsSeed
import com.karandaev.geo_inspect.core.data.local.report.InspectionReportDao
import com.karandaev.geo_inspect.core.data.local.report.toDetectionEntities
import com.karandaev.geo_inspect.core.data.local.report.toDomain
import com.karandaev.geo_inspect.core.data.local.report.toEntityWithDetections
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.domain.model.detection.DetectResponse
import com.karandaev.geo_inspect.core.domain.repository.InspectionReportRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Local Room-backed implementation of [InspectionReportRepository].
 *
 * This class maps between Room entities and domain models so the rest of the app does not
 * depend on database-specific types.
 */
class LocalInspectionReportRepository(
  private val inspectionReportDao: InspectionReportDao
) : InspectionReportRepository {

  /**
   * Observes all reports from the local database.
   */
  override fun observeInspectionReports(): Flow<List<InspectionReport>> {
    return inspectionReportDao.observeInspectionReports()
      .map { entities ->
        entities.map { entity -> entity.toDomain() }
      }
  }

  /**
   * Observes a single report from the local database.
   */
  override fun observeInspectionReportById(id: Long): Flow<InspectionReport?> {
    return inspectionReportDao.observeInspectionReportById(id)
      .map { entity -> entity?.toDomain() }
  }

  /**
   * Returns a report from the local database.
   */
  override suspend fun getInspectionReportById(id: Long): InspectionReport? {
    return inspectionReportDao.getInspectionReportById(id)?.toDomain()
  }

  /**
   * Inserts a report into the local database.
   */
  override suspend fun insert(inspectionReport: InspectionReport): Long {
    return inspectionReportDao.insert(
      inspectionReport
        .copy(id = 0L)
        .toEntityWithDetections()
    )
  }

  /**
   * Inserts multiple reports into the local database.
   */
  override suspend fun insertAll(
    inspectionReports: List<InspectionReport>
  ): List<Long> {
    return inspectionReportDao.insertAll(
      inspectionReports.map { inspectionReport ->
        inspectionReport
          .copy(id = 0L)
          .toEntityWithDetections()
      }
    )
  }

  /**
   * Updates a report in the local database.
   */
  override suspend fun update(inspectionReport: InspectionReport) {
    inspectionReportDao.update(
      inspectionReport.toEntityWithDetections()
    )
  }

  /**
   * Updates detect response for an existing report.
   */
  override suspend fun updateDetectResponse(
    inspectionReportId: Long,
    detectResponse: DetectResponse?
  ) {
    inspectionReportDao.updateDetectResponse(
      inspectionReportId = inspectionReportId,
      detections = detectResponse.toDetectionEntities(
        inspectionReportId = inspectionReportId
      )
    )
  }

  /**
   * Deletes a report by id from the local database.
   */
  override suspend fun deleteById(id: Long) {
    inspectionReportDao.deleteById(id)
  }

  /**
   * Replaces all reports with the default seed reports.
   */
  override suspend fun resetToDefaults() {
    inspectionReportDao.replaceAll(
      DefaultInspectionReportsSeed.create()
    )
  }
}