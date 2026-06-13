package com.karandaev.geo_inspect.feature.ui.components.detection_preview.overlay

import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb

/**
 * Draws detection label near bbox top-left corner.
 *
 * @param label Label text.
 * @param color Detection class color.
 * @param bboxRect Bounding box rect.
 * @param imageRect Displayed image rect.
 * @param textPaint Android text paint.
 * @param style Overlay drawing style.
 */
internal fun DrawScope.drawDetectionLabel(
  label: String,
  color: Color,
  bboxRect: Rect,
  imageRect: Rect,
  textPaint: Paint,
  style: DetectionOverlayStyle
) {
  val labelWidth = textPaint.measureText(label)
  val fontMetrics = textPaint.fontMetrics

  val textHeight = fontMetrics.bottom - fontMetrics.top
  val labelHeight = textHeight + style.labelVerticalPaddingPx * 2f

  val labelTop = if (
    bboxRect.top - labelHeight - style.labelSpacingPx >= imageRect.top
  ) {
    bboxRect.top - labelHeight - style.labelSpacingPx
  } else {
    bboxRect.top + style.labelSpacingPx
  }

  drawRect(
    color = color.copy(alpha = DetectionOverlayLabelBackgroundAlpha),
    topLeft = Offset(
      x = bboxRect.left,
      y = labelTop
    ),
    size = Size(
      width = labelWidth + style.labelHorizontalPaddingPx * 2f,
      height = labelHeight
    )
  )

  drawIntoCanvas { canvas ->
    textPaint.color = Color.White.toArgb()

    canvas.nativeCanvas.drawText(
      label,
      bboxRect.left + style.labelHorizontalPaddingPx,
      labelTop + style.labelVerticalPaddingPx - fontMetrics.top,
      textPaint
    )
  }
}