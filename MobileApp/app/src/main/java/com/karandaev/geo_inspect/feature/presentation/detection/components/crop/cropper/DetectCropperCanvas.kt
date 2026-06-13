package com.karandaev.geo_inspect.feature.presentation.detection.components.crop.cropper

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlin.math.roundToInt

/**
 * Visual cropper surface with image, scrim and viewport border.
 *
 * @param imageBitmap Decoded image bitmap.
 * @param imageSize Original image size.
 * @param cropperState Current transform state.
 * @param enabled Whether gestures are enabled.
 * @param contentDescription Accessibility description for cropper canvas.
 * @param onViewportSizeChange Called when viewport size changes.
 * @param modifier Modifier applied to the surface.
 */
@Composable
internal fun DetectCropperCanvas(
  imageBitmap: ImageBitmap,
  imageSize: IntSize,
  cropperState: DetectCropperState,
  enabled: Boolean,
  contentDescription: String,
  onViewportSizeChange: (IntSize) -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier,
    shape = DetectCropperShape,
    color = MaterialTheme.colorScheme.surfaceContainerHighest,
    tonalElevation = DetectCropperTonalElevation,
    shadowElevation = DetectCropperShadowElevation,
    border = BorderStroke(
      width = DetectCropperBorderWidth,
      color = MaterialTheme.colorScheme.primary
    )
  ) {
    Box {
      Canvas(
        modifier = Modifier
          .matchParentSize()
          .onSizeChanged(onViewportSizeChange)
          .semantics {
            this.contentDescription = contentDescription
          }
          .pointerInput(
            enabled,
            imageBitmap,
            imageSize
          ) {
            if (!enabled) return@pointerInput

            detectTransformGestures { _, pan, zoom, _ ->
              cropperState.applyGesture(
                imageSize = imageSize,
                viewportSize = size,
                pan = pan,
                zoom = zoom
              )
            }
          }
      ) {
        drawDetectCropperContent(
          imageBitmap = imageBitmap,
          imageSize = imageSize,
          cropperState = cropperState
        )
      }
    }
  }
}

/**
 * Draws image and scrim.
 */
private fun DrawScope.drawDetectCropperContent(
  imageBitmap: ImageBitmap,
  imageSize: IntSize,
  cropperState: DetectCropperState
) {
  val viewportSize = IntSize(
    width = size.width.roundToInt(),
    height = size.height.roundToInt()
  )

  val placement = calculateDetectImagePlacement(
    imageSize = imageSize,
    viewportSize = viewportSize,
    scale = cropperState.scale,
    offset = cropperState.offset
  )

  drawImage(
    image = imageBitmap,
    srcOffset = IntOffset.Zero,
    srcSize = imageSize,
    dstOffset = placement.offset,
    dstSize = placement.size
  )

  drawRect(
    color = Color.Black.copy(alpha = DetectCropperScrimAlpha),
    size = Size(
      width = size.width,
      height = size.height
    )
  )
}