package com.karandaev.geo_inspect.feature.presentation.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.karandaev.geo_inspect.app.rememberMyApp
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.image.report.store_and_resolve.InspectionReportImageStorage
import com.karandaev.geo_inspect.core.presentation.detection.DetectViewModel
import com.karandaev.geo_inspect.core.presentation.persisted.PersistedAppStateViewModel
import com.karandaev.geo_inspect.core.presentation.reports.InspectionReportsViewModel
import com.karandaev.geo_inspect.feature.presentation.create.effects.CreateInspectionReportDetectionResultEffect
import com.karandaev.geo_inspect.feature.presentation.create.effects.CreateInspectionReportRouteEffects
import com.karandaev.geo_inspect.feature.presentation.create.location.rememberCreateInspectionReportMapLocationController
import com.karandaev.geo_inspect.feature.presentation.create.save.saveInspectionReport
import com.karandaev.geo_inspect.feature.presentation.create.state.CreateInspectionReportViewModel
import com.karandaev.geo_inspect.feature.presentation.create.state.canSaveCreateInspectionReport
import com.karandaev.geo_inspect.feature.presentation.create.state.createInspectionReportFormViewModelKey
import com.karandaev.geo_inspect.feature.presentation.create.state.rememberCreateInspectionReportFormViewModel
import com.karandaev.geo_inspect.feature.presentation.create.state.rememberInitialSelectedPoint
import com.karandaev.geo_inspect.feature.ui.components.maps.currentLocationPoint

private const val CreateInspectionReportUiViewModelKeySuffix = "-ui"

/**
 * Route-level container for the create/edit inspection report feature.
 */
@Composable
fun CreateInspectionReportRoute(
  inspectionReportsViewModel: InspectionReportsViewModel,
  persistedAppStateViewModel: PersistedAppStateViewModel,
  detectViewModel: DetectViewModel,
  editedInspectionReport: InspectionReport? = null,
  prefilledLatitude: Double? = null,
  prefilledLongitude: Double? = null,
  onDetectClick: () -> Unit,
  onBackClick: () -> Unit
) {
  val context = LocalContext.current
  val app = rememberMyApp()
  val isEditMode = editedInspectionReport != null

  val completedDetectionResult by detectViewModel.completedResult.collectAsStateWithLifecycle()

  val editedInspectionReportImagePath = remember(
    context,
    editedInspectionReport?.id
  ) {
    editedInspectionReport
      ?.id
      ?.let { inspectionReportId ->
        InspectionReportImageStorage.findDetectionImagePath(
          context = context,
          inspectionReportId = inspectionReportId
        )
      }
  }

  val initialSelectedPoint = rememberInitialSelectedPoint(
    latitude = prefilledLatitude,
    longitude = prefilledLongitude
  )

  val formViewModelKey = createInspectionReportFormViewModelKey(
    editedInspectionReport = editedInspectionReport,
    prefilledLatitude = prefilledLatitude,
    prefilledLongitude = prefilledLongitude
  )

  val createViewModel: CreateInspectionReportViewModel = viewModel(
    key = "$formViewModelKey$CreateInspectionReportUiViewModelKeySuffix"
  )

  val formState = rememberCreateInspectionReportFormViewModel(
    initialSelectedPoint = initialSelectedPoint,
    viewModelKey = formViewModelKey
  )

  val locationController = rememberCreateInspectionReportMapLocationController(
    locationResolver = app.locationResolver,
    persistedAppStateViewModel = persistedAppStateViewModel,
    isEditMode = isEditMode,
    initialSelectedPoint = initialSelectedPoint
  )

  val currentLocationPoint = locationController.locationState.currentLocationPoint
  val effectivePoint = formState.effectivePoint(currentLocationPoint)

  val hasDetectionPreview = formState.detectResponse != null ||
    !formState.detectionImagePath.isNullOrBlank()

  LaunchedEffect(hasDetectionPreview) {
    if (!hasDetectionPreview) {
      createViewModel.hideDetectionPreviewDialog()
    }
  }

  CreateInspectionReportRouteEffects(
    editedInspectionReport = editedInspectionReport,
    editedInspectionReportImagePath = editedInspectionReportImagePath,
    isEditMode = isEditMode,
    initialSelectedPoint = initialSelectedPoint,
    currentLocationPoint = currentLocationPoint,
    formState = formState
  )

  CreateInspectionReportDetectionResultEffect(
    completedResult = completedDetectionResult,
    formState = formState,
    onCompletedResultConsumed = detectViewModel::consumeCompletedResult
  )

  CreateInspectionReportScreen(
    title = formState.title,
    content = formState.content,
    selectedPoint = formState.selectedPoint,
    locationState = locationController.locationState,
    mapFocusRequest = formState.mapFocusRequest,
    detectionImagePath = formState.detectionImagePath,
    detectResponse = formState.detectResponse,
    isDetectionPreviewDialogVisible = createViewModel.isDetectionPreviewDialogVisible,
    canSave = canSaveCreateInspectionReport(
      title = formState.title,
      effectivePoint = effectivePoint,
      isEditMode = isEditMode,
      editedInspectionReport = editedInspectionReport
    ),
    isEditMode = isEditMode,
    onTitleChange = formState::onTitleChange,
    onContentChange = formState::onContentChange,
    onMapFocusClick = {
      locationController.focusLocation()
    },
    onUseCurrentLocationClick = {
      formState.useCurrentLocation()
      locationController.focusLocation()
    },
    onSelectedPointChange = formState::onSelectedPointChange,
    onLatitudeChange = formState::onLatitudeChange,
    onLongitudeChange = formState::onLongitudeChange,
    onDetectionPreviewClick = createViewModel::showDetectionPreviewDialog,
    onDismissDetectionPreviewDialogRequest = createViewModel::hideDetectionPreviewDialog,
    onDetectClick = onDetectClick,
    onClearDetectionClick = {
      createViewModel.hideDetectionPreviewDialog()
      formState.clearDetectionResult()
    },
    onSaveClick = {
      val point = formState.effectivePoint(currentLocationPoint)
        ?: return@CreateInspectionReportScreen

      saveInspectionReport(
        context = context,
        inspectionReportsViewModel = inspectionReportsViewModel,
        editedInspectionReport = editedInspectionReport,
        title = formState.title,
        content = formState.content,
        point = point,
        detectResponse = formState.detectResponse,
        detectionImagePath = formState.detectionImagePath,
        removeDetectionImage = formState.isDetectionMarkedForRemoval,
        onSaved = onBackClick
      )
    },
    onBackClick = onBackClick
  )
}