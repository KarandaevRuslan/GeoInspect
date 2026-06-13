package com.karandaev.geo_inspect.feature.presentation.detection

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.karandaev.geo_inspect.core.image.crop.NormalizedCropBox
import com.karandaev.geo_inspect.core.presentation.detection.DetectUiState
import com.karandaev.geo_inspect.feature.presentation.detection.contents.DetectContent

/**
 * Detection screen UI.
 *
 * This composable owns no ViewModel and performs no navigation directly.
 *
 * @param state Current detection UI state.
 * @param onBackClick Called when the user clicks back button.
 * @param onTakePhotoClick Called when user wants to take a photo.
 * @param onChooseImageClick Called when user wants to choose image from gallery.
 * @param onClearImageClick Called when user clears selected image.
 * @param onCropBoxChange Called when crop box changes.
 * @param onResetCropClick Called when user resets crop.
 * @param onDetectClick Called when user starts inference.
 * @param onClearMessageClick Called when user dismisses message.
 * @param modifier Modifier applied to screen content.
 */
@Composable
fun DetectScreen(
  state: DetectUiState,
  onBackClick: () -> Unit,
  onTakePhotoClick: () -> Unit,
  onChooseImageClick: () -> Unit,
  onClearImageClick: () -> Unit,
  onCropBoxChange: (NormalizedCropBox?) -> Unit,
  onResetCropClick: () -> Unit,
  onDetectClick: (Context) -> Unit,
  onClearMessageClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  DetectContent(
    state = state,
    onBackClick = onBackClick,
    onTakePhotoClick = onTakePhotoClick,
    onChooseImageClick = onChooseImageClick,
    onClearImageClick = onClearImageClick,
    onCropBoxChange = onCropBoxChange,
    onResetCropClick = onResetCropClick,
    onDetectClick = {
      onDetectClick(context)
    },
    onClearMessageClick = onClearMessageClick,
    modifier = modifier
  )
}