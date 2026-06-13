package com.karandaev.geo_inspect.feature.presentation.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.karandaev.geo_inspect.app.adaptive_layout.AdaptiveLayoutType
import com.karandaev.geo_inspect.app.adaptive_layout.rememberAdaptiveLayoutType
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.domain.model.detection.Detection
import com.karandaev.geo_inspect.core.presentation.location.LocationUiState
import com.karandaev.geo_inspect.core.presentation.weather.WeatherUiState
import com.karandaev.geo_inspect.core.util.formatters.formatLastUpdatedAt
import com.karandaev.geo_inspect.feature.presentation.view.contents.ViewInspectionReportSinglePaneContent
import com.karandaev.geo_inspect.feature.presentation.view.contents.ViewInspectionReportTwoPaneContent

/**
 * Inspection report details screen.
 *
 * This screen is UI-only. It does not create ViewModels and does not resolve
 * detection image paths from report ids.
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ViewInspectionReportScreen(
  inspectionReport: InspectionReport,
  detectionImagePath: String?,
  detections: List<Detection>,
  isDetectionPreviewDialogVisible: Boolean,
  inspectionReportLocationState: LocationUiState,
  currentLocationState: LocationUiState,
  inspectionReportWeatherState: WeatherUiState,
  isPullRefreshing: Boolean,
  onPullRefresh: () -> Unit,
  onDetectionPreviewClick: () -> Unit,
  onDismissDetectionPreviewDialogRequest: () -> Unit,
  onMapFocusClick: () -> Unit,
  onWeatherCardRetry: () -> Unit,
  onEditClick: () -> Unit,
  onDeleteClick: () -> Unit,
  onBackClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val pullRefreshState = rememberPullRefreshState(
    refreshing = isPullRefreshing,
    onRefresh = onPullRefresh
  )

  val lastUpdatedText = remember(inspectionReport.lastUpdatedAtMillis) {
    formatLastUpdatedAt(inspectionReport.lastUpdatedAtMillis)
  }

  val hasDetectionPreview = inspectionReport.detectResponse != null ||
    !detectionImagePath.isNullOrBlank()

  Box(
    modifier = modifier
      .fillMaxSize()
      .pullRefresh(pullRefreshState)
  ) {
    BoxWithConstraints(
      modifier = Modifier.fillMaxSize()
    ) {
      val layoutType = rememberAdaptiveLayoutType(
        maxWidth = maxWidth,
        maxHeight = maxHeight
      )

      when (layoutType) {
        AdaptiveLayoutType.SinglePane -> {
          ViewInspectionReportSinglePaneContent(
            inspectionReport = inspectionReport,
            detectionImagePath = detectionImagePath,
            detections = detections,
            hasDetectionPreview = hasDetectionPreview,
            isDetectionPreviewDialogVisible = isDetectionPreviewDialogVisible,
            lastUpdatedText = lastUpdatedText,
            inspectionReportLocationState = inspectionReportLocationState,
            currentLocationState = currentLocationState,
            inspectionReportWeatherState = inspectionReportWeatherState,
            onDetectionPreviewClick = onDetectionPreviewClick,
            onDismissDetectionPreviewDialogRequest = onDismissDetectionPreviewDialogRequest,
            onMapFocusClick = onMapFocusClick,
            onWeatherCardRetry = onWeatherCardRetry,
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick,
            modifier = Modifier.fillMaxSize()
          )
        }

        AdaptiveLayoutType.TwoPane -> {
          ViewInspectionReportTwoPaneContent(
            inspectionReport = inspectionReport,
            detectionImagePath = detectionImagePath,
            detections = detections,
            hasDetectionPreview = hasDetectionPreview,
            isDetectionPreviewDialogVisible = isDetectionPreviewDialogVisible,
            lastUpdatedText = lastUpdatedText,
            inspectionReportLocationState = inspectionReportLocationState,
            currentLocationState = currentLocationState,
            inspectionReportWeatherState = inspectionReportWeatherState,
            onDetectionPreviewClick = onDetectionPreviewClick,
            onDismissDetectionPreviewDialogRequest = onDismissDetectionPreviewDialogRequest,
            onMapFocusClick = onMapFocusClick,
            onWeatherCardRetry = onWeatherCardRetry,
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick,
            onBackClick = onBackClick,
            modifier = Modifier.fillMaxSize()
          )
        }
      }
    }

    PullRefreshIndicator(
      refreshing = isPullRefreshing,
      state = pullRefreshState,
      modifier = Modifier.align(Alignment.TopCenter)
    )
  }
}