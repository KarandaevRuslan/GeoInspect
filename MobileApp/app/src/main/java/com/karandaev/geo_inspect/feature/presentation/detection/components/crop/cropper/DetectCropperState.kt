package com.karandaev.geo_inspect.feature.presentation.detection.components.crop.cropper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.karandaev.geo_inspect.core.image.crop.NormalizedCropBox

/**
 * Remembers cropper state for a concrete image.
 *
 * @param imagePath Original image path used as state key.
 */
@Composable
internal fun rememberDetectCropperState(
  imagePath: String
): DetectCropperState {
  return remember(imagePath) {
    DetectCropperState()
  }
}

/**
 * Mutable cropper transform state.
 */
@Stable
internal class DetectCropperState {

  var scale by mutableFloatStateOf(DetectCropperMinScale)
    private set

  var offset by mutableStateOf(Offset.Zero)
    private set

  /**
   * Resets cropper transform to initial centered state.
   */
  fun reset() {
    scale = DetectCropperMinScale
    offset = Offset.Zero
  }

  /**
   * Restores cropper transform from already selected crop box.
   *
   * Used after configuration changes, because [NormalizedCropBox] is stored in image source,
   * while visual pan and zoom state is local to the cropper.
   */
  fun restoreFromCropBox(
    cropBox: NormalizedCropBox,
    imageSize: IntSize,
    viewportSize: IntSize
  ) {
    if (!viewportSize.isValid()) {
      return
    }

    val cropWidth = (cropBox.xMax - cropBox.xMin)
      .toFloat()
      .coerceAtLeast(DetectCropperMinCropSize)

    val cropHeight = (cropBox.yMax - cropBox.yMin)
      .toFloat()
      .coerceAtLeast(DetectCropperMinCropSize)

    val basePlacement = calculateDetectImagePlacement(
      imageSize = imageSize,
      viewportSize = viewportSize,
      scale = DetectCropperMinScale,
      offset = Offset.Zero
    )

    val restoredScaleX = viewportSize.width / (basePlacement.size.width * cropWidth)
    val restoredScaleY = viewportSize.height / (basePlacement.size.height * cropHeight)

    val restoredScale = maxOf(
      restoredScaleX,
      restoredScaleY,
      DetectCropperMinScale
    ).coerceAtMost(DetectCropperMaxScale)

    val restoredPlacement = calculateDetectImagePlacement(
      imageSize = imageSize,
      viewportSize = viewportSize,
      scale = restoredScale,
      offset = Offset.Zero
    )

    val imageLeft = -cropBox.xMin.toFloat() * restoredPlacement.size.width
    val imageTop = -cropBox.yMin.toFloat() * restoredPlacement.size.height

    scale = restoredScale
    offset = clampDetectCropperOffset(
      imageSize = imageSize,
      viewportSize = viewportSize,
      scale = restoredScale,
      offset = Offset(
        x = imageLeft - restoredPlacement.offset.x,
        y = imageTop - restoredPlacement.offset.y
      )
    )
  }

  /**
   * Applies drag and zoom gesture to cropper transform.
   *
   * @param imageSize Original image size.
   * @param viewportSize Cropper viewport size.
   * @param pan Drag delta.
   * @param zoom Zoom multiplier.
   */
  fun applyGesture(
    imageSize: IntSize,
    viewportSize: IntSize,
    pan: Offset,
    zoom: Float
  ) {
    if (!viewportSize.isValid()) {
      return
    }

    val nextScale = (scale * zoom).coerceIn(
      minimumValue = DetectCropperMinScale,
      maximumValue = DetectCropperMaxScale
    )

    scale = nextScale

    offset = clampDetectCropperOffset(
      imageSize = imageSize,
      viewportSize = viewportSize,
      scale = nextScale,
      offset = offset + pan
    )
  }

  private companion object {
    const val DetectCropperMinCropSize = 0.001f
  }
}