package com.karandaev.geo_inspect.core.ui.components.toggle_group

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ToggleGroupColors(
  val containerColor: Color,
  val selectedContainerColor: Color,
  val selectedContentColor: Color,
  val contentColor: Color,
  val disabledContentColor: Color
) {
  fun contentColor(
    selected: Boolean,
    enabled: Boolean
  ): Color {
    return when {
      !enabled -> disabledContentColor
      selected -> selectedContentColor
      else -> contentColor
    }
  }
}