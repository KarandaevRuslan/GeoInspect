package com.karandaev.geo_inspect.core.ui.components.map.controls.focus_menu.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.core.ui.components.map.controls.focus_menu.model.OsmFocusMenuItem

/**
 * Displays selectable focus menu destination rows.
 */
@Composable
internal fun FocusMenuItemList(
  items: List<OsmFocusMenuItem>,
  showSearch: Boolean,
  onItemClick: (OsmFocusMenuItem) -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxWidth()
      .padding(top = if (showSearch) 0.dp else 8.dp),
    contentPadding = PaddingValues(vertical = 4.dp)
  ) {
    items(
      items = items,
      key = { item -> item.id }
    ) { item ->
      FocusMenuRow(
        item = item,
        onClick = { onItemClick(item) }
      )
    }
  }
}