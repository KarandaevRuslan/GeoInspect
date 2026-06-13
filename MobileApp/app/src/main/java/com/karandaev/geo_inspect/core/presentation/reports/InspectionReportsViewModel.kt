package com.karandaev.geo_inspect.core.presentation.reports

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.domain.model.detection.DetectResponse
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import com.karandaev.geo_inspect.core.domain.repository.InspectionReportRepository
import com.karandaev.geo_inspect.core.image.report.store_and_resolve.InspectionReportImageStorage
import com.karandaev.geo_inspect.core.presentation.viewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val InspectionReportsSharingTimeoutMillis = 5_000L

/**
 * ViewModel for managing reports.
 *
 * Exposes a reactive list of reports and handles report creation, updates, deletion,
 * and resetting reports to seed defaults.
 */
class InspectionReportsViewModel(
  private val inspectionReportRepository: InspectionReportRepository
) : ViewModel() {

  /**
   * Reactive list of all reports.
   */
  val inspectionReports: StateFlow<List<InspectionReport>> =
    inspectionReportRepository.observeInspectionReports()
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(InspectionReportsSharingTimeoutMillis),
        initialValue = emptyList()
      )

  /**
   * Returns a report by id.
   */
  fun getInspectionReportById(
    id: Long,
    onResult: (InspectionReport?) -> Unit
  ) {
    viewModelScope.launch {
      onResult(inspectionReportRepository.getInspectionReportById(id))
    }
  }

  /**
   * Creates a new report.
   */
  fun addInspectionReport(
    title: String,
    content: String,
    point: MapPoint,
    detectResponse: DetectResponse? = null
  ) {
    viewModelScope.launch {
      inspectionReportRepository.insert(
        InspectionReport(
          title = title,
          content = content,
          lastUpdatedAtMillis = System.currentTimeMillis(),
          point = point,
          detectResponse = detectResponse
        )
      )
    }
  }

  /**
   * Updates an existing report and refreshes its last-updated timestamp.
   */
  fun updateInspectionReport(inspectionReport: InspectionReport) {
    viewModelScope.launch {
      inspectionReportRepository.update(
        inspectionReport.copy(
          lastUpdatedAtMillis = System.currentTimeMillis()
        )
      )
    }
  }

  /**
   * Deletes a report by id.
   */
  fun deleteInspectionReport(id: Long) {
    viewModelScope.launch {
      inspectionReportRepository.deleteById(id)
    }
  }

  /**
   * Observes a report by id.
   */
  fun observeInspectionReportById(id: Long) =
    inspectionReportRepository.observeInspectionReportById(id)

  /**
   * Saves new or edited inspection report and stores detection image when provided.
   */
  fun saveInspectionReport(
    context: Context,
    editedInspectionReport: InspectionReport?,
    title: String,
    content: String,
    point: MapPoint,
    detectResponse: DetectResponse?,
    detectionImagePath: String?,
    removeDetectionImage: Boolean,
    onSaved: () -> Unit
  ) {
    viewModelScope.launch {
      val savedInspectionReportId = withContext(Dispatchers.IO) {
        val trimmedTitle = title.trim()
        val trimmedContent = content.trim()

        if (editedInspectionReport != null) {
          val updatedInspectionReport = editedInspectionReport.copy(
            title = trimmedTitle,
            content = trimmedContent,
            point = point,
            detectResponse = detectResponse
          )

          inspectionReportRepository.update(updatedInspectionReport)

          updatedInspectionReport.id
        } else {
          inspectionReportRepository.insert(
            InspectionReport(
              title = trimmedTitle,
              content = trimmedContent,
              point = point,
              lastUpdatedAtMillis = System.currentTimeMillis(),
              detectResponse = detectResponse
            )
          )
        }
      }

      withContext(Dispatchers.IO) {
        if (removeDetectionImage) {
          InspectionReportImageStorage.deleteDetectionImageFiles(
            context = context.applicationContext,
            inspectionReportId = savedInspectionReportId
          )
        } else {
          InspectionReportImageStorage.saveDetectionImageFile(
            context = context.applicationContext,
            inspectionReportId = savedInspectionReportId,
            sourcePath = detectionImagePath
          )
        }
      }

      onSaved()
    }
  }

  /**
   * Replaces all reports with default seed reports.
   */
  fun resetToDefaults() {
    viewModelScope.launch {
      inspectionReportRepository.resetToDefaults()
    }
  }

  companion object {

    /**
     * Creates a factory for [InspectionReportsViewModel].
     */
    fun factory(
      inspectionReportRepository: InspectionReportRepository
    ): ViewModelProvider.Factory {
      return viewModelFactory {
        InspectionReportsViewModel(inspectionReportRepository)
      }
    }
  }
}