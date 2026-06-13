package com.karandaev.geo_inspect.feature.presentation.create.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import com.karandaev.geo_inspect.core.presentation.location.LocationUiState
import com.karandaev.geo_inspect.feature.ui.components.maps.MiniOsmMap
import com.karandaev.geo_inspect.feature.ui.components.maps.currentLocationPoint

/**
 * Location selection section for the create/edit inspection report screen.
 *
 * This component allows the user to:
 * - select a point on the map;
 * - focus or refresh current location on the map;
 * - use current device location as the selected inspection report point;
 * - adjust coordinates manually.
 *
 * Map focus and "Use current location" are intentionally separated:
 * - map focus only polls/focuses location;
 * - use current location clears manual point selection and requests map focus.
 *
 * @param selectedPoint Manually selected point, if any.
 * @param locationState Current location UI state.
 * @param focusRequest Counter used to request map focus.
 * @param onMapFocusClick Called when map focus action is clicked.
 * @param onUseCurrentLocationClick Called when current location should become the inspection report location.
 * @param onSelectedPointChange Called when the selected map point changes.
 * @param onLatitudeChange Called when latitude value changes.
 * @param onLongitudeChange Called when longitude value changes.
 * @param modifier Modifier applied to the section root.
 */
@Composable
internal fun CreateInspectionReportLocationSection(
  selectedPoint: MapPoint?,
  locationState: LocationUiState,
  focusRequest: Int,
  onMapFocusClick: () -> Unit,
  onUseCurrentLocationClick: () -> Unit,
  onSelectedPointChange: (MapPoint) -> Unit,
  onLatitudeChange: (Double, Double) -> Unit,
  onLongitudeChange: (Double, Double) -> Unit,
  modifier: Modifier = Modifier
) {
  val currentLocationPoint = locationState.currentLocationPoint
  val effectivePoint = selectedPoint ?: currentLocationPoint
  val isLocationLoading = locationState is LocationUiState.Loading

  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    CreateInspectionReportLocationHeader()

    MiniOsmMap(
      selectedPoint = selectedPoint,
      locationState = locationState,
      focusRequest = focusRequest,
      preferSelectedPointFocus = true,
      centerOnMarkerChange = true,
      onFocusClick = onMapFocusClick,
      onSelectedPointChange = onSelectedPointChange
    )

    UseCurrentLocationButton(
      isLoading = isLocationLoading,
      onClick = onUseCurrentLocationClick
    )

    CreateNoteCoordinateSection(
      point = effectivePoint,
      enabled = !isLocationLoading,
      onLatitudeChange = onLatitudeChange,
      onLongitudeChange = onLongitudeChange
    )
  }
}

/**
 * Displays title and hint for the location section.
 */
@Composable
private fun CreateInspectionReportLocationHeader() {
  Text(
    text = stringResource(id = R.string.create_inspection_report_location_label),
    style = MaterialTheme.typography.labelSmall,
    color = MaterialTheme.colorScheme.onSurfaceVariant
  )

  Text(
    text = stringResource(id = R.string.create_inspection_report_location_hint),
    style = MaterialTheme.typography.bodySmall,
    color = MaterialTheme.colorScheme.outline
  )
}

/**
 * Button that switches inspection report location source to current device location.
 *
 * @param isLoading Whether current location is being resolved.
 * @param onClick Called when the button is clicked.
 */
@Composable
private fun UseCurrentLocationButton(
  isLoading: Boolean,
  onClick: () -> Unit
) {
  OutlinedButton(
    enabled = !isLoading,
    onClick = onClick,
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(20.dp)
  ) {
    Text(
      text = stringResource(
        id = if (isLoading) {
          R.string.create_inspection_report_getting_current_location
        } else {
          R.string.create_inspection_report_use_current_location
        }
      )
    )
  }
}