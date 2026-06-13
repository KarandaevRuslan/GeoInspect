package com.karandaev.geo_inspect.feature.presentation.create.location

import androidx.compose.runtime.Composable
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import com.karandaev.geo_inspect.core.location.resolver.LocationResolver
import com.karandaev.geo_inspect.core.presentation.map.MapLocationController
import com.karandaev.geo_inspect.core.presentation.map.rememberMapLocationController
import com.karandaev.geo_inspect.core.presentation.persisted.PersistedAppStateViewModel
import com.karandaev.geo_inspect.feature.presentation.create.CREATE_INSPECTION_REPORT_LOCATION_VIEW_MODEL_KEY
import com.karandaev.geo_inspect.feature.presentation.create.CREATE_INSPECTION_REPORT_PERMISSION_VIEW_MODEL_KEY

/**
 * Creates a map-specific location controller for the create/edit note screen.
 *
 * Current location is requested automatically only when:
 * - the screen is not in edit mode;
 * - no point was prefilled from the map.
 *
 * The returned controller supports polling and map focus requests, which makes it
 * suitable for map-based screens.
 *
 * @param locationResolver Resolver used to obtain current device location.
 * @param persistedAppStateViewModel ViewModel used by the map controller for last location fallback.
 * @param isEditMode Whether the screen is opened in edit mode.
 * @param initialSelectedPoint Point selected before opening the screen.
 * @return Map location controller used by the route.
 */
@Composable
internal fun rememberCreateInspectionReportMapLocationController(
  locationResolver: LocationResolver,
  persistedAppStateViewModel: PersistedAppStateViewModel,
  isEditMode: Boolean,
  initialSelectedPoint: MapPoint?
): MapLocationController {
  return rememberMapLocationController(
    locationViewModelKey = CREATE_INSPECTION_REPORT_LOCATION_VIEW_MODEL_KEY,
    permissionViewModelKey = CREATE_INSPECTION_REPORT_PERMISSION_VIEW_MODEL_KEY,
    locationResolver = locationResolver,
    persistedAppStateViewModel = persistedAppStateViewModel,
    requestPermissionOnStart = shouldRequestCurrentLocationOnStart(
      isEditMode = isEditMode,
      initialSelectedPoint = initialSelectedPoint
    ),
    enablePolling = true
  )
}

/**
 * Returns whether the screen should request current location on start.
 *
 * Current location is requested automatically only for a new note without
 * prefilled coordinates. In edit mode or map-long-press flow, the screen already
 * has a target point and should not ask for current location immediately.
 */
private fun shouldRequestCurrentLocationOnStart(
  isEditMode: Boolean,
  initialSelectedPoint: MapPoint?
): Boolean {
  val isCreatingNewNote = !isEditMode
  val hasNoPrefilledPoint = initialSelectedPoint == null

  return isCreatingNewNote && hasNoPrefilledPoint
}