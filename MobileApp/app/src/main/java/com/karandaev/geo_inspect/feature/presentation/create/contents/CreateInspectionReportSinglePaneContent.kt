package com.karandaev.geo_inspect.feature.presentation.create.contents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.core.domain.model.detection.DetectResponse
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import com.karandaev.geo_inspect.core.presentation.location.LocationUiState
import com.karandaev.geo_inspect.feature.presentation.create.components.CreateInspectionReportContentField
import com.karandaev.geo_inspect.feature.presentation.create.components.CreateInspectionReportDetectionSection
import com.karandaev.geo_inspect.feature.presentation.create.components.CreateInspectionReportLocationSection
import com.karandaev.geo_inspect.feature.presentation.create.components.CreateInspectionReportSaveButton
import com.karandaev.geo_inspect.feature.presentation.create.components.CreateInspectionReportTitleField
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.DetectionPreviewDialogOrientation

private val CreateInspectionReportSinglePaneHorizontalPadding = 16.dp
private val CreateInspectionReportSinglePaneTopPadding = 8.dp
private val CreateInspectionReportSinglePaneBottomPadding = 24.dp
private val CreateInspectionReportSinglePaneSpacing = 20.dp

@Composable
internal fun CreateInspectionReportSinglePaneContent(
  title: String,
  content: String,
  selectedPoint: MapPoint?,
  locationState: LocationUiState,
  mapFocusRequest: Int,
  detectionImagePath: String?,
  detectResponse: DetectResponse?,
  isDetectionPreviewDialogVisible: Boolean,
  canSave: Boolean,
  onTitleChange: (String) -> Unit,
  onContentChange: (String) -> Unit,
  onMapFocusClick: () -> Unit,
  onUseCurrentLocationClick: () -> Unit,
  onSelectedPointChange: (MapPoint) -> Unit,
  onLatitudeChange: (Double, Double) -> Unit,
  onLongitudeChange: (Double, Double) -> Unit,
  onDetectionPreviewClick: () -> Unit,
  onDismissDetectionPreviewDialogRequest: () -> Unit,
  onDetectClick: () -> Unit,
  onClearDetectionClick: () -> Unit,
  onSaveClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(
        start = CreateInspectionReportSinglePaneHorizontalPadding,
        top = CreateInspectionReportSinglePaneTopPadding,
        end = CreateInspectionReportSinglePaneHorizontalPadding,
        bottom = CreateInspectionReportSinglePaneBottomPadding
      ),
    verticalArrangement = Arrangement.spacedBy(CreateInspectionReportSinglePaneSpacing)
  ) {
    CreateInspectionReportTitleField(
      value = title,
      onValueChange = onTitleChange
    )

    CreateInspectionReportContentField(
      value = content,
      onValueChange = onContentChange
    )

    CreateInspectionReportDetectionSection(
      detectionImagePath = detectionImagePath,
      detectResponse = detectResponse,
      isDetectionPreviewDialogVisible = isDetectionPreviewDialogVisible,
      detectionPreviewDialogOrientation = DetectionPreviewDialogOrientation.Portrait,
      onDetectionPreviewClick = onDetectionPreviewClick,
      onDismissDetectionPreviewDialogRequest = onDismissDetectionPreviewDialogRequest,
      onDetectClick = onDetectClick,
      onClearDetectionClick = onClearDetectionClick
    )

    CreateInspectionReportLocationSection(
      selectedPoint = selectedPoint,
      locationState = locationState,
      focusRequest = mapFocusRequest,
      onMapFocusClick = onMapFocusClick,
      onUseCurrentLocationClick = onUseCurrentLocationClick,
      onSelectedPointChange = onSelectedPointChange,
      onLatitudeChange = onLatitudeChange,
      onLongitudeChange = onLongitudeChange
    )

    CreateInspectionReportSaveButton(
      enabled = canSave,
      onClick = onSaveClick
    )
  }
}