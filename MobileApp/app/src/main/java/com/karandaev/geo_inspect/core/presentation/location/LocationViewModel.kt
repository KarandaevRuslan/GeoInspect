package com.karandaev.geo_inspect.core.presentation.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.karandaev.geo_inspect.core.location.LocatedPlace
import com.karandaev.geo_inspect.core.location.place.PlaceNameProvider
import com.karandaev.geo_inspect.core.location.resolver.LocationResolver
import com.karandaev.geo_inspect.core.location.source.CoordinatesLocationSource
import com.karandaev.geo_inspect.core.location.source.CurrentLocationSource
import com.karandaev.geo_inspect.core.location.source.LocationSource
import com.karandaev.geo_inspect.core.presentation.viewModelFactory
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

private const val UnknownLocationMessage = "Location is unknown"
private const val LOCATION_PERMISSION_DENIED_MESSAGE =
  "Location permission is required to get current location."

/**
 * Resolves a [LocationUiState] from an injected [LocationSource].
 *
 * This ViewModel does not know where the location comes from. The source can be:
 *
 * - [CurrentLocationSource], which asks the device for the current location;
 * - [CoordinatesLocationSource], which resolves a fixed latitude/longitude pair;
 * - any custom [LocationSource] passed through [factory].
 *
 * The ViewModel is responsible only for UI-facing state transitions:
 *
 * - full loading;
 * - pull-to-refresh over existing content;
 * - silent background refresh;
 * - success state;
 * - unknown/error state.
 *
 * Visible requests cancel previous requests and increment [LocationUiState.Success.refreshId]
 * or [LocationUiState.Unknown.refreshId] when they complete.
 *
 * Silent background requests preserve the previous refresh id so passive updates do not
 * trigger UI effects that are keyed by refresh id.
 *
 * Silent requests are ignored while a visible request is already active. This prevents
 * lifecycle/background polling from interrupting full loading or pull-to-refresh.
 */
class LocationViewModel(
  private val locationSource: LocationSource
) : ViewModel() {

  private val _state = MutableStateFlow<LocationUiState>(
    LocationUiState.Loading
  )

  /**
   * Current location UI state.
   *
   * Screens observe this flow and decide what to render:
   *
   * - [LocationUiState.Loading] for first-load or full retry loading;
   * - [LocationUiState.Success] when a location was resolved;
   * - [LocationUiState.Unknown] when resolving failed.
   *
   * [LocationUiState.resolveMode] explains how the current state should be represented:
   *
   * - [LocationResolveMode.FullLoading] means visible full loading;
   * - [LocationResolveMode.PullRefresh] means existing content is kept while refresh is active;
   * - [LocationResolveMode.Idle] means no visible resolving operation is active.
   *
   * Silent background polling is not represented as a UI mode. It is an internal
   * operation that keeps the UI visually idle.
   */
  val state: StateFlow<LocationUiState> = _state.asStateFlow()

  private var resolveJob: Job? = null

  /**
   * Monotonic id for completed visible resolving attempts.
   *
   * It changes after every completed full-loading or pull-to-refresh request,
   * even when the resolved coordinates are the same as before.
   *
   * Silent polling does not increment this value. It may update the stored location
   * data, but it should not be treated as a user-visible refresh event.
   */
  private var nextRefreshId = 0L

  /**
   * Resolves location with a full loading state.
   *
   * Use this for first load or explicit retry when the current content can be
   * replaced by [LocationUiState.Loading].
   *
   * A completed request increments `refreshId`.
   */
  fun resolveLocation() {
    resolveLocation(operation = ResolveOperation.FullLoading)
  }

  /**
   * Resolves location as part of pull-to-refresh.
   *
   * Unlike [resolveLocation], this keeps the current Success/Unknown content on screen
   * and marks it with [LocationResolveMode.PullRefresh].
   *
   * A completed request increments `refreshId`.
   */
  fun refreshLocation() {
    resolveLocation(operation = ResolveOperation.PullRefresh)
  }

  /**
   * Resolves location in the background without changing visible UI mode.
   *
   * If a visible request is already active, this call is ignored. Polling should never
   * cancel or override full loading or pull-to-refresh.
   *
   * If the request succeeds, the state is updated with a fresh [LocationUiState.Success],
   * but the previous `refreshId` is preserved.
   *
   * If the request fails, the previous state is kept unchanged.
   */
  fun pollLocation() {
    resolveLocation(operation = ResolveOperation.Silent)
  }

  /**
   * Starts a new location resolving request.
   *
   * Flow:
   *
   * 1. Ignore silent polling if a visible request is already active.
   * 2. Cancel the previous request when starting a new accepted request.
   * 3. Update the state according to [ResolveOperation].
   * 4. Call [LocationSource.resolve].
   * 5. Emit Success if the source returns a location.
   * 6. Emit Unknown or keep the previous state if the source fails.
   *
   * Important: [CancellationException] is rethrown, not swallowed. Otherwise a cancelled
   * old request could still write stale state after a newer request starts.
   */
  private fun resolveLocation(
    operation: ResolveOperation
  ) {
    val currentState = _state.value

    if (
      operation == ResolveOperation.Silent &&
      currentState.resolveMode != LocationResolveMode.Idle
    ) {
      return
    }

    cancelResolveJob()

    _state.value = when (operation) {
      ResolveOperation.FullLoading -> {
        LocationUiState.Loading
      }

      ResolveOperation.PullRefresh -> {
        when (currentState) {
          LocationUiState.Loading -> {
            LocationUiState.Loading
          }

          is LocationUiState.Success -> {
            currentState.copy(
              resolveMode = LocationResolveMode.PullRefresh
            )
          }

          is LocationUiState.Unknown -> {
            currentState.copy(
              resolveMode = LocationResolveMode.PullRefresh
            )
          }
        }
      }

      ResolveOperation.Silent -> {
        currentState
      }
    }

    resolveJob = viewModelScope.launch {
      val locatedPlace = resolveLocatedPlaceOrNull()

      if (!isActive) {
        return@launch
      }

      if (locatedPlace != null) {
        val refreshId = when (operation) {
          ResolveOperation.FullLoading,
          ResolveOperation.PullRefresh -> {
            ++nextRefreshId
          }

          ResolveOperation.Silent -> {
            currentState.refreshId
          }
        }

        _state.value = LocationUiState.Success(
          data = locatedPlace,
          refreshId = refreshId,
          resolveMode = LocationResolveMode.Idle
        )
      } else {
        _state.value = when (operation) {
          ResolveOperation.FullLoading,
          ResolveOperation.PullRefresh -> {
            LocationUiState.Unknown(
              message = UnknownLocationMessage,
              refreshId = ++nextRefreshId,
              resolveMode = LocationResolveMode.Idle
            )
          }

          ResolveOperation.Silent -> {
            currentState
          }
        }
      }
    }
  }

  private suspend fun resolveLocatedPlaceOrNull(): LocatedPlace? {
    return try {
      locationSource.resolve()
    } catch (error: CancellationException) {
      throw error
    } catch (_: Throwable) {
      null
    }
  }

  /**
   * Cancels the active location resolving job.
   *
   * This is needed when resolving becomes invalid from outside the resolver flow,
   * for example after Android location permission is denied.
   */
  private fun cancelResolveJob() {
    resolveJob?.cancel()
    resolveJob = null
  }

  /**
   * Marks current location as unavailable because Android location permission
   * is not granted.
   *
   * This cancels the active resolving job to prevent stale location results from
   * overwriting the permission-denied state.
   */
  fun markPermissionDenied() {
    cancelResolveJob()

    _state.value = LocationUiState.Unknown(
      message = LOCATION_PERMISSION_DENIED_MESSAGE,
      refreshId = ++nextRefreshId,
      resolveMode = LocationResolveMode.Idle
    )
  }

  /**
   * Describes what kind of resolving operation should be performed.
   *
   * This is intentionally separate from [LocationResolveMode]:
   *
   * - [LocationResolveMode] describes how the current state should look in UI;
   * - [ResolveOperation] describes how this ViewModel should perform the next request.
   *
   * Silent is an operation, not a UI mode.
   */
  private enum class ResolveOperation {

    /**
     * Replace the current UI state with full loading.
     */
    FullLoading,

    /**
     * Keep existing Success/Unknown content visible and mark it as pull-refreshing.
     */
    PullRefresh,

    /**
     * Resolve location in the background without changing visible UI mode.
     *
     * This operation is ignored if full loading or pull-to-refresh is already active.
     */
    Silent
  }

  companion object {

    /**
     * Creates a [LocationViewModel] that resolves the current device location.
     *
     * Internally this wraps [LocationResolver] into [CurrentLocationSource].
     */
    fun currentLocationFactory(
      locationResolver: LocationResolver
    ): ViewModelProvider.Factory {
      return viewModelFactory {
        LocationViewModel(
          locationSource = CurrentLocationSource(
            locationResolver = locationResolver
          )
        )
      }
    }

    /**
     * Creates a [LocationViewModel] that resolves a fixed coordinate pair.
     *
     * This is useful for screens where the app already has latitude/longitude and
     * only needs to turn them into a user-facing place representation.
     */
    fun coordinatesFactory(
      latitude: Double,
      longitude: Double,
      placeNameProvider: PlaceNameProvider
    ): ViewModelProvider.Factory {
      return viewModelFactory {
        LocationViewModel(
          locationSource = CoordinatesLocationSource(
            latitude = latitude,
            longitude = longitude,
            placeNameProvider = placeNameProvider
          )
        )
      }
    }

    /**
     * Creates a [LocationViewModel] with a custom [LocationSource].
     *
     * Use this when the caller wants full control over how location data is resolved.
     */
    fun factory(
      locationSource: LocationSource
    ): ViewModelProvider.Factory {
      return viewModelFactory {
        LocationViewModel(locationSource)
      }
    }
  }
}

/**
 * Returns the refresh id associated with the current state.
 *
 * [LocationUiState.Loading] has no completed resolving attempt, so it maps to `0L`.
 * Success and Unknown states carry their own refresh ids.
 *
 * This helper is used by silent polling to preserve the previous refresh id when
 * background resolving succeeds.
 */
private val LocationUiState.refreshId: Long
  get() = when (this) {
    LocationUiState.Loading -> 0L
    is LocationUiState.Success -> refreshId
    is LocationUiState.Unknown -> refreshId
  }