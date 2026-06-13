package com.karandaev.geo_inspect.core.ui.components.toggle_group

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object ToggleGroupDefaults {

  val Shape: Shape
    @Composable get() = RoundedCornerShape(20.dp)

  val ItemShape: Shape
    @Composable get() = RoundedCornerShape(20.dp)

  val HorizontalPadding: Dp = 14.dp
  val VerticalPadding: Dp = 8.dp

  @Composable
  fun colors(
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    selectedContainerColor: Color = MaterialTheme.colorScheme.primary,
    selectedContentColor: Color = MaterialTheme.colorScheme.onPrimary,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    disabledContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
  ): ToggleGroupColors {
    return ToggleGroupColors(
      containerColor = containerColor,
      selectedContainerColor = selectedContainerColor,
      selectedContentColor = selectedContentColor,
      contentColor = contentColor,
      disabledContentColor = disabledContentColor
    )
  }
}