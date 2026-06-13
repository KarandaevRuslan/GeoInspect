package com.karandaev.geo_inspect.feature.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.presentation.location.LocationUiState
import com.karandaev.geo_inspect.core.presentation.weather.WeatherUiState
import com.karandaev.geo_inspect.feature.presentation.home.contents.HomeContent

/**
 * Home screen UI.
 *
 * This composable is UI-only:
 * - it does not export files directly;
 * - it does not delete reports directly;
 * - it does not launch Android document pickers;
 * - it does not own location or weather controllers.
 *
 * @param inspectionReports Reports displayed in the list.
 * @param locationState Current location UI state.
 * @param weatherState Current weather UI state.
 * @param isPullRefreshing Whether pull refresh is currently active.
 * @param onInspectionReportClick Called when a report card is clicked.
 * @param onInspectionReportEdit Called when a report edit action is clicked.
 * @param onInspectionReportExport Called when a report export swipe action is triggered.
 * @param onInspectionReportDelete Called after a report delete animation finishes.
 * @param onPullRefresh Called when the user triggers pull refresh.
 * @param onWeatherCardRetry Called when the user retries weather loading.
 * @param modifier Modifier applied to the screen root.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
  inspectionReports: List<InspectionReport>,
  locationState: LocationUiState,
  weatherState: WeatherUiState,
  isPullRefreshing: Boolean,
  onInspectionReportClick: (Long) -> Unit,
  onInspectionReportEdit: (Long) -> Unit,
  onInspectionReportExport: (InspectionReport) -> Unit,
  onInspectionReportDelete: (Long) -> Unit,
  onPullRefresh: () -> Unit,
  onWeatherCardRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  val pullRefreshState = rememberPullRefreshState(
    refreshing = isPullRefreshing,
    onRefresh = onPullRefresh
  )

  Box(
    modifier = modifier
      .fillMaxSize()
      .pullRefresh(pullRefreshState)
  ) {
    HomeContent(
      inspectionReports = inspectionReports,
      locationState = locationState,
      weatherState = weatherState,
      onInspectionReportClick = onInspectionReportClick,
      onInspectionReportEdit = onInspectionReportEdit,
      onInspectionReportExport = onInspectionReportExport,
      onInspectionReportDelete = onInspectionReportDelete,
      onWeatherCardRetry = onWeatherCardRetry,
      modifier = Modifier.fillMaxSize()
    )

    PullRefreshIndicator(
      refreshing = isPullRefreshing,
      state = pullRefreshState,
      modifier = Modifier.align(Alignment.TopCenter)
    )
  }
}