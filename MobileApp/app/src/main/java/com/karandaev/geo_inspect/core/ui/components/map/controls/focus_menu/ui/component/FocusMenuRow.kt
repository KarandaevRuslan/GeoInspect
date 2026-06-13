package com.karandaev.geo_inspect.core.ui.components.map.controls.focus_menu.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.core.ui.components.map.controls.focus_menu.model.OsmFocusMenuItem
import com.karandaev.geo_inspect.core.ui.components.map.controls.focus_menu.ui.formatter.focusMenuDistanceText
import com.karandaev.geo_inspect.core.ui.components.map.controls.focus_menu.ui.formatter.focusMenuLabel
import com.karandaev.geo_inspect.core.ui.components.map.controls.focus_menu.ui.formatter.focusMenuPositionText

/**
 * Displays one selectable focus destination row.
 */
@Composable
internal fun FocusMenuRow(
  item: OsmFocusMenuItem,
  onClick: () -> Unit
) {
  val colors = focusMenuRowColors(isOrigin = item.isOrigin)

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 12.dp, vertical = 2.dp)
      .clip(RoundedCornerShape(12.dp))
      .background(colors.background)
      .clickable(onClick = onClick)
      .padding(horizontal = 8.dp, vertical = 10.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = item.title,
        modifier = Modifier.weight(1f),
        style = MaterialTheme.typography.bodyLarge,
        color = colors.title
      )

      Text(
        text = item.focusMenuLabel(),
        style = MaterialTheme.typography.labelSmall,
        color = colors.label
      )
    }

    Text(
      text = item.point.focusMenuPositionText(),
      modifier = Modifier.padding(top = 2.dp),
      style = MaterialTheme.typography.bodySmall,
      color = colors.secondary
    )

    Text(
      text = item.focusMenuDistanceText(),
      modifier = Modifier.padding(top = 2.dp),
      style = MaterialTheme.typography.bodySmall,
      color = colors.secondary
    )
  }
}

/**
 * Returns color values used by a focus menu row.
 */
@Composable
private fun focusMenuRowColors(
  isOrigin: Boolean
): FocusMenuRowColors {
  return if (isOrigin) {
    FocusMenuRowColors(
      background = MaterialTheme.colorScheme.primaryContainer,
      title = MaterialTheme.colorScheme.onPrimaryContainer,
      secondary = MaterialTheme.colorScheme.onPrimaryContainer,
      label = MaterialTheme.colorScheme.onPrimaryContainer
    )
  } else {
    FocusMenuRowColors(
      background = MaterialTheme.colorScheme.surface,
      title = MaterialTheme.colorScheme.onSurface,
      secondary = MaterialTheme.colorScheme.onSurfaceVariant,
      label = MaterialTheme.colorScheme.primary
    )
  }
}

/**
 * Color values used by a focus menu row.
 */
private data class FocusMenuRowColors(
  val background: Color,
  val title: Color,
  val secondary: Color,
  val label: Color
)