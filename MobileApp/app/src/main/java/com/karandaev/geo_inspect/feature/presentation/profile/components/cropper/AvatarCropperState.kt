package com.karandaev.geo_inspect.feature.presentation.profile.components.cropper

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import com.karandaev.geo_inspect.core.image.crop.NormalizedCropBox

/**
 * Mutable cropper transform state.
 *
 * Stores user-controlled zoom and pan and exposes helper methods for gesture handling
 * and crop box calculation.
 */
internal class AvatarCropperState {

  var scale by mutableFloatStateOf(AvatarCropperMinScale)
    private set

  var offset by mutableStateOf(Offset.Zero)
    private set

  /**
   * Resets cropper transform to initial centered state.
   */
  fun reset() {
    scale = AvatarCropperMinScale
    offset = Offset.Zero
  }

  /**
   * Applies drag and pinch gesture to current cropper state.
   */
  fun applyGesture(
    imageWidth: Int,
    imageHeight: Int,
    viewportSize: Float,
    pan: Offset,
    zoom: Float
  ) {
    val nextScale = (scale * zoom).coerceIn(
      minimumValue = AvatarCropperMinScale,
      maximumValue = AvatarCropperMaxScale
    )

    scale = nextScale
    offset = clampAvatarCropperOffset(
      imageWidth = imageWidth,
      imageHeight = imageHeight,
      viewportSize = viewportSize,
      scale = nextScale,
      offset = offset + pan
    )
  }

  /**
   * Calculates normalized crop box for current transform.
   */
  fun calculateCropBox(
    imageWidth: Int,
    imageHeight: Int,
    viewportSize: Float
  ): NormalizedCropBox {
    return calculateAvatarCropBox(
      imageWidth = imageWidth,
      imageHeight = imageHeight,
      viewportSize = viewportSize,
      scale = scale,
      offset = offset
    )
  }

  /**
   * Restores transform state from previously saved crop box.
   */
  fun restoreFromCropBox(
    cropBox: NormalizedCropBox?,
    imageWidth: Int,
    imageHeight: Int,
    viewportSize: Float
  ) {
    if (cropBox == null) {
      reset()
      return
    }

    val cropWidth = (cropBox.xMax - cropBox.xMin)
      .toFloat()
      .coerceAtLeast(0.001f)

    val cropHeight = (cropBox.yMax - cropBox.yMin)
      .toFloat()
      .coerceAtLeast(0.001f)

    val basePlacement = calculateAvatarImagePlacement(
      imageWidth = imageWidth,
      imageHeight = imageHeight,
      viewportSize = viewportSize,
      scale = 1f,
      offset = AvatarCropperInitialOffset
    )

    val restoredScaleX = viewportSize / (basePlacement.size.width * cropWidth)
    val restoredScaleY = viewportSize / (basePlacement.size.height * cropHeight)

    scale = ((restoredScaleX + restoredScaleY) / 2f)
      .coerceAtLeast(1f)

    val centeredPlacement = calculateAvatarImagePlacement(
      imageWidth = imageWidth,
      imageHeight = imageHeight,
      viewportSize = viewportSize,
      scale = scale,
      offset = AvatarCropperInitialOffset
    )

    val imageLeft = -cropBox.xMin.toFloat() * centeredPlacement.size.width
    val imageTop = -cropBox.yMin.toFloat() * centeredPlacement.size.height

    offset = AvatarCropperInitialOffset.copy(
      x = imageLeft - centeredPlacement.offset.x,
      y = imageTop - centeredPlacement.offset.y
    )
  }
}