package com.karandaev.geo_inspect.feature.presentation.detection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.karandaev.geo_inspect.core.presentation.detection.DetectViewModel
import com.karandaev.geo_inspect.feature.presentation.detection.launcher.rememberDetectLaunchers

/**
 * Logical wrapper for detection screen.
 *
 * Owns Android activity result launchers and detection completion navigation.
 * Detection state is owned by the externally provided [DetectViewModel].
 */
@Composable
fun DetectRoute(
  detectViewModel: DetectViewModel,
  onBackClick: () -> Unit,
  onDetectionComplete: () -> Unit
) {
  val state by detectViewModel.state.collectAsStateWithLifecycle()
  val completedResult by detectViewModel.completedResult.collectAsStateWithLifecycle()

  DetectCompletionNavigationEffect(
    completedResult = completedResult,
    onDetectionComplete = onDetectionComplete
  )

  val launchers = rememberDetectLaunchers(
    onGalleryImageSelected = detectViewModel::setGalleryImage,
    onCapturedPhotoSelected = detectViewModel::setCapturedPhoto,
    onCameraCaptureCancelled = detectViewModel::onCameraCaptureCancelled,
    onCameraPermissionDenied = detectViewModel::onCameraPermissionDenied
  )

  DetectScreen(
    state = state,
    onBackClick = onBackClick,
    onTakePhotoClick = launchers::launchCameraWithPermission,
    onChooseImageClick = launchers::launchGallery,
    onClearImageClick = detectViewModel::clearSelectedImage,
    onCropBoxChange = detectViewModel::setCropBox,
    onResetCropClick = detectViewModel::resetCrop,
    onDetectClick = detectViewModel::detectSelectedImage,
    onClearMessageClick = detectViewModel::clearMessage
  )
}