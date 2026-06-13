package com.karandaev.geo_inspect.feature.presentation.detection.components.crop.cropper

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.karandaev.geo_inspect.core.image.crop.NormalizedCropBox
import kotlin.math.roundToInt

/**
 * Image placement inside cropper viewport.
 *
 * @param offset Top-left image draw offset.
 * @param size Image draw size.
 */
internal data class DetectImagePlacement(
  val offset: IntOffset,
  val size: IntSize
)

/**
 * Returns whether size can be used for crop calculations.
 */
internal fun IntSize.isValid(): Boolean {
  return width > 0 && height > 0
}

/**
 * Calculates image draw placement for current transform.
 *
 * @param imageSize Original image size.
 * @param viewportSize Cropper viewport size.
 * @param scale User zoom scale.
 * @param offset User pan offset.
 */
internal fun calculateDetectImagePlacement(
  imageSize: IntSize,
  viewportSize: IntSize,
  scale: Float,
  offset: Offset
): DetectImagePlacement {
  val drawScale = detectCropperDrawScale(
    imageSize = imageSize,
    viewportSize = viewportSize,
    scale = scale
  )

  val drawWidth = imageSize.width * drawScale
  val drawHeight = imageSize.height * drawScale

  return DetectImagePlacement(
    offset = IntOffset(
      x = (viewportSize.width / 2f + offset.x - drawWidth / 2f).roundToInt(),
      y = (viewportSize.height / 2f + offset.y - drawHeight / 2f).roundToInt()
    ),
    size = IntSize(
      width = drawWidth.roundToInt(),
      height = drawHeight.roundToInt()
    )
  )
}

/**
 * Calculates normalized crop box for current transform.
 *
 * @param imageSize Original image size.
 * @param viewportSize Cropper viewport size.
 * @param scale User zoom scale.
 * @param offset User pan offset.
 */
internal fun calculateDetectCropBox(
  imageSize: IntSize,
  viewportSize: IntSize,
  scale: Float,
  offset: Offset
): NormalizedCropBox {
  val drawScale = detectCropperDrawScale(
    imageSize = imageSize,
    viewportSize = viewportSize,
    scale = scale
  )

  val drawWidth = imageSize.width * drawScale
  val drawHeight = imageSize.height * drawScale

  val imageLeft = viewportSize.width / 2f + offset.x - drawWidth / 2f
  val imageTop = viewportSize.height / 2f + offset.y - drawHeight / 2f

  return NormalizedCropBox(
    xMin = calculateNormalizedCropCoordinate(
      viewportCoordinate = 0f,
      imageStart = imageLeft,
      drawScale = drawScale,
      imageSide = imageSize.width
    ),
    yMin = calculateNormalizedCropCoordinate(
      viewportCoordinate = 0f,
      imageStart = imageTop,
      drawScale = drawScale,
      imageSide = imageSize.height
    ),
    xMax = calculateNormalizedCropCoordinate(
      viewportCoordinate = viewportSize.width.toFloat(),
      imageStart = imageLeft,
      drawScale = drawScale,
      imageSide = imageSize.width
    ),
    yMax = calculateNormalizedCropCoordinate(
      viewportCoordinate = viewportSize.height.toFloat(),
      imageStart = imageTop,
      drawScale = drawScale,
      imageSide = imageSize.height
    )
  )
}

/**
 * Clamps pan offset so the image keeps covering the whole cropper viewport.
 *
 * @param imageSize Original image size.
 * @param viewportSize Cropper viewport size.
 * @param scale User zoom scale.
 * @param offset Requested pan offset.
 */
internal fun clampDetectCropperOffset(
  imageSize: IntSize,
  viewportSize: IntSize,
  scale: Float,
  offset: Offset
): Offset {
  val drawScale = detectCropperDrawScale(
    imageSize = imageSize,
    viewportSize = viewportSize,
    scale = scale
  )

  val drawWidth = imageSize.width * drawScale
  val drawHeight = imageSize.height * drawScale

  val maxOffsetX = ((drawWidth - viewportSize.width) / 2f).coerceAtLeast(0f)
  val maxOffsetY = ((drawHeight - viewportSize.height) / 2f).coerceAtLeast(0f)

  return Offset(
    x = offset.x.coerceIn(
      minimumValue = -maxOffsetX,
      maximumValue = maxOffsetX
    ),
    y = offset.y.coerceIn(
      minimumValue = -maxOffsetY,
      maximumValue = maxOffsetY
    )
  )
}

/**
 * Calculates base scale that makes image cover the whole viewport.
 */
private fun detectCropperBaseScale(
  imageSize: IntSize,
  viewportSize: IntSize
): Float {
  return maxOf(
    viewportSize.width / imageSize.width.toFloat(),
    viewportSize.height / imageSize.height.toFloat()
  )
}

/**
 * Calculates final image draw scale.
 */
private fun detectCropperDrawScale(
  imageSize: IntSize,
  viewportSize: IntSize,
  scale: Float
): Float {
  return detectCropperBaseScale(
    imageSize = imageSize,
    viewportSize = viewportSize
  ) * scale
}

/**
 * Converts viewport coordinate to normalized image coordinate.
 */
private fun calculateNormalizedCropCoordinate(
  viewportCoordinate: Float,
  imageStart: Float,
  drawScale: Float,
  imageSide: Int
): Double {
  return ((viewportCoordinate - imageStart) / drawScale / imageSide.toFloat())
    .coerceIn(
      minimumValue = 0f,
      maximumValue = 1f
    )
    .toDouble()
}