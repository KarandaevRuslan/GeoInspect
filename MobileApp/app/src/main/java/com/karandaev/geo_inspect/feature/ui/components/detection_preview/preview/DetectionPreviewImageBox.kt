package com.karandaev.geo_inspect.feature.ui.components.detection_preview.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.overlay.DetectionOverlay
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.state.DetectionPreviewState

/**
 * Image preview area with optional placeholder and detection overlay.
 *
 * @param state Detection preview state.
 * @param previewHeight Height of image preview area.
 * @param modifier Modifier applied to the root box.
 */
@Composable
internal fun DetectionPreviewImageBox(
  state: DetectionPreviewState,
  previewHeight: Dp,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(previewHeight),
    contentAlignment = Alignment.Center
  ) {
    var containerSize by remember {
      mutableStateOf(IntSize.Zero)
    }

    Box(
      modifier = Modifier
        .fillMaxSize()
        .onSizeChanged { size ->
          containerSize = size
        }
    ) {
      DetectionPreviewImage(
        imageFile = state.imageFile,
        imageAspectRatio = state.imageAspectRatio,
        containerSize = containerSize,
        modifier = Modifier.fillMaxSize()
      )

      if (state.hasDetections) {
        DetectionOverlay(
          containerSize = containerSize,
          imageAspectRatio = state.imageAspectRatio,
          detections = state.detections,
          modifier = Modifier.fillMaxSize()
        )
      }
    }
  }
}