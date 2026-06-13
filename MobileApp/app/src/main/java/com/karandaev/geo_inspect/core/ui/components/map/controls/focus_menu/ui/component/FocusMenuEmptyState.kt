package com.karandaev.geo_inspect.core.ui.components.map.controls.focus_menu.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Displays an empty state when no focus destinations match the query.
 */
@Composable
internal fun FocusMenuEmptyState() {
  Text(
    text = "No destinations found",
    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
    style = MaterialTheme.typography.bodyMedium,
    color = MaterialTheme.colorScheme.onSurfaceVariant
  )
}