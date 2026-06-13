package com.karandaev.geo_inspect.feature.presentation.create

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.karandaev.geo_inspect.app.adaptive_layout.AdaptiveLayoutType
import com.karandaev.geo_inspect.app.adaptive_layout.rememberAdaptiveLayoutType
import com.karandaev.geo_inspect.core.domain.model.detection.DetectResponse
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import com.karandaev.geo_inspect.core.presentation.location.LocationUiState
import com.karandaev.geo_inspect.feature.presentation.create.contents.CreateInspectionReportSinglePaneContent
import com.karandaev.geo_inspect.feature.presentation.create.contents.CreateInspectionReportTwoPaneContent

/**
 * Stateless create/edit inspection report screen UI.
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CreateInspectionReportScreen(
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
  BoxWithConstraints(
    modifier = modifier.fillMaxSize()
  ) {
    val layoutType = rememberAdaptiveLayoutType(
      maxWidth = maxWidth,
      maxHeight = maxHeight
    )

    when (layoutType) {
      AdaptiveLayoutType.SinglePane -> {
        CreateInspectionReportSinglePaneContent(
          title = title,
          content = content,
          selectedPoint = selectedPoint,
          locationState = locationState,
          mapFocusRequest = mapFocusRequest,
          detectionImagePath = detectionImagePath,
          detectResponse = detectResponse,
          isDetectionPreviewDialogVisible = isDetectionPreviewDialogVisible,
          canSave = canSave,
          onTitleChange = onTitleChange,
          onContentChange = onContentChange,
          onMapFocusClick = onMapFocusClick,
          onUseCurrentLocationClick = onUseCurrentLocationClick,
          onSelectedPointChange = onSelectedPointChange,
          onLatitudeChange = onLatitudeChange,
          onLongitudeChange = onLongitudeChange,
          onDetectionPreviewClick = onDetectionPreviewClick,
          onDismissDetectionPreviewDialogRequest = onDismissDetectionPreviewDialogRequest,
          onDetectClick = onDetectClick,
          onClearDetectionClick = onClearDetectionClick,
          onSaveClick = onSaveClick,
          modifier = Modifier.fillMaxSize()
        )
      }

      AdaptiveLayoutType.TwoPane -> {
        CreateInspectionReportTwoPaneContent(
          title = title,
          content = content,
          selectedPoint = selectedPoint,
          locationState = locationState,
          mapFocusRequest = mapFocusRequest,
          detectionImagePath = detectionImagePath,
          detectResponse = detectResponse,
          isDetectionPreviewDialogVisible = isDetectionPreviewDialogVisible,
          canSave = canSave,
          isEditMode = isEditMode,
          onTitleChange = onTitleChange,
          onContentChange = onContentChange,
          onMapFocusClick = onMapFocusClick,
          onUseCurrentLocationClick = onUseCurrentLocationClick,
          onSelectedPointChange = onSelectedPointChange,
          onLatitudeChange = onLatitudeChange,
          onLongitudeChange = onLongitudeChange,
          onDetectionPreviewClick = onDetectionPreviewClick,
          onDismissDetectionPreviewDialogRequest = onDismissDetectionPreviewDialogRequest,
          onDetectClick = onDetectClick,
          onClearDetectionClick = onClearDetectionClick,
          onSaveClick = onSaveClick,
          onBackClick = onBackClick,
          modifier = Modifier.fillMaxSize()
        )
      }
    }
  }
}