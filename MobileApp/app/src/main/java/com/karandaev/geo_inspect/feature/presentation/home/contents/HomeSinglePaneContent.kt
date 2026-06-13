package com.karandaev.geo_inspect.feature.presentation.home.contents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.presentation.location.LocationUiState
import com.karandaev.geo_inspect.core.presentation.weather.WeatherUiState
import com.karandaev.geo_inspect.feature.presentation.home.components.HomeDigitalClockCard
import com.karandaev.geo_inspect.feature.presentation.home.components.HomeInspectionReportsHeader
import com.karandaev.geo_inspect.feature.presentation.home.components.homeInspectionReportItems
import com.karandaev.geo_inspect.feature.ui.components.cards.WeatherCard

private val HomeSinglePaneHorizontalPadding = 16.dp
private val HomeSinglePaneTopPadding = 8.dp
private val HomeSinglePaneBottomPadding = 90.dp
private val HomeSinglePaneSpacing = 12.dp

@Composable
internal fun HomeSinglePaneContent(
  inspectionReports: List<InspectionReport>,
  locationState: LocationUiState,
  weatherState: WeatherUiState,
  deletingInspectionReportIds: Set<Long>,
  onInspectionReportClick: (Long) -> Unit,
  onInspectionReportEdit: (Long) -> Unit,
  onInspectionReportExport: (InspectionReport) -> Unit,
  onInspectionReportDeleteRequest: (Long) -> Unit,
  onInspectionReportDeleteAnimationFinished: (Long) -> Unit,
  onWeatherCardRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier,
    contentPadding = PaddingValues(
      start = HomeSinglePaneHorizontalPadding,
      top = HomeSinglePaneTopPadding,
      end = HomeSinglePaneHorizontalPadding,
      bottom = HomeSinglePaneBottomPadding
    ),
    verticalArrangement = Arrangement.spacedBy(HomeSinglePaneSpacing)
  ) {
    item {
      WeatherCard(
        locationState = locationState,
        weatherState = weatherState,
        onRetry = onWeatherCardRetry
      )
    }

    item {
      HomeDigitalClockCard()
    }

    item {
      HomeInspectionReportsHeader()
    }

    homeInspectionReportItems(
      inspectionReports = inspectionReports,
      deletingInspectionReportIds = deletingInspectionReportIds,
      onInspectionReportClick = onInspectionReportClick,
      onInspectionReportEdit = onInspectionReportEdit,
      onInspectionReportExport = onInspectionReportExport,
      onInspectionReportDeleteRequest = onInspectionReportDeleteRequest,
      onInspectionReportDeleteAnimationFinished = onInspectionReportDeleteAnimationFinished
    )
  }
}