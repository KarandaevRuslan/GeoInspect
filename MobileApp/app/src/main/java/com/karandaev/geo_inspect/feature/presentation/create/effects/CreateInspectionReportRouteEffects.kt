package com.karandaev.geo_inspect.feature.presentation.create.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import com.karandaev.geo_inspect.feature.presentation.create.state.CreateInspectionReportFormViewModel

/**
 * Applies route-level effects for create/edit report flow.
 *
 * Effects handled here:
 * - loading edited report data into the form;
 * - loading edited report detection data into the form;
 * - requesting initial map focus after current location becomes available.
 *
 * @param editedInspectionReport Existing report when edit mode is active.
 * @param editedInspectionReportImagePath Existing report detection image path.
 * @param isEditMode Whether the screen is opened in edit mode.
 * @param initialSelectedPoint Point selected before opening the screen.
 * @param currentLocationPoint Current device location converted into a map point.
 * @param formState Mutable form state owned by the route.
 */
@Composable
internal fun CreateInspectionReportRouteEffects(
  editedInspectionReport: InspectionReport?,
  editedInspectionReportImagePath: String?,
  isEditMode: Boolean,
  initialSelectedPoint: MapPoint?,
  currentLocationPoint: MapPoint?,
  formState: CreateInspectionReportFormViewModel
) {
  ApplyEditedInspectionReportEffect(
    editedInspectionReport = editedInspectionReport,
    editedInspectionReportImagePath = editedInspectionReportImagePath,
    formState = formState
  )

  RequestInitialCurrentLocationFocusEffect(
    isEditMode = isEditMode,
    initialSelectedPoint = initialSelectedPoint,
    currentLocationPoint = currentLocationPoint,
    formState = formState
  )
}

/**
 * Applies edited report data to the form once.
 */
@Composable
private fun ApplyEditedInspectionReportEffect(
  editedInspectionReport: InspectionReport?,
  editedInspectionReportImagePath: String?,
  formState: CreateInspectionReportFormViewModel
) {
  LaunchedEffect(
    editedInspectionReport,
    editedInspectionReportImagePath
  ) {
    formState.applyLoadedInspectionReportIfNeeded(
      inspectionReport = editedInspectionReport,
      detectionImagePath = editedInspectionReportImagePath
    )
  }
}

/**
 * Requests map focus once current location becomes available in create mode.
 */
@Composable
private fun RequestInitialCurrentLocationFocusEffect(
  isEditMode: Boolean,
  initialSelectedPoint: MapPoint?,
  currentLocationPoint: MapPoint?,
  formState: CreateInspectionReportFormViewModel
) {
  LaunchedEffect(currentLocationPoint) {
    if (
      shouldFocusCurrentLocation(
        isEditMode = isEditMode,
        initialSelectedPoint = initialSelectedPoint,
        selectedPoint = formState.selectedPoint,
        currentLocationPoint = currentLocationPoint
      )
    ) {
      formState.requestMapFocus()
    }
  }
}

/**
 * Determines whether current location should be focused automatically.
 */
private fun shouldFocusCurrentLocation(
  isEditMode: Boolean,
  initialSelectedPoint: MapPoint?,
  selectedPoint: MapPoint?,
  currentLocationPoint: MapPoint?
): Boolean {
  return !isEditMode &&
    initialSelectedPoint == null &&
    selectedPoint == null &&
    currentLocationPoint != null
}