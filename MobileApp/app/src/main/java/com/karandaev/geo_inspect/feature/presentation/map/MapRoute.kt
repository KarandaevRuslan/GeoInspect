package com.karandaev.geo_inspect.feature.presentation.map

import androidx.compose.runtime.Composable
import com.karandaev.geo_inspect.app.rememberMyApp
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.presentation.map.rememberMapLocationController
import com.karandaev.geo_inspect.core.presentation.persisted.PersistedAppStateViewModel

@Composable
fun MapRoute(
  inspectionReports: List<InspectionReport>,
  persistedAppStateViewModel: PersistedAppStateViewModel,
  onMarkerClick: (Long) -> Unit,
  onLongPressCreateNote: (Double, Double) -> Unit
) {
  val app = rememberMyApp()

  val mapLocationController = rememberMapLocationController(
    locationViewModelKey = "location-map",
    permissionViewModelKey = "location-permission-map",
    locationResolver = app.locationResolver,
    persistedAppStateViewModel = persistedAppStateViewModel,
    requestPermissionOnStart = false,
    enablePolling = true
  )

  MapScreen(
    inspectionReports = inspectionReports,
    locationState = mapLocationController.locationState,
    focusRequest = mapLocationController.focusRequest,
    onFocusClick = mapLocationController::focusLocation,
    onMarkerClick = onMarkerClick,
    onLongPressCreateNote = onLongPressCreateNote
  )
}