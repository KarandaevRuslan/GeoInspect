package com.karandaev.geo_inspect.feature.presentation.home.contents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.presentation.location.LocationUiState
import com.karandaev.geo_inspect.core.presentation.weather.WeatherUiState
import com.karandaev.geo_inspect.feature.presentation.home.components.HomeDigitalClockCard
import com.karandaev.geo_inspect.feature.presentation.home.components.HomeInspectionReportsHeader
import com.karandaev.geo_inspect.feature.presentation.home.components.homeInspectionReportItems
import com.karandaev.geo_inspect.feature.ui.components.cards.WeatherCard

private val HomeTwoPaneHorizontalPadding = 16.dp
private val HomeTwoPaneTopPadding = 16.dp
private val HomeTwoPaneBottomPadding = 30.dp
private val HomeTwoPaneHorizontalSpacing = 16.dp
private val HomeTwoPaneVerticalSpacing = 12.dp

@Composable
internal fun HomeTwoPaneContent(
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
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(HomeTwoPaneHorizontalSpacing)
  ) {
    LazyColumn(
      modifier = Modifier
        .weight(1f)
        .fillMaxHeight(),
      contentPadding = PaddingValues(
        start = HomeTwoPaneHorizontalPadding,
        top = HomeTwoPaneTopPadding,
        bottom = HomeTwoPaneBottomPadding
      ),
      verticalArrangement = Arrangement.spacedBy(HomeTwoPaneVerticalSpacing)
    ) {
      item {
        Text(
          text = stringResource(id = R.string.destination_home),
          style = MaterialTheme.typography.headlineLarge,
          color = MaterialTheme.colorScheme.onSurface
        )
      }

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
    }

    LazyColumn(
      modifier = Modifier
        .weight(1f)
        .fillMaxHeight(),
      contentPadding = PaddingValues(
        top = HomeTwoPaneTopPadding,
        end = HomeTwoPaneHorizontalPadding,
        bottom = HomeTwoPaneBottomPadding
      ),
      verticalArrangement = Arrangement.spacedBy(HomeTwoPaneVerticalSpacing)
    ) {
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
}