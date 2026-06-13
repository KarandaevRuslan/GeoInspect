package com.karandaev.geo_inspect.feature.presentation.view.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.presentation.location.LocationUiState
import com.karandaev.geo_inspect.core.presentation.weather.WeatherUiState

@Composable
internal fun ViewInspectionReportSupportingSection(
  inspectionReport: InspectionReport,
  inspectionReportLocationState: LocationUiState,
  currentLocationState: LocationUiState,
  inspectionReportWeatherState: WeatherUiState,
  onMapFocusClick: () -> Unit,
  onWeatherCardRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  androidx.compose.foundation.layout.Column(
    modifier = modifier
  ) {
    ViewInspectionReportLocationSection(
      inspectionReport = inspectionReport,
      currentLocationState = currentLocationState,
      onMapFocusClick = onMapFocusClick
    )

    ViewInspectionReportWeatherSection(
      inspectionLocationState = inspectionReportLocationState,
      inspectionWeatherState = inspectionReportWeatherState,
      onRetry = onWeatherCardRetry
    )
  }
}