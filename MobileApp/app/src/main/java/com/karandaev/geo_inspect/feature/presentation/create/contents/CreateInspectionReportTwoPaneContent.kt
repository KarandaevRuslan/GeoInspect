package com.karandaev.geo_inspect.feature.presentation.create.contents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.core.domain.model.detection.DetectResponse
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import com.karandaev.geo_inspect.core.presentation.location.LocationUiState
import com.karandaev.geo_inspect.feature.presentation.create.components.CreateInspectionReportContentField
import com.karandaev.geo_inspect.feature.presentation.create.components.CreateInspectionReportDetectionSection
import com.karandaev.geo_inspect.feature.presentation.create.components.CreateInspectionReportHeader
import com.karandaev.geo_inspect.feature.presentation.create.components.CreateInspectionReportLocationSection
import com.karandaev.geo_inspect.feature.presentation.create.components.CreateInspectionReportSaveButton
import com.karandaev.geo_inspect.feature.presentation.create.components.CreateInspectionReportTitleField
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.DetectionPreviewDialogOrientation

private val CreateInspectionReportTwoPaneHorizontalPadding = 16.dp
private val CreateInspectionReportTwoPaneColumnTopPadding = 16.dp
private val CreateInspectionReportTwoPaneColumnBottomPadding = 24.dp
private val CreateInspectionReportTwoPaneHorizontalSpacing = 16.dp
private val CreateInspectionReportTwoPaneVerticalSpacing = 20.dp

@Composable
internal fun CreateInspectionReportTwoPaneContent(
  title: String,
  content: String,
  selectedPoint: MapPoint?,
  locationState: LocationUiState,
  mapFocusRequest: Int,
  detectionImagePath: String?,
  detectResponse: DetectResponse?,
  isDetectionPreviewDialogVisible: Boolean,
  canSave: Boolean,
  isEditMode: Boolean,
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
  onBackClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.padding(
      horizontal = CreateInspectionReportTwoPaneHorizontalPadding
    ),
    horizontalArrangement = Arrangement.spacedBy(CreateInspectionReportTwoPaneHorizontalSpacing)
  ) {
    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxHeight()
        .verticalScroll(rememberScrollState())
        .padding(
          top = CreateInspectionReportTwoPaneColumnTopPadding,
          bottom = CreateInspectionReportTwoPaneColumnBottomPadding
        ),
      verticalArrangement = Arrangement.spacedBy(CreateInspectionReportTwoPaneVerticalSpacing)
    ) {
      CreateInspectionReportHeader(
        title = stringResource(
          id = if (isEditMode) {
            R.string.destination_edit_inspection_report
          } else {
            R.string.destination_create_inspection_report
          }
        ),
        onBackClick = onBackClick
      )

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
        detectionPreviewDialogOrientation = DetectionPreviewDialogOrientation.Landscape,
        onDetectionPreviewClick = onDetectionPreviewClick,
        onDismissDetectionPreviewDialogRequest = onDismissDetectionPreviewDialogRequest,
        onDetectClick = onDetectClick,
        onClearDetectionClick = onClearDetectionClick
      )

      CreateInspectionReportSaveButton(
        enabled = canSave,
        onClick = onSaveClick
      )
    }

    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxHeight()
        .verticalScroll(rememberScrollState())
        .padding(
          top = CreateInspectionReportTwoPaneColumnTopPadding,
          bottom = CreateInspectionReportTwoPaneColumnBottomPadding
        ),
      verticalArrangement = Arrangement.spacedBy(CreateInspectionReportTwoPaneVerticalSpacing)
    ) {
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
    }
  }
}