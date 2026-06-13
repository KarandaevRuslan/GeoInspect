package com.karandaev.geo_inspect.core.presentation.weather

import com.karandaev.geo_inspect.core.weather.WeatherInfo

/**
 * Describes how the current weather loading state should be represented in UI.
 */
enum class WeatherResolveMode {

  /**
   * No visible loading operation is active.
   */
  Idle,

  /**
   * Weather is being loaded with full loading UI.
   *
   * Existing content can be replaced by a loading state.
   */
  FullLoading,

  /**
   * Weather is being loaded by pull-to-refresh.
   *
   * Existing Success/Error content should stay visible while the pull-refresh
   * indicator is shown.
   */
  PullRefresh
}

/**
 * UI state for weather loading.
 */
sealed interface WeatherUiState {

  /**
   * Describes how this state should be represented in UI.
   */
  val resolveMode: WeatherResolveMode

  /**
   * Weather data is being loaded without existing content to keep on screen.
   */
  data object Loading : WeatherUiState {
    override val resolveMode: WeatherResolveMode = WeatherResolveMode.FullLoading
  }

  /**
   * Weather data has been loaded successfully.
   *
   * During pull-to-refresh, [resolveMode] is [WeatherResolveMode.PullRefresh].
   * Otherwise it is [WeatherResolveMode.Idle].
   */
  data class Success(
    val data: WeatherInfo,
    override val resolveMode: WeatherResolveMode = WeatherResolveMode.Idle
  ) : WeatherUiState

  /**
   * Weather loading failed.
   *
   * During pull-to-refresh, [resolveMode] is [WeatherResolveMode.PullRefresh].
   * Otherwise it is [WeatherResolveMode.Idle].
   */
  data class Error(
    val message: String,
    override val resolveMode: WeatherResolveMode = WeatherResolveMode.Idle
  ) : WeatherUiState
}