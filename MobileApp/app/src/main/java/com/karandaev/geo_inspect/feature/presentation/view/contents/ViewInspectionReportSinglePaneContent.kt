package com.karandaev.geo_inspect.feature.presentation.view.contents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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

@Composable
internal fun ViewInspectionReportSinglePaneContent(
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
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(
        start = 16.dp,
        top = 8.dp,
        end = 16.dp,
        bottom = 24.dp
      ),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    ViewInspectionReportContentSection(
      inspectionReport = inspectionReport,
      lastUpdatedText = lastUpdatedText
    )

    if (hasDetectionPreview) {
      InspectionReportDetectionPreviewWithDialog(
        detections = detections,
        imagePath = detectionImagePath,
        isDialogVisible = isDetectionPreviewDialogVisible,
        dialogOrientation = DetectionPreviewDialogOrientation.Portrait,
        onPreviewClick = onDetectionPreviewClick,
        onDismissDialogRequest = onDismissDetectionPreviewDialogRequest
      )
    }

    ViewInspectionReportSupportingSection(
      inspectionReport = inspectionReport,
      inspectionReportLocationState = inspectionReportLocationState,
      currentLocationState = currentLocationState,
      inspectionReportWeatherState = inspectionReportWeatherState,
      onMapFocusClick = onMapFocusClick,
      onWeatherCardRetry = onWeatherCardRetry
    )

    ViewInspectionReportActions(
      onEditClick = onEditClick,
      onDeleteClick = onDeleteClick
    )
  }
}