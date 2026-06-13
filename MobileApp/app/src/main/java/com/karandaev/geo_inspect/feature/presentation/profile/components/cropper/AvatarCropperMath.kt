package com.karandaev.geo_inspect.feature.presentation.profile.components.cropper

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.karandaev.geo_inspect.core.image.crop.NormalizedCropBox
import kotlin.math.roundToInt

internal val AvatarImageSrcOffset = IntOffset.Zero

/**
 * Destination placement for drawing avatar image inside cropper viewport.
 */
internal data class AvatarImagePlacement(
  val offset: IntOffset,
  val size: IntSize
)

/**
 * Calculates image placement for current cropper transform.
 */
internal fun calculateAvatarImagePlacement(
  imageWidth: Int,
  imageHeight: Int,
  viewportSize: Float,
  scale: Float,
  offset: Offset
): AvatarImagePlacement {
  val drawScale = avatarCropperDrawScale(
    imageWidth = imageWidth,
    imageHeight = imageHeight,
    viewportSize = viewportSize,
    scale = scale
  )

  val drawWidth = imageWidth * drawScale
  val drawHeight = imageHeight * drawScale

  return AvatarImagePlacement(
    offset = IntOffset(
      x = (viewportSize / 2f + offset.x - drawWidth / 2f).roundToInt(),
      y = (viewportSize / 2f + offset.y - drawHeight / 2f).roundToInt()
    ),
    size = IntSize(
      width = drawWidth.roundToInt(),
      height = drawHeight.roundToInt()
    )
  )
}

/**
 * Calculates base scale that makes image fully cover circular viewport.
 */
internal fun avatarCropperBaseScale(
  imageWidth: Int,
  imageHeight: Int,
  viewportSize: Float
): Float {
  return maxOf(
    viewportSize / imageWidth.toFloat(),
    viewportSize / imageHeight.toFloat()
  )
}

/**
 * Calculates final draw scale using base cover scale and user zoom.
 */
internal fun avatarCropperDrawScale(
  imageWidth: Int,
  imageHeight: Int,
  viewportSize: Float,
  scale: Float
): Float {
  return avatarCropperBaseScale(
    imageWidth = imageWidth,
    imageHeight = imageHeight,
    viewportSize = viewportSize
  ) * scale
}

/**
 * Clamps pan offset so circular crop viewport is always covered by the image.
 */
internal fun clampAvatarCropperOffset(
  imageWidth: Int,
  imageHeight: Int,
  viewportSize: Float,
  scale: Float,
  offset: Offset
): Offset {
  val drawScale = avatarCropperDrawScale(
    imageWidth = imageWidth,
    imageHeight = imageHeight,
    viewportSize = viewportSize,
    scale = scale
  )

  val drawWidth = imageWidth * drawScale
  val drawHeight = imageHeight * drawScale

  val maxOffsetX = ((drawWidth - viewportSize) / 2f).coerceAtLeast(0f)
  val maxOffsetY = ((drawHeight - viewportSize) / 2f).coerceAtLeast(0f)

  return Offset(
    x = offset.x.coerceIn(-maxOffsetX, maxOffsetX),
    y = offset.y.coerceIn(-maxOffsetY, maxOffsetY)
  )
}

/**
 * Calculates normalized crop box for current transform.
 */
internal fun calculateAvatarCropBox(
  imageWidth: Int,
  imageHeight: Int,
  viewportSize: Float,
  scale: Float,
  offset: Offset
): NormalizedCropBox {
  val drawScale = avatarCropperDrawScale(
    imageWidth = imageWidth,
    imageHeight = imageHeight,
    viewportSize = viewportSize,
    scale = scale
  )

  val drawWidth = imageWidth * drawScale
  val drawHeight = imageHeight * drawScale

  val imageLeft = viewportSize / 2f + offset.x - drawWidth / 2f
  val imageTop = viewportSize / 2f + offset.y - drawHeight / 2f

  return NormalizedCropBox(
    xMin = ((0f - imageLeft) / drawScale / imageWidth.toFloat())
      .coerceIn(0f, 1f)
      .toDouble(),
    yMin = ((0f - imageTop) / drawScale / imageHeight.toFloat())
      .coerceIn(0f, 1f)
      .toDouble(),
    xMax = ((viewportSize - imageLeft) / drawScale / imageWidth.toFloat())
      .coerceIn(0f, 1f)
      .toDouble(),
    yMax = ((viewportSize - imageTop) / drawScale / imageHeight.toFloat())
      .coerceIn(0f, 1f)
      .toDouble()
  )
}

/**
 * Converts image bitmap dimensions to [IntSize].
 */
internal fun ImageBitmap.toIntSize(): IntSize {
  return IntSize(
    width = width,
    height = height
  )
}