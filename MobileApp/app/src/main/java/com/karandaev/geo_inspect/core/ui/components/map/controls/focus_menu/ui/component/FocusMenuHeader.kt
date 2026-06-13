package com.karandaev.geo_inspect.core.ui.components.map.controls.focus_menu.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Displays the focus menu title.
 */
@Composable
internal fun FocusMenuHeader() {
  Text(
    text = "Focus destination",
    modifier = Modifier.padding(horizontal = 20.dp),
    style = MaterialTheme.typography.titleMedium,
    color = MaterialTheme.colorScheme.onSurface
  )
}