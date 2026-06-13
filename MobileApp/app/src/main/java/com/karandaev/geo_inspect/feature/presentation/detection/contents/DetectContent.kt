package com.karandaev.geo_inspect.feature.presentation.detection.contents

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.karandaev.geo_inspect.app.adaptive_layout.AdaptiveLayoutType
import com.karandaev.geo_inspect.app.adaptive_layout.rememberAdaptiveLayoutType
import com.karandaev.geo_inspect.core.image.crop.NormalizedCropBox
import com.karandaev.geo_inspect.core.presentation.detection.DetectUiState

/**
 * Adaptive detection content.
 *
 * Chooses single-pane or two-pane layout depending on available size.
 *
 * @param state Current detection UI state.
 * @param onBackClick Called when user clicks back button.
 * @param onTakePhotoClick Called when user wants to take a photo.
 * @param onChooseImageClick Called when user wants to choose image from gallery.
 * @param onClearImageClick Called when user clears selected image.
 * @param onCropBoxChange Called when crop box changes.
 * @param onResetCropClick Called when user resets crop.
 * @param onDetectClick Called when user starts inference.
 * @param onClearMessageClick Called when user dismisses message.
 * @param modifier Modifier applied to root.
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
internal fun DetectContent(
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
  BoxWithConstraints(
    modifier = modifier.fillMaxSize()
  ) {
    val layoutType = rememberAdaptiveLayoutType(
      maxWidth = maxWidth,
      maxHeight = maxHeight
    )

    when (layoutType) {
      AdaptiveLayoutType.SinglePane -> {
        DetectSinglePaneContent(
          state = state,
          onBackClick = onBackClick,
          onTakePhotoClick = onTakePhotoClick,
          onChooseImageClick = onChooseImageClick,
          onClearImageClick = onClearImageClick,
          onCropBoxChange = onCropBoxChange,
          onResetCropClick = onResetCropClick,
          onDetectClick = onDetectClick,
          onClearMessageClick = onClearMessageClick,
          modifier = Modifier.fillMaxSize()
        )
      }

      AdaptiveLayoutType.TwoPane -> {
        DetectTwoPaneContent(
          state = state,
          onBackClick = onBackClick,
          onTakePhotoClick = onTakePhotoClick,
          onChooseImageClick = onChooseImageClick,
          onClearImageClick = onClearImageClick,
          onCropBoxChange = onCropBoxChange,
          onResetCropClick = onResetCropClick,
          onDetectClick = onDetectClick,
          onClearMessageClick = onClearMessageClick,
          modifier = Modifier.fillMaxSize()
        )
      }
    }
  }
}