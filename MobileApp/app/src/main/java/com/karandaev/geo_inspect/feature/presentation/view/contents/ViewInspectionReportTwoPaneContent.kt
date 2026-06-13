package com.karandaev.geo_inspect.feature.presentation.view.contents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.domain.model.detection.Detection
import com.karandaev.geo_inspect.core.presentation.location.LocationUiState
import com.karandaev.geo_inspect.core.presentation.weather.WeatherUiState
import com.karandaev.geo_inspect.feature.presentation.view.components.ViewInspectionReportActions
import com.karandaev.geo_inspect.feature.presentation.view.components.ViewInspectionReportContentSection
import com.karandaev.geo_inspect.feature.presentation.view.components.ViewInspectionReportSupportingSection
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.DetectionPreviewDialogOrientation
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.InspectionReportDetectionPreviewWithDialog

private val ViewInspectionReportTwoPaneHorizontalPadding = 16.dp
private val ViewInspectionReportTwoPaneColumnTopPadding = 16.dp
private val ViewInspectionReportTwoPaneColumnBottomPadding = 24.dp
private val ViewInspectionReportTwoPaneSpacing = 16.dp

@Composable
internal fun ViewInspectionReportTwoPaneContent(
  inspectionReport: InspectionReport,
  detectionImagePath: String?,
  detections: List<Detection>,
  hasDetectionPreview: Boolean,
  isDetectionPreviewDialogVisible: Boolean,
  lastUpdatedText: String,
  inspectionReportLocationState: LocationUiState,
  currentLocationState: LocationUiState,
  inspectionReportWeatherState: WeatherUiState,
  onDetectionPreviewClick: () -> Unit,
  onDismissDetectionPreviewDialogRequest: () -> Unit,
  onMapFocusClick: () -> Unit,
  onWeatherCardRetry: () -> Unit,
  onEditClick: () -> Unit,
  onDeleteClick: () -> Unit,
  onBackClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.padding(
      horizontal = ViewInspectionReportTwoPaneHorizontalPadding
    ),
    horizontalArrangement = Arrangement.spacedBy(ViewInspectionReportTwoPaneSpacing)
  ) {
    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxHeight()
        .verticalScroll(rememberScrollState())
        .padding(
          top = ViewInspectionReportTwoPaneColumnTopPadding,
          bottom = ViewInspectionReportTwoPaneColumnBottomPadding
        ),
      verticalArrangement = Arrangement.spacedBy(ViewInspectionReportTwoPaneSpacing)
    ) {
      ViewInspectionReportContentSection(
        inspectionReport = inspectionReport,
        lastUpdatedText = lastUpdatedText,
        showTitle = true,
        showBackButton = true,
        onBackClick = onBackClick
      )

      if (hasDetectionPreview) {
        InspectionReportDetectionPreviewWithDialog(
          detections = detections,
          imagePath = detectionImagePath,
          isDialogVisible = isDetectionPreviewDialogVisible,
          dialogOrientation = DetectionPreviewDialogOrientation.Landscape,
          onPreviewClick = onDetectionPreviewClick,
          onDismissDialogRequest = onDismissDetectionPreviewDialogRequest
        )
      }

      ViewInspectionReportActions(
        onEditClick = onEditClick,
        onDeleteClick = onDeleteClick
      )
    }

    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxHeight()
        .verticalScroll(rememberScrollState())
        .padding(
          top = ViewInspectionReportTwoPaneColumnTopPadding,
          bottom = ViewInspectionReportTwoPaneColumnBottomPadding
        ),
      verticalArrangement = Arrangement.spacedBy(ViewInspectionReportTwoPaneSpacing)
    ) {
      ViewInspectionReportSupportingSection(
        inspectionReport = inspectionReport,
        inspectionReportLocationState = inspectionReportLocationState,
        currentLocationState = currentLocationState,
        inspectionReportWeatherState = inspectionReportWeatherState,
        onMapFocusClick = onMapFocusClick,
        onWeatherCardRetry = onWeatherCardRetry
      )
    }
  }
}