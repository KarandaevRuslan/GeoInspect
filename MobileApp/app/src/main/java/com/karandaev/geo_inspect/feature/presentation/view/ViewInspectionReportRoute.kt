package com.karandaev.geo_inspect.feature.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.karandaev.geo_inspect.app.rememberMyApp
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.image.report.store_and_resolve.InspectionReportImageStorage
import com.karandaev.geo_inspect.core.presentation.locationweather.rememberLocationWeatherController
import com.karandaev.geo_inspect.core.presentation.map.rememberMapLocationController
import com.karandaev.geo_inspect.core.presentation.persisted.PersistedAppStateViewModel
import com.karandaev.geo_inspect.feature.presentation.view.dialogs.ViewInspectionReportDeleteDialogHost
import com.karandaev.geo_inspect.feature.presentation.view.state.ViewInspectionReportViewModel

/**
 * Route-level container for the inspection report details screen.
 */
@Composable
fun ViewInspectionReportRoute(
  inspectionReport: InspectionReport?,
  persistedAppStateViewModel: PersistedAppStateViewModel,
  onBackClick: () -> Unit,
  onEditClick: (Long) -> Unit,
  onDeleteClick: (Long) -> Unit
) {
  val viewState: ViewInspectionReportViewModel = viewModel()

  val currentReport = inspectionReport

  if (currentReport == null) {
    ViewInspectionReportLoadingContent()
    return
  }

  LaunchedEffect(currentReport.id) {
    viewState.bindInspectionReport(currentReport.id)
  }

  val context = LocalContext.current
  val app = rememberMyApp()

  val detectionImagePath = remember(
    context,
    currentReport.id,
    currentReport.detectResponse,
    currentReport.lastUpdatedAtMillis
  ) {
    InspectionReportImageStorage.findDetectionImagePath(
      context = context,
      inspectionReportId = currentReport.id
    )
  }

  val detections = currentReport.detectResponse?.detections.orEmpty()

  val inspectionLocationWeatherController = rememberLocationWeatherController(
    locationViewModelKey = "location-inspection-report-${currentReport.id}",
    weatherViewModelKey = "weather-inspection-report-${currentReport.id}",
    latitude = currentReport.latitude,
    longitude = currentReport.longitude,
    placeNameProvider = app.placeNameProvider,
    weatherProvider = app.weatherProvider
  )

  val currentMapLocationController = rememberMapLocationController(
    locationViewModelKey = "current-location-inspection-report-${currentReport.id}",
    permissionViewModelKey = "current-location-permission-inspection-report-${currentReport.id}",
    locationResolver = app.locationResolver,
    persistedAppStateViewModel = persistedAppStateViewModel,
    requestPermissionOnStart = false,
    enablePolling = true
  )

  ViewInspectionReportScreen(
    inspectionReport = currentReport,
    detectionImagePath = detectionImagePath,
    detections = detections,
    isDetectionPreviewDialogVisible = viewState.isDetectionPreviewDialogVisible,
    inspectionReportLocationState = inspectionLocationWeatherController.locationState,
    currentLocationState = currentMapLocationController.locationState,
    inspectionReportWeatherState = inspectionLocationWeatherController.weatherState,
    isPullRefreshing = inspectionLocationWeatherController.isPullRefreshing,
    onPullRefresh = inspectionLocationWeatherController::refresh,
    onDetectionPreviewClick = viewState::showDetectionPreviewDialog,
    onDismissDetectionPreviewDialogRequest = viewState::hideDetectionPreviewDialog,
    onMapFocusClick = currentMapLocationController::focusLocation,
    onWeatherCardRetry = inspectionLocationWeatherController::retryWeather,
    onEditClick = {
      onEditClick(currentReport.id)
    },
    onDeleteClick = viewState::showDeleteDialog,
    onBackClick = onBackClick
  )

  ViewInspectionReportDeleteDialogHost(
    showDeleteDialog = viewState.isDeleteDialogVisible,
    inspectionReportTitle = currentReport.title,
    confirmationText = viewState.deleteConfirmationText,
    onConfirmationTextChange = viewState::updateDeleteConfirmationText,
    onConfirmationTextClear = viewState::clearDeleteConfirmationText,
    onDismiss = viewState::dismissDeleteDialog,
    onConfirm = {
      val idToDelete = currentReport.id

      viewState.dismissDeleteDialog()
      onBackClick()
      onDeleteClick(idToDelete)
    }
  )
}

@Composable
private fun ViewInspectionReportLoadingContent() {
  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    CircularProgressIndicator()
  }
}