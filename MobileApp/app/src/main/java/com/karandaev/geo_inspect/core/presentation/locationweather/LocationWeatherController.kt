package com.karandaev.geo_inspect.core.presentation.locationweather

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.karandaev.geo_inspect.core.location.place.PlaceNameProvider
import com.karandaev.geo_inspect.core.location.resolver.LocationResolver
import com.karandaev.geo_inspect.core.presentation.location.LocationController
import com.karandaev.geo_inspect.core.presentation.location.LocationResolveMode
import com.karandaev.geo_inspect.core.presentation.location.LocationUiState
import com.karandaev.geo_inspect.core.presentation.location.rememberLocationController
import com.karandaev.geo_inspect.core.presentation.map.toMapPointOrNull
import com.karandaev.geo_inspect.core.presentation.weather.WeatherResolveMode
import com.karandaev.geo_inspect.core.presentation.weather.WeatherUiState
import com.karandaev.geo_inspect.core.presentation.weather.WeatherViewModel
import com.karandaev.geo_inspect.core.util.formatters.formatCoordinate
import com.karandaev.geo_inspect.core.weather.WeatherProvider

/**
 * UI-facing controller that connects location resolving with weather loading.
 *
 * This controller owns the orchestration between:
 *
 * LocationController -> current MapPoint provider -> WeatherViewModel
 *
 * It intentionally does not belong to any specific screen or route.
 */
class LocationWeatherController internal constructor(
  val locationState: LocationUiState,
  val weatherState: WeatherUiState,
  val isPullRefreshing: Boolean,
  private val refreshAction: () -> Unit,
  private val retryWeatherAction: () -> Unit
) {

  /**
   * Refreshes location first.
   *
   * Weather is refreshed later by the controller after location refresh finishes
   * successfully, so weather is loaded for the latest resolved point.
   */
  fun refresh() {
    refreshAction()
  }

  /**
   * Retries weather loading.
   *
   * If location is already resolved and idle, weather is loaded for the current point.
   * Otherwise location resolving is started first.
   */
  fun retryWeather() {
    retryWeatherAction()
  }
}

/**
 * Creates a generic location/weather controller for current device location.
 *
 * This overload uses Android runtime permission handling through [rememberLocationController].
 */
@Composable
fun rememberLocationWeatherController(
  locationViewModelKey: String,
  permissionViewModelKey: String,
  weatherViewModelKey: String,
  locationResolver: LocationResolver,
  weatherProvider: WeatherProvider,
  resolveOnPermissionGranted: Boolean = true,
  resolveOnResume: Boolean = true,
  requestPermissionOnStart: Boolean = false
): LocationWeatherController {
  val locationController = rememberLocationController(
    locationViewModelKey = locationViewModelKey,
    permissionViewModelKey = permissionViewModelKey,
    locationResolver = locationResolver,
    resolveOnPermissionGranted = resolveOnPermissionGranted,
    resolveOnResume = resolveOnResume,
    requestPermissionOnStart = requestPermissionOnStart
  )

  return rememberLocationWeatherController(
    locationController = locationController,
    weatherViewModelKey = weatherViewModelKey,
    weatherProvider = weatherProvider
  )
}

/**
 * Creates a generic location/weather controller for a fixed coordinate pair.
 *
 * This overload does not use Android runtime permission handling because the app
 * already has explicit coordinates.
 */
@Composable
fun rememberLocationWeatherController(
  locationViewModelKey: String,
  weatherViewModelKey: String,
  latitude: Double,
  longitude: Double,
  placeNameProvider: PlaceNameProvider,
  weatherProvider: WeatherProvider,
  resolveOnStart: Boolean = true
): LocationWeatherController {
  val coordinateKey = remember(
    latitude,
    longitude
  ) {
    "${formatCoordinate(latitude)}:${formatCoordinate(longitude)}"
  }

  val locationController = rememberLocationController(
    locationViewModelKey = "$locationViewModelKey:$coordinateKey",
    latitude = latitude,
    longitude = longitude,
    placeNameProvider = placeNameProvider,
    resolveOnStart = resolveOnStart
  )

  return rememberLocationWeatherController(
    locationController = locationController,
    weatherViewModelKey = "$weatherViewModelKey:$coordinateKey",
    weatherProvider = weatherProvider
  )
}

/**
 * Shared implementation for any location source.
 *
 * The source can be current device location or fixed coordinates. This function only
 * needs a [LocationController] that exposes [LocationUiState] and location actions.
 */
@Composable
private fun rememberLocationWeatherController(
  locationController: LocationController,
  weatherViewModelKey: String,
  weatherProvider: WeatherProvider
): LocationWeatherController {
  val weatherViewModel: WeatherViewModel = viewModel(
    key = weatherViewModelKey,
    factory = WeatherViewModel.factory(
      weatherProvider = weatherProvider
    )
  )

  val weatherState by weatherViewModel.state.collectAsStateWithLifecycle()

  var shouldRefreshWeatherAfterLocationRefresh by remember {
    mutableStateOf(false)
  }

  val locationEffectKey = locationController.locationState.toLocationEffectKey()

  /**
   * Location -> weather orchestration.
   *
   * Regular flow:
   *
   * 1. location resolves successfully;
   * 2. weather loads for the current location.
   *
   * Pull-refresh flow:
   *
   * 1. refresh location;
   * 2. wait until location leaves PullRefresh mode;
   * 3. refresh weather for the latest location.
   */
  LaunchedEffect(locationEffectKey) {
    when (val locationState = locationController.locationState) {
      LocationUiState.Loading -> Unit

      is LocationUiState.Success -> {
        weatherViewModel.updateCurrentPoint(
          point = locationState.toMapPointOrNull()
        )

        when (locationState.resolveMode) {
          LocationResolveMode.PullRefresh -> {
            shouldRefreshWeatherAfterLocationRefresh = true
          }

          LocationResolveMode.FullLoading -> Unit

          LocationResolveMode.Idle -> {
            if (shouldRefreshWeatherAfterLocationRefresh) {
              shouldRefreshWeatherAfterLocationRefresh = false
              weatherViewModel.refreshWeather()
            } else {
              weatherViewModel.loadWeather()
            }
          }
        }
      }

      is LocationUiState.Unknown -> {
        weatherViewModel.updateCurrentPoint(point = null)

        if (locationState.resolveMode == LocationResolveMode.Idle) {
          shouldRefreshWeatherAfterLocationRefresh = false
        }
      }
    }
  }

  val isPullRefreshing =
    shouldRefreshWeatherAfterLocationRefresh ||
      locationController.locationState.resolveMode == LocationResolveMode.PullRefresh ||
      weatherState.resolveMode == WeatherResolveMode.PullRefresh

  return LocationWeatherController(
    locationState = locationController.locationState,
    weatherState = weatherState,
    isPullRefreshing = isPullRefreshing,
    refreshAction = {
      locationController.refreshLocation(
        additionalAction = {
          shouldRefreshWeatherAfterLocationRefresh = true
        }
      )
    },
    retryWeatherAction = {
      when (val currentLocationState = locationController.locationState) {
        LocationUiState.Loading -> Unit

        is LocationUiState.Success -> {
          if (currentLocationState.resolveMode == LocationResolveMode.Idle) {
            weatherViewModel.updateCurrentPoint(
              point = currentLocationState.toMapPointOrNull()
            )
            weatherViewModel.loadWeather()
          }
        }

        is LocationUiState.Unknown -> {
          if (currentLocationState.resolveMode == LocationResolveMode.Idle) {
            weatherViewModel.updateCurrentPoint(point = null)
            locationController.resolveLocation()
          }
        }
      }
    }
  )
}

private data class LocationEffectKey(
  val stateKind: LocationStateKind,
  val resolveMode: LocationResolveMode,
  val refreshId: Long
)

private enum class LocationStateKind {
  Loading,
  Success,
  Unknown
}

private fun LocationUiState.toLocationEffectKey(): LocationEffectKey {
  return when (this) {
    LocationUiState.Loading -> {
      LocationEffectKey(
        stateKind = LocationStateKind.Loading,
        resolveMode = resolveMode,
        refreshId = 0L
      )
    }

    is LocationUiState.Success -> {
      LocationEffectKey(
        stateKind = LocationStateKind.Success,
        resolveMode = resolveMode,
        refreshId = refreshId
      )
    }

    is LocationUiState.Unknown -> {
      LocationEffectKey(
        stateKind = LocationStateKind.Unknown,
        resolveMode = resolveMode,
        refreshId = refreshId
      )
    }
  }
}