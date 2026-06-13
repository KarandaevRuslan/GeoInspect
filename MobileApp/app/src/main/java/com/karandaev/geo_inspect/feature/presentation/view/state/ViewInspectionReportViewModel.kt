package com.karandaev.geo_inspect.feature.presentation.view.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private object ViewInspectionReportSavedStateKeys {

  const val REPORT_ID = "view_inspection_report_report_id"
  const val DETECTION_PREVIEW_DIALOG_VISIBLE =
    "view_inspection_report_detection_preview_dialog_visible"
  const val DELETE_DIALOG_VISIBLE = "view_inspection_report_delete_dialog_visible"
  const val DELETE_CONFIRMATION_TEXT = "view_inspection_report_delete_confirmation_text"
}

/**
 * UI state holder for inspection report details route.
 *
 * Stores dialog state in [SavedStateHandle], so it survives configuration changes.
 */
class ViewInspectionReportViewModel(
  private val savedStateHandle: SavedStateHandle
) : ViewModel() {

  private var currentReportId by mutableStateOf(
    savedStateHandle.get<Long>(ViewInspectionReportSavedStateKeys.REPORT_ID)
  )

  var isDetectionPreviewDialogVisible by mutableStateOf(
    savedStateHandle[
      ViewInspectionReportSavedStateKeys.DETECTION_PREVIEW_DIALOG_VISIBLE
    ] ?: false
  )
    private set

  var isDeleteDialogVisible by mutableStateOf(
    savedStateHandle[
      ViewInspectionReportSavedStateKeys.DELETE_DIALOG_VISIBLE
    ] ?: false
  )
    private set

  var deleteConfirmationText by mutableStateOf(
    savedStateHandle[
      ViewInspectionReportSavedStateKeys.DELETE_CONFIRMATION_TEXT
    ] ?: ""
  )
    private set

  /**
   * Binds this state holder to currently displayed report.
   *
   * If report id changes, transient dialog state is cleared.
   */
  fun bindInspectionReport(
    reportId: Long
  ) {
    if (currentReportId == reportId) {
      return
    }

    if (currentReportId == null) {
      currentReportId = reportId
      savedStateHandle[ViewInspectionReportSavedStateKeys.REPORT_ID] = reportId
      return
    }

    currentReportId = reportId
    savedStateHandle[ViewInspectionReportSavedStateKeys.REPORT_ID] = reportId

    hideDetectionPreviewDialog()
    dismissDeleteDialog()
  }

  /**
   * Shows enlarged detection preview dialog.
   */
  fun showDetectionPreviewDialog() {
    isDetectionPreviewDialogVisible = true

    savedStateHandle[
      ViewInspectionReportSavedStateKeys.DETECTION_PREVIEW_DIALOG_VISIBLE
    ] = true
  }

  /**
   * Hides enlarged detection preview dialog.
   */
  fun hideDetectionPreviewDialog() {
    isDetectionPreviewDialogVisible = false

    savedStateHandle[
      ViewInspectionReportSavedStateKeys.DETECTION_PREVIEW_DIALOG_VISIBLE
    ] = false
  }

  /**
   * Shows delete confirmation dialog.
   */
  fun showDeleteDialog() {
    deleteConfirmationText = ""
    isDeleteDialogVisible = true

    savedStateHandle[
      ViewInspectionReportSavedStateKeys.DELETE_CONFIRMATION_TEXT
    ] = ""

    savedStateHandle[
      ViewInspectionReportSavedStateKeys.DELETE_DIALOG_VISIBLE
    ] = true
  }

  /**
   * Hides delete confirmation dialog and clears confirmation input.
   */
  fun dismissDeleteDialog() {
    deleteConfirmationText = ""
    isDeleteDialogVisible = false

    savedStateHandle[
      ViewInspectionReportSavedStateKeys.DELETE_CONFIRMATION_TEXT
    ] = ""

    savedStateHandle[
      ViewInspectionReportSavedStateKeys.DELETE_DIALOG_VISIBLE
    ] = false
  }

  /**
   * Updates delete confirmation input.
   */
  fun updateDeleteConfirmationText(
    value: String
  ) {
    deleteConfirmationText = value

    savedStateHandle[
      ViewInspectionReportSavedStateKeys.DELETE_CONFIRMATION_TEXT
    ] = value
  }

  /**
   * Clears delete confirmation input.
   */
  fun clearDeleteConfirmationText() {
    updateDeleteConfirmationText("")
  }
}