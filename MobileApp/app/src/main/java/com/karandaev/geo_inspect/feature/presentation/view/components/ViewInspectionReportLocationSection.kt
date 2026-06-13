package com.karandaev.geo_inspect.feature.presentation.view.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.util.mappers.toMapPoint
import com.karandaev.geo_inspect.core.presentation.location.LocationUiState
import com.karandaev.geo_inspect.feature.ui.components.maps.MiniOsmMap

@Composable
internal fun ViewInspectionReportLocationSection(
  inspectionReport: InspectionReport,
  currentLocationState: LocationUiState,
  onMapFocusClick: () -> Unit
) {
  Text(
    text = "Location",
    style = MaterialTheme.typography.labelLarge,
    color = MaterialTheme.colorScheme.onSurfaceVariant,
    modifier = Modifier.padding(top = 20.dp, bottom = 8.dp)
  )

  MiniOsmMap(
    selectedPoint = inspectionReport.toMapPoint(),
    locationState = currentLocationState,
    showZoomControls = true,
    onFocusClick = onMapFocusClick
  )
}