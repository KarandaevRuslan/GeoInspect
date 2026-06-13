package com.karandaev.geo_inspect.feature.presentation.home

import androidx.compose.runtime.Composable
import com.karandaev.geo_inspect.app.rememberMyApp
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.json.export_json.rememberDirectoryExporter
import com.karandaev.geo_inspect.core.json.export_json.reports.exportInspectionReportDirectory
import com.karandaev.geo_inspect.core.presentation.locationweather.rememberLocationWeatherController

/**
 * Route-level container for the home feature.
 *
 * This composable owns home-level side effects:
 * - location and weather controller setup;
 * - single report export flow;
 * - report deletion callback delegation.
 *
 * [HomeScreen] remains stateless and UI-only.
 *
 * @param inspectionReports Reports displayed on the home screen.
 * @param onInspectionReportClick Called when a report should be opened.
 * @param onInspectionReportEdit Called when a report should be edited.
 * @param onInspectionReportDelete Called when a report should be deleted.
 */
@Composable
fun HomeRoute(
  inspectionReports: List<InspectionReport>,
  onInspectionReportClick: (Long) -> Unit,
  onInspectionReportEdit: (Long) -> Unit,
  onInspectionReportDelete: (Long) -> Unit
) {
  val app = rememberMyApp()

  val exportInspectionReport = rememberDirectoryExporter<InspectionReport>(
    exportToDirectory = { context, directoryUri, inspectionReport ->
      exportInspectionReportDirectory(
        context = context,
        parentDirectoryUri = directoryUri,
        inspectionReport = inspectionReport,
        moshi = app.moshi
      )
    },
    successMessage = "Report exported."
  )

  val locationWeatherController = rememberLocationWeatherController(
    locationViewModelKey = HOME_LOCATION_VIEW_MODEL_KEY,
    permissionViewModelKey = HOME_PERMISSION_VIEW_MODEL_KEY,
    weatherViewModelKey = HOME_WEATHER_VIEW_MODEL_KEY,
    locationResolver = app.locationResolver,
    weatherProvider = app.weatherProvider,
    requestPermissionOnStart = true
  )

  HomeScreen(
    inspectionReports = inspectionReports,
    locationState = locationWeatherController.locationState,
    weatherState = locationWeatherController.weatherState,
    isPullRefreshing = locationWeatherController.isPullRefreshing,
    onInspectionReportClick = onInspectionReportClick,
    onInspectionReportEdit = onInspectionReportEdit,
    onInspectionReportExport = exportInspectionReport,
    onInspectionReportDelete = onInspectionReportDelete,
    onPullRefresh = locationWeatherController::refresh,
    onWeatherCardRetry = locationWeatherController::retryWeather
  )
}