package com.karandaev.geo_inspect.feature.ui.components.maps

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import com.karandaev.geo_inspect.core.presentation.location.LocationUiState
import com.karandaev.geo_inspect.core.ui.components.map.AdaptiveOsmMap
import com.karandaev.geo_inspect.core.ui.components.map.controls.model.OsmMapControls
import com.karandaev.geo_inspect.core.ui.components.map.controls.model.OsmMapInteractions
import com.karandaev.geo_inspect.core.ui.components.map.viewport.OsmMapViewport

private const val MINI_HEIGHT_DP = 180

/**
 * Compact map for selecting or previewing a single location.
 *
 * This legacy-compatible wrapper maps compact location selection onto [AdaptiveOsmMap].
 * Current location is provided through [LocationUiState], while selected location is
 * controlled by the caller.
 *
 * The map can display up to two markers:
 * - current location from [locationState];
 * - selected location from [selectedPoint].
 *
 * Focus target priority is controlled by [preferSelectedPointFocus]:
 * - `true`: selected point is focused first, current location is used as fallback;
 * - `false`: current location is focused first, selected point is used as fallback.
 *
 * [centerOnMarkerChange] controls whether the map should automatically center when
 * the marker set changes.
 *
 * Tapping the map emits a new selected [MapPoint] through [onSelectedPointChange].
 * Location resolving, permission handling, polling, and focus request ownership stay
 * outside this composable.
 */
@Composable
fun MiniOsmMap(
  selectedPoint: MapPoint?,
  locationState: LocationUiState,
  @SuppressLint("ModifierParameter")
  modifier: Modifier = Modifier,
  focusRequest: Int = 0,
  preferSelectedPointFocus: Boolean = true,
  centerOnMarkerChange: Boolean = true,
  showZoomControls: Boolean = true,
  showCurrentLocationButton: Boolean = true,
  onFocusClick: (() -> Unit)? = null,
  onSelectedPointChange: ((MapPoint) -> Unit)? = null
) {
  val currentLocationPoint = locationState.currentLocationPoint

  val focusTarget = remember(
    selectedPoint,
    currentLocationPoint,
    preferSelectedPointFocus
  ) {
    resolveMiniMapFocusTarget(
      selectedPoint = selectedPoint,
      currentLocationPoint = currentLocationPoint,
      preferSelectedPointFocus = preferSelectedPointFocus
    )
  }

  val markers = remember(
    selectedPoint,
    currentLocationPoint
  ) {
    buildMiniMapMarkers(
      selectedPoint = selectedPoint,
      currentLocationPoint = currentLocationPoint
    )
  }

  AdaptiveOsmMap(
    markers = markers,
    modifier = modifier,
    viewport = OsmMapViewport.Card(
      height = MINI_HEIGHT_DP.dp
    ),
    controls = OsmMapControls(
      showZoomControls = showZoomControls,
      showFocusButton = showCurrentLocationButton,
      focusButtonText = "⌖",
      onFocusClick = onFocusClick
    ),
    interactions = OsmMapInteractions(
      onTap = onSelectedPointChange
    ),
    focusTarget = focusTarget,
    focusRequest = focusRequest,
    centerOnMarkerChange = centerOnMarkerChange,
    isLoading = locationState.isInitialLocationLoading
  )
}

/**
 * Resolves which point should be used as the map focus target.
 *
 * @param selectedPoint Point selected by the user.
 * @param currentLocationPoint Current device location point.
 * @param preferSelectedPointFocus Whether selected point should have higher priority.
 * @return Focus target point, or null when no point is available.
 */
private fun resolveMiniMapFocusTarget(
  selectedPoint: MapPoint?,
  currentLocationPoint: MapPoint?,
  preferSelectedPointFocus: Boolean
): MapPoint? {
  return if (preferSelectedPointFocus) {
    selectedPoint ?: currentLocationPoint
  } else {
    currentLocationPoint ?: selectedPoint
  }
}