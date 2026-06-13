package com.karandaev.geo_inspect.core.presentation.location

import com.karandaev.geo_inspect.core.presentation.location.permission.LocationPermissionState

/**
 * State holder for screen-level location resolving.
 *
 * It can represent either:
 *
 * - current device location, where Android runtime permission is required;
 * - fixed coordinates, where no permission flow is needed.
 */
class LocationController internal constructor(
  val locationState: LocationUiState,
  val permissionState: LocationPermissionState?,
  private val requestLocationPermissionAction: () -> Unit,
  private val resolveLocationAction: (additionalAction: () -> Unit) -> Unit,
  private val refreshLocationAction: (additionalAction: () -> Unit) -> Unit,
  private val pollLocationAction: (additionalAction: () -> Unit) -> Unit
) {

  /**
   * True when location actions can run without requesting permission.
   *
   * For fixed-coordinate controllers this is always true because no Android location
   * permission is required.
   */
  val hasLocationPermission: Boolean
    get() = permissionState == null || permissionState is LocationPermissionState.Granted

  /**
   * Requests Android location permission.
   *
   * For fixed-coordinate controllers this is a no-op.
   */
  fun requestLocationPermission() {
    requestLocationPermissionAction()
  }

  fun resolveLocation(
    additionalAction: () -> Unit = {}
  ) {
    resolveLocationAction(additionalAction)
  }

  fun refreshLocation(
    additionalAction: () -> Unit = {}
  ) {
    refreshLocationAction(additionalAction)
  }

  fun pollLocation(
    additionalAction: () -> Unit = {}
  ) {
    pollLocationAction(additionalAction)
  }
}