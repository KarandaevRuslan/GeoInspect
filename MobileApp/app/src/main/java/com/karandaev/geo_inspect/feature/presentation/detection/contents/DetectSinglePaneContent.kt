package com.karandaev.geo_inspect.feature.presentation.detection.contents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.core.image.crop.NormalizedCropBox
import com.karandaev.geo_inspect.core.presentation.detection.DetectStep
import com.karandaev.geo_inspect.core.presentation.detection.DetectUiState
import com.karandaev.geo_inspect.core.ui.components.notification.MiniUiMessageCard
import com.karandaev.geo_inspect.feature.presentation.detection.components.crop.DetectCropCard
import com.karandaev.geo_inspect.feature.presentation.detection.components.feedback.DetectProgressCard
import com.karandaev.geo_inspect.feature.presentation.detection.components.source.DetectActionCard

/**
 * Single-pane detection layout.
 *
 * Used on compact screens where all content is shown in one vertical list.
 */
@Composable
internal fun DetectSinglePaneContent(
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
  LazyColumn(
    modifier = modifier,
    contentPadding = PaddingValues(
      horizontal = 16.dp,
      vertical = 12.dp
    ),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    state.message?.let { message ->
      item {
        MiniUiMessageCard(
          message = message,
          onClearMessageClick = onClearMessageClick
        )
      }
    }

    if (state.isLoading) {
      item {
        DetectProgressCard()
      }
    }

    when (state.step) {
      DetectStep.ChooseSource -> {
        item {
          DetectActionCard(
            enabled = !state.isLoading,
            onTakePhotoClick = onTakePhotoClick,
            onChooseImageClick = onChooseImageClick
          )
        }
      }

      DetectStep.CropImage -> {
        item {
          DetectCropCard(
            imageSource = state.imageSource,
            enabled = !state.isLoading,
            canDetect = state.canDetect,
            onCropBoxChange = onCropBoxChange,
            onResetCropClick = onResetCropClick,
            onClearImageClick = onClearImageClick,
            onDetectClick = onDetectClick
          )
        }
      }
    }
  }
}