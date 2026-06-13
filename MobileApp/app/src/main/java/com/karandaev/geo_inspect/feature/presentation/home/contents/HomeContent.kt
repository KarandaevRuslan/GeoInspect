package com.karandaev.geo_inspect.feature.presentation.home.contents

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.karandaev.geo_inspect.app.adaptive_layout.AdaptiveLayoutType
import com.karandaev.geo_inspect.app.adaptive_layout.rememberAdaptiveLayoutType
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.presentation.location.LocationUiState
import com.karandaev.geo_inspect.core.presentation.weather.WeatherUiState

/**
 * Main scrollable home content.
 *
 * Owns only list-local UI state, such as reports currently playing delete animation.
 *
 * @param inspectionReports Reports displayed in the list.
 * @param locationState Current location UI state.
 * @param weatherState Current weather UI state.
 * @param onInspectionReportClick Called when a report card is clicked.
 * @param onInspectionReportEdit Called when a report edit action is clicked.
 * @param onInspectionReportExport Called when a report export swipe action is triggered.
 * @param onInspectionReportDelete Called after a report delete animation finishes.
 * @param onWeatherCardRetry Called when the user retries weather loading.
 * @param modifier Modifier applied to the content root.
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
internal fun HomeContent(
  inspectionReports: List<InspectionReport>,
  locationState: LocationUiState,
  weatherState: WeatherUiState,
  onInspectionReportClick: (Long) -> Unit,
  onInspectionReportEdit: (Long) -> Unit,
  onInspectionReportExport: (InspectionReport) -> Unit,
  onInspectionReportDelete: (Long) -> Unit,
  onWeatherCardRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  var deletingInspectionReportIds by remember {
    mutableStateOf<Set<Long>>(emptySet())
  }

  fun requestInspectionReportDelete(
    inspectionReportId: Long
  ) {
    if (!deletingInspectionReportIds.contains(inspectionReportId)) {
      deletingInspectionReportIds = deletingInspectionReportIds + inspectionReportId
    }
  }

  fun finishInspectionReportDelete(
    inspectionReportId: Long
  ) {
    onInspectionReportDelete(inspectionReportId)
    deletingInspectionReportIds = deletingInspectionReportIds - inspectionReportId
  }

  BoxWithConstraints(
    modifier = modifier.fillMaxSize()
  ) {
    val layoutType = rememberAdaptiveLayoutType(
      maxWidth = maxWidth,
      maxHeight = maxHeight
    )

    when (layoutType) {
      AdaptiveLayoutType.SinglePane -> {
        HomeSinglePaneContent(
          inspectionReports = inspectionReports,
          locationState = locationState,
          weatherState = weatherState,
          deletingInspectionReportIds = deletingInspectionReportIds,
          onInspectionReportClick = onInspectionReportClick,
          onInspectionReportEdit = onInspectionReportEdit,
          onInspectionReportExport = onInspectionReportExport,
          onInspectionReportDeleteRequest = ::requestInspectionReportDelete,
          onInspectionReportDeleteAnimationFinished = ::finishInspectionReportDelete,
          onWeatherCardRetry = onWeatherCardRetry,
          modifier = Modifier.fillMaxSize()
        )
      }

      AdaptiveLayoutType.TwoPane -> {
        HomeTwoPaneContent(
          inspectionReports = inspectionReports,
          locationState = locationState,
          weatherState = weatherState,
          deletingInspectionReportIds = deletingInspectionReportIds,
          onInspectionReportClick = onInspectionReportClick,
          onInspectionReportEdit = onInspectionReportEdit,
          onInspectionReportExport = onInspectionReportExport,
          onInspectionReportDeleteRequest = ::requestInspectionReportDelete,
          onInspectionReportDeleteAnimationFinished = ::finishInspectionReportDelete,
          onWeatherCardRetry = onWeatherCardRetry,
          modifier = Modifier.fillMaxSize()
        )
      }
    }
  }
}