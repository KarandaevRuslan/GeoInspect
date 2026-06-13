package com.karandaev.geo_inspect.core.presentation.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.karandaev.geo_inspect.core.location.LocatedPlace
import com.karandaev.geo_inspect.core.location.place.PlaceNameProvider
import com.karandaev.geo_inspect.core.location.resolver.LocationResolver
import com.karandaev.geo_inspect.core.presentation.location.LocationController
import com.karandaev.geo_inspect.core.presentation.location.LocationResolveMode
import com.karandaev.geo_inspect.core.presentation.location.LocationUiState
import com.karandaev.geo_inspect.core.presentation.location.rememberLocationController
import com.karandaev.geo_inspect.core.presentation.persisted.PersistedAppStateViewModel

private const val LastLocatedPlaceMaxAgeMillis = 24 * 60 * 60 * 1_000L

/**
 * UI-facing controller for map location state.
 *
 * Owns:
 *
 * - location resolving;
 * - optional background location polling;
 * - fallback to the last saved location when current location is unknown;
 * - initial map focus after the first successful location resolve;
 * - explicit focus requests from the map focus button.
 *
 * The map itself stays passive: it receives [locationState] and [focusRequest].
 */
class MapLocationController internal constructor(
  val locationState: LocationUiState,
  val focusRequest: Int,
  private val focusLocationAction: () -> Unit
) {

  /**
   * Requests the map to focus on the current location target.
   *
   * Also starts silent polling through the location controller. The actual focus target
   * is provided to the map through [locationState].
   */
  fun focusLocation() {
    focusLocationAction()
  }
}

/**
 * Creates a map-specific location controller for current device location.
 *
 * This overload uses Android runtime permission handling through [rememberLocationController].
 * When the current location is unknown, it can fall back to the last fresh location saved
 * in [persistedAppStateViewModel].
 */
@Composable
fun rememberMapLocationController(
  locationViewModelKey: String,
  permissionViewModelKey: String,
  locationResolver: LocationResolver,
  persistedAppStateViewModel: PersistedAppStateViewModel,
  resolveOnPermissionGranted: Boolean = true,
  resolveOnResume: Boolean = true,
  requestPermissionOnStart: Boolean = false,
  enablePolling: Boolean = true
): MapLocationController {
  val lastLocatedPlace by persistedAppStateViewModel.lastLocatedPlace.collectAsState()

  val locationController = rememberLocationController(
    locationViewModelKey = locationViewModelKey,
    permissionViewModelKey = permissionViewModelKey,
    locationResolver = locationResolver,
    resolveOnPermissionGranted = resolveOnPermissionGranted,
    resolveOnResume = resolveOnResume,
    requestPermissionOnStart = requestPermissionOnStart
  )

  return rememberMapLocationController(
    locationController = locationController,
    fallbackLocatedPlace = lastLocatedPlace,
    enablePolling = enablePolling
  )
}

/**
 * Creates a map-specific location controller for a fixed coordinate pair.
 *
 * This overload does not use Android runtime permission handling because the app
 * already has explicit coordinates.
 */
@Composable
fun rememberMapLocationController(
  locationViewModelKey: String,
  latitude: Double,
  longitude: Double,
  placeNameProvider: PlaceNameProvider,
  resolveOnStart: Boolean = true,
  enablePolling: Boolean = false
): MapLocationController {
  val locationController = rememberLocationController(
    locationViewModelKey = locationViewModelKey,
    latitude = latitude,
    longitude = longitude,
    placeNameProvider = placeNameProvider,
    resolveOnStart = resolveOnStart
  )

  return rememberMapLocationController(
    locationController = locationController,
    fallbackLocatedPlace = null,
    enablePolling = enablePolling
  )
}

/**
 * Shared implementation for any location source.
 *
 * The source can be current device location or fixed coordinates. This function only
 * needs a [LocationController] that exposes [LocationUiState] and location actions.
 */
@Composable
private fun rememberMapLocationController(
  locationController: LocationController,
  fallbackLocatedPlace: LocatedPlace?,
  enablePolling: Boolean
): MapLocationController {
  var focusRequest by remember {
    mutableIntStateOf(0)
  }

  var fallbackLocationFocusWasRequested by remember {
    mutableStateOf(false)
  }

  var actualLocationFocusWasRequested by remember {
    mutableStateOf(false)
  }

  val actualLocationState = locationController.locationState

  val displayLocationState = actualLocationState.withFallbackLocatedPlace(
    fallbackLocatedPlace = fallbackLocatedPlace
  )

  val fallbackSuccessState = displayLocationState as? LocationUiState.Success
  val actualSuccessState = actualLocationState as? LocationUiState.Success

  LaunchedEffect(
    fallbackSuccessState?.refreshId
  ) {
    if (
      fallbackSuccessState != null &&
      actualSuccessState == null &&
      !fallbackLocationFocusWasRequested
    ) {
      fallbackLocationFocusWasRequested = true
      focusRequest += 1
    }
  }

  LaunchedEffect(actualSuccessState?.refreshId) {
    if (
      actualSuccessState != null &&
      !actualLocationFocusWasRequested
    ) {
      actualLocationFocusWasRequested = true
      focusRequest += 1
    }
  }

  if (
    enablePolling &&
    locationController.hasLocationPermission
  ) {
    MapLocationPollingEffect(
      pollLocation = {
        locationController.pollLocation()
      }
    )
  }

  return MapLocationController(
    locationState = displayLocationState,
    focusRequest = focusRequest,
    focusLocationAction = {
      locationController.pollLocation()
    }
  )
}

/**
 * Replaces unavailable current location state with a fresh saved located place when available.
 *
 * This allows the map to show and focus the last known app location while the real
 * current location is still loading.
 *
 * Success, pull-to-refresh states, missing saved locations, and stale saved locations
 * are returned unchanged.
 */
private fun LocationUiState.withFallbackLocatedPlace(
  fallbackLocatedPlace: LocatedPlace?,
  currentTimeMillis: Long = System.currentTimeMillis()
): LocationUiState {
  if (!canUseFallbackLocatedPlace()) {
    return this
  }

  val locatedPlace = fallbackLocatedPlace ?: return this
  val updatedAt = locatedPlace.location.timestampMillis ?: return this
  val ageMillis = currentTimeMillis - updatedAt

  if (ageMillis !in 0..LastLocatedPlaceMaxAgeMillis) {
    return this
  }

  return LocationUiState.Success(
    data = locatedPlace,
    refreshId = updatedAt,
    resolveMode = fallbackResolveMode()
  )
}

private fun LocationUiState.canUseFallbackLocatedPlace(): Boolean {
  return when (this) {
    LocationUiState.Loading -> true

    is LocationUiState.Unknown -> {
      resolveMode == LocationResolveMode.Idle
    }

    is LocationUiState.Success -> false
  }
}

private fun LocationUiState.fallbackResolveMode(): LocationResolveMode {
  return when (this) {
    LocationUiState.Loading -> LocationResolveMode.FullLoading

    is LocationUiState.Unknown -> LocationResolveMode.Idle

    is LocationUiState.Success -> resolveMode
  }
}