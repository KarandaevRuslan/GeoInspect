package com.karandaev.geo_inspect.feature.presentation.detection.contents

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
import com.karandaev.geo_inspect.core.image.crop.NormalizedCropBox
import com.karandaev.geo_inspect.core.presentation.detection.DetectStep
import com.karandaev.geo_inspect.core.presentation.detection.DetectUiState
import com.karandaev.geo_inspect.core.ui.components.notification.MiniUiMessageCard
import com.karandaev.geo_inspect.feature.presentation.detection.components.crop.DetectCropActionsCard
import com.karandaev.geo_inspect.feature.presentation.detection.components.crop.DetectCropCard
import com.karandaev.geo_inspect.feature.presentation.detection.components.feedback.DetectProgressCard
import com.karandaev.geo_inspect.feature.presentation.detection.components.navigation.DetectHeader
import com.karandaev.geo_inspect.feature.presentation.detection.components.source.DetectActionCard

private val DetectTwoPaneHorizontalPadding = 16.dp
private val DetectTwoPaneColumnTopPadding = 16.dp
private val DetectTwoPaneColumnBottomPadding = 16.dp
private val DetectTwoPaneHorizontalSpacing = 16.dp
private val DetectTwoPaneVerticalSpacing = 12.dp
private const val DetectTwoPaneColumnWeight = 1f

/**
 * Two-pane detection layout.
 *
 * Used on wider screens where source actions and image workflow can be shown side by side.
 */
@Composable
internal fun DetectTwoPaneContent(
  state: DetectUiState,
  onBackClick: () -> Unit,
  onTakePhotoClick: () -> Unit,
  onChooseImageClick: () -> Unit,
  onClearImageClick: () -> Unit,
  onCropBoxChange: (NormalizedCropBox?) -> Unit,
  onResetCropClick: () -> Unit,
  onDetectClick: () -> Unit,
  onClearMessageClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.padding(
      horizontal = DetectTwoPaneHorizontalPadding
    ),
    horizontalArrangement = Arrangement.spacedBy(DetectTwoPaneHorizontalSpacing)
  ) {
    DetectTwoPaneActionColumn(
      state = state,
      onBackClick = onBackClick,
      onTakePhotoClick = onTakePhotoClick,
      onChooseImageClick = onChooseImageClick,
      onClearImageClick = onClearImageClick,
      onResetCropClick = onResetCropClick,
      onDetectClick = onDetectClick,
      onClearMessageClick = onClearMessageClick,
      modifier = Modifier
        .weight(DetectTwoPaneColumnWeight)
        .fillMaxHeight()
    )

    DetectTwoPaneWorkflowColumn(
      state = state,
      onClearImageClick = onClearImageClick,
      onCropBoxChange = onCropBoxChange,
      onResetCropClick = onResetCropClick,
      onDetectClick = onDetectClick,
      modifier = Modifier
        .weight(DetectTwoPaneColumnWeight)
        .fillMaxHeight()
    )
  }
}

/**
 * Left column with navigation, source actions, crop actions, message and loading state.
 */
@Composable
private fun DetectTwoPaneActionColumn(
  state: DetectUiState,
  onBackClick: () -> Unit,
  onTakePhotoClick: () -> Unit,
  onChooseImageClick: () -> Unit,
  onClearImageClick: () -> Unit,
  onResetCropClick: () -> Unit,
  onDetectClick: () -> Unit,
  onClearMessageClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .verticalScroll(rememberScrollState())
      .padding(
        top = DetectTwoPaneColumnTopPadding,
        bottom = DetectTwoPaneColumnBottomPadding
      ),
    verticalArrangement = Arrangement.spacedBy(DetectTwoPaneVerticalSpacing)
  ) {
    DetectHeader(
      onBackClick = onBackClick
    )

    state.message?.let { message ->
      MiniUiMessageCard(
        message = message,
        onClearMessageClick = onClearMessageClick
      )
    }

    DetectActionCard(
      enabled = !state.isLoading,
      onTakePhotoClick = onTakePhotoClick,
      onChooseImageClick = onChooseImageClick
    )

    if (state.step == DetectStep.CropImage) {
      DetectCropActionsCard(
        imageSource = state.imageSource,
        enabled = !state.isLoading,
        canDetect = state.canDetect,
        onResetCropClick = onResetCropClick,
        onClearImageClick = onClearImageClick,
        onDetectClick = onDetectClick
      )
    }

    if (state.isLoading) {
      DetectProgressCard()
    }
  }
}

/**
 * Right column with current image workflow step.
 */
@Composable
private fun DetectTwoPaneWorkflowColumn(
  state: DetectUiState,
  onClearImageClick: () -> Unit,
  onCropBoxChange: (NormalizedCropBox?) -> Unit,
  onResetCropClick: () -> Unit,
  onDetectClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .verticalScroll(rememberScrollState())
      .padding(
        top = DetectTwoPaneColumnTopPadding,
        bottom = DetectTwoPaneColumnBottomPadding
      ),
    verticalArrangement = Arrangement.spacedBy(DetectTwoPaneVerticalSpacing)
  ) {
    when (state.step) {
      DetectStep.ChooseSource -> Unit

      DetectStep.CropImage -> {
        DetectCropCard(
          imageSource = state.imageSource,
          enabled = !state.isLoading,
          canDetect = state.canDetect,
          onCropBoxChange = onCropBoxChange,
          onResetCropClick = onResetCropClick,
          onClearImageClick = onClearImageClick,
          onDetectClick = onDetectClick,
          showActions = false
        )
      }
    }
  }
}