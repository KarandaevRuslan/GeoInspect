package com.karandaev.geo_inspect.core.presentation.location

import com.karandaev.geo_inspect.core.location.LocatedPlace

/**
 * Describes how the current location resolving state should be represented in UI.
 */
enum class LocationResolveMode {

  /**
   * No visible resolving operation is active.
   */
  Idle,

  /**
   * Location is being resolved with full loading UI.
   *
   * Existing content can be replaced by a loading state.
   */
  FullLoading,

  /**
   * Location is being resolved by pull-to-refresh.
   *
   * Existing Success/Unknown content should stay visible while the pull-refresh
   * indicator is shown.
   */
  PullRefresh
}

/**
 * UI state for location resolving.
 */
sealed interface LocationUiState {

  /**
   * Describes how this state should be represented in UI.
   */
  val resolveMode: LocationResolveMode

  /**
   * Location is being resolved without existing content to keep on screen.
   */
  data object Loading : LocationUiState {
    override val resolveMode: LocationResolveMode = LocationResolveMode.FullLoading
  }

  /**
   * Location has been resolved successfully.
   *
   * [refreshId] changes after every completed visible resolving attempt,
   * even if coordinates stay the same.
   *
   * During pull-to-refresh, [resolveMode] is [LocationResolveMode.PullRefresh].
   * Otherwise it is [LocationResolveMode.Idle].
   */
  data class Success(
    val data: LocatedPlace,
    val refreshId: Long = 0L,
    override val resolveMode: LocationResolveMode = LocationResolveMode.Idle
  ) : LocationUiState

  /**
   * Location could not be resolved.
   *
   * [refreshId] changes after every completed visible failed resolving attempt.
   *
   * During pull-to-refresh, [resolveMode] is [LocationResolveMode.PullRefresh].
   * Otherwise it is [LocationResolveMode.Idle].
   */
  data class Unknown(
    val message: String,
    val refreshId: Long = 0L,
    override val resolveMode: LocationResolveMode = LocationResolveMode.Idle
  ) : LocationUiState
}