package com.karandaev.geo_inspect.feature.ui.components.detection_preview.dialog.details.palette

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val DetectionPreviewPaletteColorSize = 22.dp
private val DetectionPreviewPaletteColorShape = RoundedCornerShape(6.dp)

/**
 * Compact row of image palette colors.
 *
 * @param palette Image palette to display.
 * @param modifier Modifier applied to root.
 */
@Composable
internal fun DetectionPreviewPaletteRow(
  palette: DetectionPreviewImagePalette?,
  modifier: Modifier = Modifier
) {
  if (palette == null || !palette.hasColors) {
    return
  }

  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      palette.colors.forEach { color ->
        DetectionPreviewPaletteColorBox(
          color = color
        )
      }
    }

    Text(
      text = palette.colors.joinToString(
        separator = " · "
      ) { color -> color.hexText },
      style = MaterialTheme.typography.labelSmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}

/**
 * Single palette color swatch.
 */
@Composable
private fun DetectionPreviewPaletteColorBox(
  color: DetectionPreviewPaletteColor
) {
  Box(
    modifier = Modifier
      .size(DetectionPreviewPaletteColorSize)
      .clip(DetectionPreviewPaletteColorShape)
      .background(Color(color.colorArgb))
  )
}