package com.karandaev.geo_inspect.feature.presentation.create.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private object CreateInspectionReportSavedStateKeys {
  const val DETECTION_PREVIEW_DIALOG_VISIBLE =
    "create_inspection_report_detection_preview_dialog_visible"
}

/**
 * UI state holder for create/edit inspection report route.
 *
 * Keeps transient screen UI state outside the form state.
 */
class CreateInspectionReportViewModel(
  private val savedStateHandle: SavedStateHandle
) : ViewModel() {

  var isDetectionPreviewDialogVisible by mutableStateOf(
    savedStateHandle[
      CreateInspectionReportSavedStateKeys.DETECTION_PREVIEW_DIALOG_VISIBLE
    ] ?: false
  )
    private set

  /**
   * Shows enlarged detection preview dialog.
   */
  fun showDetectionPreviewDialog() {
    isDetectionPreviewDialogVisible = true
    savedStateHandle[
      CreateInspectionReportSavedStateKeys.DETECTION_PREVIEW_DIALOG_VISIBLE
    ] = true
  }

  /**
   * Hides enlarged detection preview dialog.
   */
  fun hideDetectionPreviewDialog() {
    isDetectionPreviewDialogVisible = false
    savedStateHandle[
      CreateInspectionReportSavedStateKeys.DETECTION_PREVIEW_DIALOG_VISIBLE
    ] = false
  }
}