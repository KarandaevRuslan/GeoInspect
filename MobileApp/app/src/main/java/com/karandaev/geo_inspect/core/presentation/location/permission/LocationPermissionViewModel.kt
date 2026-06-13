package com.karandaev.geo_inspect.core.presentation.location.permission

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import com.karandaev.geo_inspect.core.location.permission_checker.LocationPermissionChecker
import com.karandaev.geo_inspect.core.location.permission_checker.LocationPermissionRationaleChecker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Tracks the current Android location permission state for the UI.
 *
 * This ViewModel only resolves and exposes permission state. It does not launch
 * permission requests or show permission dialogs. Runtime permission requests are
 * expected to be triggered from the UI layer, for example via Compose Activity
 * Result APIs.
 *
 * The exposed state includes both the actual permission result and whether the UI
 * should show an explanation before requesting the permission again.
 */
class LocationPermissionViewModel : ViewModel() {

  private val _state = MutableStateFlow<LocationPermissionState>(
    LocationPermissionState.Unknown
  )

  /**
   * Current location permission state observed by the UI.
   *
   * The initial value is [LocationPermissionState.Unknown] until
   * [checkPermission] is called.
   */
  val state: StateFlow<LocationPermissionState> = _state.asStateFlow()

  /**
   * Resolves the current Android location permission state, updates [state],
   * and returns the freshly resolved value.
   *
   * [context] is used to check whether fine or coarse location permission is
   * currently granted.
   *
   * [activity] is used to determine whether Android recommends showing a
   * permission rationale before requesting location permission again. If
   * [activity] is `null`, the rationale flag is treated as `false`.
   *
   * This method only checks the current state. It does not request permissions.
   *
   * @return the same [LocationPermissionState] value that was written to [state].
   */
  fun checkPermission(
    context: Context,
    activity: Activity?
  ): LocationPermissionState {
    val newState = resolvePermissionState(
      context = context,
      activity = activity
    )

    _state.value = newState

    return newState
  }

  /**
   * Maps Android permission APIs to the app-specific [LocationPermissionState].
   *
   * Returns [LocationPermissionState.Granted] when either fine or coarse location
   * permission is granted. Otherwise returns [LocationPermissionState.Denied]
   * with the current rationale recommendation.
   */
  private fun resolvePermissionState(
    context: Context,
    activity: Activity?
  ): LocationPermissionState {
    if (LocationPermissionChecker.hasLocationPermission(context)) {
      return LocationPermissionState.Granted
    }

    return LocationPermissionState.Denied(
      shouldShowRationale = activity?.let {
        LocationPermissionRationaleChecker
          .shouldShowLocationPermissionRationale(it)
      } == true
    )
  }
}