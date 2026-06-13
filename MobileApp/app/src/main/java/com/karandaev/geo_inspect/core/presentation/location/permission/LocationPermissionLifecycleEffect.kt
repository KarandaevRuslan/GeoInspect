package com.karandaev.geo_inspect.core.presentation.location.permission

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.karandaev.geo_inspect.core.presentation.location.LocationViewModel

/**
 * Binds location permission checks to the current composable lifecycle.
 *
 * This effect performs three permission-related tasks:
 *
 * - checks the current location permission state when the composable enters
 *   composition;
 * - starts location resolving when permission becomes granted, if enabled;
 * - re-checks permission when the screen returns to the foreground.
 *
 * The effect does not request runtime permissions by itself. Permission requests
 * should be launched by the UI layer, for example with Compose Activity Result
 * APIs.
 *
 * @param permissionState current location permission state observed by the UI.
 * @param permissionViewModel ViewModel responsible for resolving permission state.
 * @param locationViewModel ViewModel responsible for resolving the current device location.
 * @param context Android context used for permission checks.
 * @param activity current Activity used for checking whether permission rationale
 * should be shown. If `null`, rationale is treated as unavailable.
 * @param resolveOnPermissionGranted whether location resolving should start
 * when permission becomes granted.
 * @param resolveOnResume whether location resolving should be retried when the
 * screen resumes and permission was already granted before resume.
 */
@Composable
fun LocationPermissionLifecycleEffect(
  permissionState: LocationPermissionState,
  permissionViewModel: LocationPermissionViewModel,
  locationViewModel: LocationViewModel,
  context: Context,
  activity: Activity?,
  resolveOnPermissionGranted: Boolean,
  resolveOnResume: Boolean
) {
  LaunchedEffect(permissionViewModel, context, activity) {
    val refreshedPermissionState = checkLocationPermission(
      permissionViewModel = permissionViewModel,
      context = context,
      activity = activity
    )

    markLocationPermissionDeniedIfNeeded(
      permissionState = refreshedPermissionState,
      locationViewModel = locationViewModel
    )
  }

  LaunchedEffect(permissionState, resolveOnPermissionGranted) {
    markLocationPermissionDeniedIfNeeded(
      permissionState = permissionState,
      locationViewModel = locationViewModel
    )

    resolveLocationIfGranted(
      previousPermissionState = null,
      currentPermissionState = permissionState,
      locationViewModel = locationViewModel,
      reason = LocationResolveReason.PermissionGranted,
      resolveOnPermissionGranted = resolveOnPermissionGranted,
      resolveOnResume = resolveOnResume
    )
  }

  ObserveLocationPermissionOnResume(
    permissionState = permissionState,
    permissionViewModel = permissionViewModel,
    locationViewModel = locationViewModel,
    context = context,
    activity = activity,
    resolveOnPermissionGranted = resolveOnPermissionGranted,
    resolveOnResume = resolveOnResume
  )
}

/**
 * Observes the current screen lifecycle and refreshes location permission state
 * whenever the screen resumes.
 *
 * This keeps the app state synchronized with Android permission state, including
 * cases where the user grants or revokes location permission from system settings
 * while the app is in the background.
 */
@Composable
private fun ObserveLocationPermissionOnResume(
  permissionState: LocationPermissionState,
  permissionViewModel: LocationPermissionViewModel,
  locationViewModel: LocationViewModel,
  context: Context,
  activity: Activity?,
  resolveOnPermissionGranted: Boolean,
  resolveOnResume: Boolean
) {
  val lifecycleOwner = LocalLifecycleOwner.current

  val currentPermissionState = rememberUpdatedState(permissionState)
  val currentActivity = rememberUpdatedState(activity)
  val currentResolveOnPermissionGranted = rememberUpdatedState(
    resolveOnPermissionGranted
  )
  val currentResolveOnResume = rememberUpdatedState(resolveOnResume)

  DisposableEffect(
    lifecycleOwner,
    permissionViewModel,
    locationViewModel,
    context
  ) {
    val observer = LifecycleEventObserver { _, event ->
      if (event == Lifecycle.Event.ON_RESUME) {
        handleLocationPermissionResume(
          previousPermissionState = currentPermissionState.value,
          permissionViewModel = permissionViewModel,
          locationViewModel = locationViewModel,
          context = context,
          activity = currentActivity.value,
          resolveOnPermissionGranted = currentResolveOnPermissionGranted.value,
          resolveOnResume = currentResolveOnResume.value
        )
      }
    }

    lifecycleOwner.lifecycle.addObserver(observer)

    onDispose {
      lifecycleOwner.lifecycle.removeObserver(observer)
    }
  }
}

/**
 * Handles location permission work that should happen when the screen returns
 * to the foreground.
 *
 * Permission state is refreshed first. Location resolving on resume is only
 * performed when permission was already granted before resume and is still
 * granted after the refresh.
 *
 * If permission changes from denied to granted during resume, location resolving
 * is left to [LocationResolveReason.PermissionGranted]. This prevents duplicate
 * calls to [LocationViewModel.resolveLocation].
 */
private fun handleLocationPermissionResume(
  previousPermissionState: LocationPermissionState,
  permissionViewModel: LocationPermissionViewModel,
  locationViewModel: LocationViewModel,
  context: Context,
  activity: Activity?,
  resolveOnPermissionGranted: Boolean,
  resolveOnResume: Boolean
) {
  val refreshedPermissionState = checkLocationPermission(
    permissionViewModel = permissionViewModel,
    context = context,
    activity = activity
  )

  markLocationPermissionDeniedIfNeeded(
    permissionState = refreshedPermissionState,
    locationViewModel = locationViewModel
  )

  resolveLocationIfGranted(
    previousPermissionState = previousPermissionState,
    currentPermissionState = refreshedPermissionState,
    locationViewModel = locationViewModel,
    reason = LocationResolveReason.ScreenResumed,
    resolveOnPermissionGranted = resolveOnPermissionGranted,
    resolveOnResume = resolveOnResume
  )
}

/**
 * Describes why location resolving is being attempted.
 */
private enum class LocationResolveReason {

  /**
   * Permission state changed and the current state is granted.
   */
  PermissionGranted,

  /**
   * The screen resumed and permission state was refreshed.
   */
  ScreenResumed
}

/**
 * Refreshes the current Android location permission state.
 *
 * @return the freshly resolved permission state.
 */
private fun checkLocationPermission(
  permissionViewModel: LocationPermissionViewModel,
  context: Context,
  activity: Activity?
): LocationPermissionState {
  return permissionViewModel.checkPermission(
    context = context,
    activity = activity
  )
}

/**
 * Starts resolving the current device location when permission is granted and
 * the current resolve reason is allowed by configuration.
 */
private fun resolveLocationIfGranted(
  previousPermissionState: LocationPermissionState?,
  currentPermissionState: LocationPermissionState,
  locationViewModel: LocationViewModel,
  reason: LocationResolveReason,
  resolveOnPermissionGranted: Boolean,
  resolveOnResume: Boolean
) {
  if (
    shouldResolveLocation(
      previousPermissionState = previousPermissionState,
      currentPermissionState = currentPermissionState,
      reason = reason,
      resolveOnPermissionGranted = resolveOnPermissionGranted,
      resolveOnResume = resolveOnResume
    )
  ) {
    locationViewModel.resolveLocation()
  }
}

/**
 * Returns whether location should be resolved for the current permission state
 * and lifecycle reason.
 */
private fun shouldResolveLocation(
  previousPermissionState: LocationPermissionState?,
  currentPermissionState: LocationPermissionState,
  reason: LocationResolveReason,
  resolveOnPermissionGranted: Boolean,
  resolveOnResume: Boolean
): Boolean {
  if (currentPermissionState !is LocationPermissionState.Granted) {
    return false
  }

  return when (reason) {
    LocationResolveReason.PermissionGranted -> {
      resolveOnPermissionGranted
    }

    LocationResolveReason.ScreenResumed -> {
      resolveOnResume &&
        previousPermissionState is LocationPermissionState.Granted
    }
  }
}

/**
 * Finishes location loading when location permission is not granted.
 *
 * Without this, the UI can stay in Loading forever after the user denies
 * Android location permission.
 */
private fun markLocationPermissionDeniedIfNeeded(
  permissionState: LocationPermissionState,
  locationViewModel: LocationViewModel
) {
  if (permissionState is LocationPermissionState.Granted) {
    return
  }

  locationViewModel.markPermissionDenied()
}