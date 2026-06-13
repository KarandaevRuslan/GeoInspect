package com.karandaev.geo_inspect.feature.ui.components.detection_preview.geometry

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntSize
import com.karandaev.geo_inspect.core.domain.model.detection.NormalizedBBox

/**
 * Calculates displayed image rect for ContentScale.Fit-like behavior.
 *
 * @param containerSize Preview container size.
 * @param imageAspectRatio Image aspect ratio.
 */
internal fun calculateFittedImageRect(
  containerSize: IntSize,
  imageAspectRatio: Float
): Rect {
  val containerWidth = containerSize.width.toFloat()
  val containerHeight = containerSize.height.toFloat()

  if (
    containerWidth <= 0f ||
    containerHeight <= 0f ||
    imageAspectRatio <= 0f
  ) {
    return Rect.Zero
  }

  val containerAspectRatio = containerWidth / containerHeight

  val fittedSize = if (containerAspectRatio > imageAspectRatio) {
    val fittedHeight = containerHeight

    Size(
      width = fittedHeight * imageAspectRatio,
      height = fittedHeight
    )
  } else {
    val fittedWidth = containerWidth

    Size(
      width = fittedWidth,
      height = fittedWidth / imageAspectRatio
    )
  }

  val left = (containerWidth - fittedSize.width) / 2f
  val top = (containerHeight - fittedSize.height) / 2f

  return Rect(
    left = left,
    top = top,
    right = left + fittedSize.width,
    bottom = top + fittedSize.height
  )
}

/**
 * Converts normalized bbox to canvas rect inside fitted image area.
 *
 * @param imageRect Displayed image rect.
 */
internal fun NormalizedBBox.toCanvasRect(
  imageRect: Rect
): Rect {
  val leftFraction = minOf(xMin, xMax)
    .toFloat()
    .coerceIn(0f, 1f)
  val topFraction = minOf(yMin, yMax)
    .toFloat()
    .coerceIn(0f, 1f)
  val rightFraction = maxOf(xMin, xMax)
    .toFloat()
    .coerceIn(0f, 1f)
  val bottomFraction = maxOf(yMin, yMax)
    .toFloat()
    .coerceIn(0f, 1f)

  return Rect(
    left = imageRect.left + imageRect.width * leftFraction,
    top = imageRect.top + imageRect.height * topFraction,
    right = imageRect.left + imageRect.width * rightFraction,
    bottom = imageRect.top + imageRect.height * bottomFraction
  )
}

/**
 * Returns top-left offset of rect.
 */
internal fun Rect.toOffset(): Offset {
  return Offset(
    x = left,
    y = top
  )
}

/**
 * Returns size of rect.
 */
internal fun Rect.toSize(): Size {
  return Size(
    width = width,
    height = height
  )
}