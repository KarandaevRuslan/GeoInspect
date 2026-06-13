package com.karandaev.geo_inspect.core.presentation.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import com.karandaev.geo_inspect.core.presentation.viewModelFactory
import com.karandaev.geo_inspect.core.weather.WeatherInfo
import com.karandaev.geo_inspect.core.weather.WeatherProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val DefaultWeatherErrorMessage = "Unknown weather loading error"
private const val MissingWeatherPointMessage = "Weather location is unavailable"

/**
 * ViewModel for loading weather for the current map point.
 *
 * This ViewModel does not accept latitude/longitude in every public loading method.
 * Instead, the current point is explicitly updated through [updateCurrentPoint].
 *
 * This avoids keeping Compose-owned lambdas inside a long-lived ViewModel instance.
 * A ViewModel can outlive a particular composition, so storing a provider lambda from
 * Compose can lead to stale location snapshots after navigating away and back.
 *
 * If no current point is available when loading starts:
 *
 * - full loading emits [WeatherUiState.Error];
 * - pull-to-refresh stops without keeping the UI in pull-refresh mode;
 * - silent polling is ignored.
 *
 * This keeps the state model simple: [WeatherUiState] describes weather UI only,
 * while the current point ownership stays outside this ViewModel.
 *
 * The intended flow is:
 *
 * Location source/orchestrator -> updateCurrentPoint -> WeatherViewModel
 *
 * This ViewModel is responsible only for UI-facing weather state transitions:
 *
 * - full loading;
 * - pull-to-refresh over existing content;
 * - silent background refresh;
 * - success state;
 * - error state.
 *
 * Visible requests cancel previous requests and may replace or refresh visible UI.
 *
 * Silent background requests are internal operations. They may update weather data
 * on success, but they do not expose a separate UI mode and do not show loading UI.
 *
 * Silent requests are ignored while a visible request is already active. This prevents
 * lifecycle/background polling from interrupting full loading or pull-to-refresh.
 */
class WeatherViewModel(
  private val weatherProvider: WeatherProvider
) : ViewModel() {

  private val _state = MutableStateFlow<WeatherUiState>(
    WeatherUiState.Loading
  )

  /**
   * Current weather UI state.
   *
   * Screens observe this flow and decide what to render:
   *
   * - [WeatherUiState.Loading] for first-load or full retry loading;
   * - [WeatherUiState.Success] when weather was loaded;
   * - [WeatherUiState.Error] when loading failed.
   *
   * [WeatherUiState.resolveMode] explains how the current state should be represented:
   *
   * - [WeatherResolveMode.FullLoading] means visible full loading;
   * - [WeatherResolveMode.PullRefresh] means existing content is kept while refresh is active;
   * - [WeatherResolveMode.Idle] means no visible loading operation is active.
   *
   * Silent background polling is not represented as a UI mode. It is an internal
   * operation that keeps the UI visually idle.
   */
  val state: StateFlow<WeatherUiState> = _state.asStateFlow()

  private var loadJob: Job? = null

  /**
   * Current point that future weather loading operations should use.
   *
   * It is updated by the owner/orchestrator through [updateCurrentPoint].
   */
  private var currentPoint: MapPoint? = null

  /**
   * Monotonic id for accepted weather loading requests.
   *
   * It is used as an extra stale-result guard in addition to coroutine cancellation.
   * If an older request completes after a newer request was started, its result is ignored.
   */
  private var nextRequestId = 0L

  /**
   * Updates the point used by future weather requests.
   *
   * This method does not start loading by itself. The owner should call [loadWeather],
   * [refreshWeather], or [pollWeather] after updating the point when needed.
   */
  fun updateCurrentPoint(point: MapPoint?) {
    currentPoint = point
  }

  /**
   * Loads weather for the current point with a full loading state.
   *
   * Use this for first load or explicit retry when the current content can be
   * replaced by [WeatherUiState.Loading].
   */
  fun loadWeather() {
    loadWeather(operation = LoadOperation.FullLoading)
  }

  /**
   * Refreshes weather for the current point while preserving current Success/Error content.
   *
   * Existing content is marked with [WeatherResolveMode.PullRefresh] until the
   * request finishes.
   */
  fun refreshWeather() {
    loadWeather(operation = LoadOperation.PullRefresh)
  }

  /**
   * Loads weather for the current point in the background without changing visible UI mode.
   *
   * If a visible request is already active, this call is ignored. Polling should never
   * cancel or override full loading or pull-to-refresh.
   *
   * If no current point is available, this call is ignored.
   *
   * If the request succeeds, the state is updated with fresh [WeatherUiState.Success].
   * If the request fails, the previous state is kept unchanged.
   */
  fun pollWeather() {
    loadWeather(operation = LoadOperation.Silent)
  }

  /**
   * Starts a new weather loading request for the current point.
   *
   * Flow:
   *
   * 1. Ignore silent polling if a visible request is already active.
   * 2. Read the latest point stored by [updateCurrentPoint].
   * 3. Handle missing point without starting a network request.
   * 4. Cancel the previous request when starting a new accepted visible/background request.
   * 5. Update the state according to [LoadOperation].
   * 6. Call [WeatherProvider.getWeather].
   * 7. Emit Success if weather is loaded.
   * 8. Emit Error or keep the previous state if loading fails.
   *
   * The point is captured once at the start of the operation. If the app needs weather
   * for a newer point, the orchestrator should call [updateCurrentPoint] and then call
   * this ViewModel again, which cancels the previous request and starts a new one.
   *
   * Important: [CancellationException] is rethrown, not swallowed. Otherwise a cancelled
   * old request could still write stale state after a newer request starts.
   */
  private fun loadWeather(
    operation: LoadOperation
  ) {
    val currentState = _state.value

    if (
      operation == LoadOperation.Silent &&
      currentState.resolveMode != WeatherResolveMode.Idle
    ) {
      return
    }

    val point = currentPoint

    if (point == null) {
      handleMissingPoint(
        operation = operation,
        currentState = currentState
      )
      return
    }

    val requestId = ++nextRequestId

    loadJob?.cancel()

    _state.value = when (operation) {
      LoadOperation.FullLoading -> {
        WeatherUiState.Loading
      }

      LoadOperation.PullRefresh -> {
        when (currentState) {
          WeatherUiState.Loading -> {
            WeatherUiState.Loading
          }

          is WeatherUiState.Success -> {
            currentState.copy(
              resolveMode = WeatherResolveMode.PullRefresh
            )
          }

          is WeatherUiState.Error -> {
            currentState.copy(
              resolveMode = WeatherResolveMode.PullRefresh
            )
          }
        }
      }

      LoadOperation.Silent -> {
        currentState
      }
    }

    loadJob = viewModelScope.launch {
      loadWeatherInfo(point = point).fold(
        onSuccess = { weatherInfo ->
          if (requestId != nextRequestId) {
            return@launch
          }

          _state.value = WeatherUiState.Success(
            data = weatherInfo,
            resolveMode = WeatherResolveMode.Idle
          )
        },
        onFailure = { error ->
          if (requestId != nextRequestId) {
            return@launch
          }

          _state.value = when (operation) {
            LoadOperation.Silent -> {
              currentState
            }

            LoadOperation.FullLoading,
            LoadOperation.PullRefresh -> {
              WeatherUiState.Error(
                message = error.message ?: DefaultWeatherErrorMessage,
                resolveMode = WeatherResolveMode.Idle
              )
            }
          }
        }
      )
    }
  }

  private fun handleMissingPoint(
    operation: LoadOperation,
    currentState: WeatherUiState
  ) {
    when (operation) {
      LoadOperation.Silent -> {
        return
      }

      LoadOperation.FullLoading -> {
        nextRequestId += 1
        loadJob?.cancel()

        _state.value = WeatherUiState.Error(
          message = MissingWeatherPointMessage,
          resolveMode = WeatherResolveMode.Idle
        )
      }

      LoadOperation.PullRefresh -> {
        nextRequestId += 1
        loadJob?.cancel()

        _state.value = when (currentState) {
          WeatherUiState.Loading -> {
            WeatherUiState.Error(
              message = MissingWeatherPointMessage,
              resolveMode = WeatherResolveMode.Idle
            )
          }

          is WeatherUiState.Success -> {
            currentState.copy(
              resolveMode = WeatherResolveMode.Idle
            )
          }

          is WeatherUiState.Error -> {
            currentState.copy(
              resolveMode = WeatherResolveMode.Idle
            )
          }
        }
      }
    }
  }

  private suspend fun loadWeatherInfo(
    point: MapPoint
  ): Result<WeatherInfo> {
    return try {
      val result = weatherProvider.getWeather(
        latitude = point.latitude,
        longitude = point.longitude
      )

      val error = result.exceptionOrNull()

      if (error is CancellationException) {
        throw error
      }

      result
    } catch (error: CancellationException) {
      throw error
    } catch (error: Throwable) {
      Result.failure(error)
    }
  }

  /**
   * Describes what kind of weather loading operation should be performed.
   *
   * This is intentionally separate from [WeatherResolveMode]:
   *
   * - [WeatherResolveMode] describes how the current state should look in UI;
   * - [LoadOperation] describes how this ViewModel should perform the next request.
   *
   * Silent is an operation, not a UI mode.
   */
  private enum class LoadOperation {

    /**
     * Replace the current UI state with full loading.
     */
    FullLoading,

    /**
     * Keep existing Success/Error content visible and mark it as pull-refreshing.
     */
    PullRefresh,

    /**
     * Load weather in the background without changing visible UI mode.
     *
     * This operation is ignored if full loading or pull-to-refresh is already active.
     */
    Silent
  }

  companion object {

    /**
     * Creates a factory for [WeatherViewModel].
     */
    fun factory(
      weatherProvider: WeatherProvider
    ): ViewModelProvider.Factory {
      return viewModelFactory {
        WeatherViewModel(
          weatherProvider = weatherProvider
        )
      }
    }
  }
}