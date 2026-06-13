package com.karandaev.geo_inspect.core.ui.components.toggle_group

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

/**
 * Universal multi-option toggle group.
 *
 * Prefer stable keys for options:
 *
 * ToggleGroup(
 *   options = mapOf(
 *     ThemeMode.System to "System",
 *     ThemeMode.Light to "Light",
 *     ThemeMode.Dark to "Dark"
 *   ),
 *   selectedOption = selectedTheme,
 *   onSelect = { selectedTheme = it }
 * )
 */
@Composable
fun <T> ToggleGroup(
  options: Map<T, String>,
  selectedOption: T?,
  onSelect: (T) -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  itemEnabled: (T) -> Boolean = { true },
  colors: ToggleGroupColors = ToggleGroupDefaults.colors(),
  shape: Shape = ToggleGroupDefaults.Shape,
  itemShape: Shape = ToggleGroupDefaults.ItemShape,
  textStyle: TextStyle = MaterialTheme.typography.labelLarge,
  horizontalPadding: Dp = ToggleGroupDefaults.HorizontalPadding,
  verticalPadding: Dp = ToggleGroupDefaults.VerticalPadding,
  itemModifier: RowScope.(option: T, isSelected: Boolean) -> Modifier = { _, _ -> Modifier }
) {
  Row(
    modifier = modifier
      .clip(shape)
      .background(colors.containerColor)
  ) {
    options.forEach { (option, label) ->
      val isSelected = option == selectedOption
      val isItemEnabled = enabled && itemEnabled(option)

      Text(
        text = label,
        style = textStyle,
        color = colors.contentColor(
          selected = isSelected,
          enabled = isItemEnabled
        ),
        modifier = Modifier
          .clip(itemShape)
          .then(
            if (isSelected) {
              Modifier.background(colors.selectedContainerColor)
            } else {
              Modifier
            }
          )
          .clickable(enabled = isItemEnabled) {
            onSelect(option)
          }
          .padding(
            horizontal = horizontalPadding,
            vertical = verticalPadding
          )
          .then(itemModifier(option, isSelected))
      )
    }
  }
}