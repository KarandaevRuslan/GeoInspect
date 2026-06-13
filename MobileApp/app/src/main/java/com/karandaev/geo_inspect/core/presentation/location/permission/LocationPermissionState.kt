package com.karandaev.geo_inspect.core.presentation.location.permission

/**
 * UI state for Android location runtime permission.
 */
sealed interface LocationPermissionState {

  /**
   * Permission status has not been checked yet.
   */
  data object Unknown : LocationPermissionState

  /**
   * Either precise or approximate location permission is granted.
   */
  data object Granted : LocationPermissionState

  /**
   * Location permission is not granted.
   *
   * @property shouldShowRationale Whether the UI should explain why location access is needed
   * before requesting permission again.
   */
  data class Denied(
    val shouldShowRationale: Boolean
  ) : LocationPermissionState
}