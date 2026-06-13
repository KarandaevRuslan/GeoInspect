package com.karandaev.geo_inspect.feature.ui.components.detection_preview.placeholder

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.geometry.calculateFittedImageRect
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.geometry.toOffset
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.geometry.toSize

private val DetectionPlaceholderStrokeWidth = 1.dp
private val DetectionPlaceholderGridStrokeWidth = 1.dp

private const val DetectionPlaceholderGridLines = 4
private const val DetectionPlaceholderGridAlpha = 0.18f

/**
 * Draws image placeholder inside the same fitted area where bboxes are drawn.
 *
 * @param containerSize Preview container size.
 * @param imageAspectRatio Aspect ratio used to calculate fitted image area.
 * @param modifier Modifier applied to canvas.
 */
@Composable
internal fun DetectionPreviewPlaceholder(
  containerSize: IntSize,
  imageAspectRatio: Float,
  modifier: Modifier = Modifier
) {
  val textMeasurer = rememberTextMeasurer()
  val placeholderText = stringResource(
    R.string.inspection_report_detection_preview_placeholder
  )

  val containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
  val borderColor = MaterialTheme.colorScheme.outlineVariant
  val gridColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
    alpha = DetectionPlaceholderGridAlpha
  )
  val textStyle = MaterialTheme.typography.bodyMedium.copy(
    color = MaterialTheme.colorScheme.onSurfaceVariant,
    textAlign = TextAlign.Center
  )

  Canvas(modifier = modifier) {
    val imageRect = calculateFittedImageRect(
      containerSize = containerSize,
      imageAspectRatio = imageAspectRatio
    )

    drawPlaceholderBackground(
      color = containerColor,
      borderColor = borderColor,
      imageRect = imageRect
    )

    drawPlaceholderGrid(
      color = gridColor,
      imageRect = imageRect
    )

    val textLayout = textMeasurer.measure(
      text = placeholderText,
      style = textStyle
    )

    drawText(
      textLayoutResult = textLayout,
      topLeft = Offset(
        x = imageRect.left + (imageRect.width - textLayout.size.width) / 2f,
        y = imageRect.top + (imageRect.height - textLayout.size.height) / 2f
      )
    )
  }
}

/**
 * Draws placeholder background and border.
 */
private fun DrawScope.drawPlaceholderBackground(
  color: Color,
  borderColor: Color,
  imageRect: Rect
) {
  drawRect(
    color = color,
    topLeft = imageRect.toOffset(),
    size = imageRect.toSize()
  )

  drawRect(
    color = borderColor,
    topLeft = imageRect.toOffset(),
    size = imageRect.toSize(),
    style = Stroke(
      width = DetectionPlaceholderStrokeWidth.toPx()
    )
  )
}

/**
 * Draws placeholder grid.
 */
private fun DrawScope.drawPlaceholderGrid(
  color: Color,
  imageRect: Rect
) {
  repeat(DetectionPlaceholderGridLines) { index ->
    val fraction = (index + 1).toFloat() /
      (DetectionPlaceholderGridLines + 1).toFloat()

    val verticalX = imageRect.left + imageRect.width * fraction
    val horizontalY = imageRect.top + imageRect.height * fraction

    drawLine(
      color = color,
      start = Offset(verticalX, imageRect.top),
      end = Offset(verticalX, imageRect.bottom),
      strokeWidth = DetectionPlaceholderGridStrokeWidth.toPx()
    )

    drawLine(
      color = color,
      start = Offset(imageRect.left, horizontalY),
      end = Offset(imageRect.right, horizontalY),
      strokeWidth = DetectionPlaceholderGridStrokeWidth.toPx()
    )
  }
}