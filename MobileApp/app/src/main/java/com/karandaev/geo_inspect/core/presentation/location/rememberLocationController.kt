package com.karandaev.geo_inspect.core.presentation.location

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.karandaev.geo_inspect.core.location.place.PlaceNameProvider
import com.karandaev.geo_inspect.core.location.resolver.LocationResolver
import com.karandaev.geo_inspect.core.presentation.location.permission.LocationPermissionLifecycleEffect
import com.karandaev.geo_inspect.core.presentation.location.permission.LocationPermissionRequestEffect
import com.karandaev.geo_inspect.core.presentation.location.permission.LocationPermissionState
import com.karandaev.geo_inspect.core.presentation.location.permission.LocationPermissionViewModel
import com.karandaev.geo_inspect.core.presentation.location.permission.findActivity
import com.karandaev.geo_inspect.core.util.formatters.formatCoordinate

/**
 * Creates a controller for current device location.
 *
 * This overload owns Android runtime permission handling.
 */
@Composable
fun rememberLocationController(
  locationViewModelKey: String,
  permissionViewModelKey: String,
  locationResolver: LocationResolver,
  resolveOnPermissionGranted: Boolean = true,
  resolveOnResume: Boolean = true,
  requestPermissionOnStart: Boolean = false
): LocationController {
  val context = LocalContext.current
  val activity = remember(context) {
    context.findActivity()
  }

  val locationViewModel: LocationViewModel = viewModel(
    key = locationViewModelKey,
    factory = LocationViewModel.currentLocationFactory(
      locationResolver = locationResolver
    )
  )

  val permissionViewModel: LocationPermissionViewModel = viewModel(
    key = permissionViewModelKey
  )

  val locationState by locationViewModel.state.collectAsStateWithLifecycle()
  val permissionState by permissionViewModel.state.collectAsStateWithLifecycle()

  var permissionRequestKey by remember {
    mutableIntStateOf(0)
  }

  var startPermissionRequestWasLaunched by remember {
    mutableStateOf(false)
  }

  val launchLocationPermissionRequest = {
    permissionRequestKey += 1
  }

  fun runLocationAction(
    additionalAction: () -> Unit,
    action: () -> Unit
  ) {
    runWithLocationPermission(
      permissionViewModel = permissionViewModel,
      context = context,
      activity = activity,
      onPermissionDenied = {
        locationViewModel.markPermissionDenied()
        launchLocationPermissionRequest()
      },
      action = {
        additionalAction()
        action()
      }
    )
  }

  LaunchedEffect(
    permissionState,
    requestPermissionOnStart,
    startPermissionRequestWasLaunched
  ) {
    if (
      requestPermissionOnStart &&
      !startPermissionRequestWasLaunched &&
      permissionState is LocationPermissionState.Denied
    ) {
      startPermissionRequestWasLaunched = true
      launchLocationPermissionRequest()
    }
  }

  LocationPermissionRequestEffect(
    requestKey = permissionRequestKey,
    onPermissionResult = {
      permissionViewModel.checkPermission(
        context = context,
        activity = activity
      )
    }
  )

  LocationPermissionLifecycleEffect(
    permissionState = permissionState,
    permissionViewModel = permissionViewModel,
    locationViewModel = locationViewModel,
    context = context,
    activity = activity,
    resolveOnPermissionGranted = resolveOnPermissionGranted,
    resolveOnResume = resolveOnResume
  )

  return LocationController(
    locationState = locationState,
    permissionState = permissionState,
    requestLocationPermissionAction = launchLocationPermissionRequest,
    resolveLocationAction = { additionalAction ->
      runLocationAction(
        additionalAction = additionalAction,
        action = locationViewModel::resolveLocation
      )
    },
    refreshLocationAction = { additionalAction ->
      runLocationAction(
        additionalAction = additionalAction,
        action = locationViewModel::refreshLocation
      )
    },
    pollLocationAction = { additionalAction ->
      runLocationAction(
        additionalAction = additionalAction,
        action = locationViewModel::pollLocation
      )
    }
  )
}

/**
 * Creates a controller for a fixed coordinate pair.
 *
 * This overload does not use Android runtime permission handling because the app
 * already has explicit coordinates.
 *
 * The internal ViewModel key includes the coordinate pair so a different latitude
 * or longitude creates a ViewModel with a matching [LocationViewModel] source.
 */
@Composable
fun rememberLocationController(
  locationViewModelKey: String,
  latitude: Double,
  longitude: Double,
  placeNameProvider: PlaceNameProvider,
  resolveOnStart: Boolean = true
): LocationController {
  val coordinatesViewModelKey = remember(
    locationViewModelKey,
    latitude,
    longitude
  ) {
    "$locationViewModelKey:${formatCoordinate(latitude)}:${formatCoordinate(longitude)}"
  }

  val locationViewModel: LocationViewModel = viewModel(
    key = coordinatesViewModelKey,
    factory = LocationViewModel.coordinatesFactory(
      latitude = latitude,
      longitude = longitude,
      placeNameProvider = placeNameProvider
    )
  )

  val locationState by locationViewModel.state.collectAsStateWithLifecycle()

  LaunchedEffect(
    locationViewModel,
    resolveOnStart
  ) {
    if (resolveOnStart) {
      locationViewModel.resolveLocation()
    }
  }

  fun runLocationAction(
    additionalAction: () -> Unit,
    action: () -> Unit
  ) {
    additionalAction()
    action()
  }

  return LocationController(
    locationState = locationState,
    permissionState = null,
    requestLocationPermissionAction = {},
    resolveLocationAction = { additionalAction ->
      runLocationAction(
        additionalAction = additionalAction,
        action = locationViewModel::resolveLocation
      )
    },
    refreshLocationAction = { additionalAction ->
      runLocationAction(
        additionalAction = additionalAction,
        action = locationViewModel::refreshLocation
      )
    },
    pollLocationAction = { additionalAction ->
      runLocationAction(
        additionalAction = additionalAction,
        action = locationViewModel::pollLocation
      )
    }
  )
}

/**
 * Shared low-level helper for current-location controllers.
 *
 * Runs [action] only when Android location permission is currently granted.
 */
private fun runWithLocationPermission(
  permissionViewModel: LocationPermissionViewModel,
  context: Context,
  activity: Activity?,
  onPermissionDenied: () -> Unit,
  action: () -> Unit
) {
  val permissionState = permissionViewModel.checkPermission(
    context = context,
    activity = activity
  )

  if (permissionState is LocationPermissionState.Granted) {
    action()
  } else {
    onPermissionDenied()
  }
}