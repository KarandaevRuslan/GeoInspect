package com.karandaev.geo_inspect.feature.presentation.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.presentation.location.LocationUiState
import com.karandaev.geo_inspect.feature.ui.components.maps.OsmMapView

/**
 * Map screen with an interactive OSM map and note markers.
 */
@Composable
fun MapScreen(
  inspectionReports: List<InspectionReport>,
  locationState: LocationUiState,
  focusRequest: Int,
  onFocusClick: () -> Unit,
  onMarkerClick: (Long) -> Unit,
  onLongPressCreateNote: (Double, Double) -> Unit
) {
  OsmMapView(
    inspectionReports = inspectionReports,
    locationState = locationState,
    modifier = Modifier.fillMaxSize(),
    focusRequest = focusRequest,
    onFocusClick = onFocusClick,
    onNoteMarkerClick = onMarkerClick,
    onLongPress = onLongPressCreateNote
  )
}