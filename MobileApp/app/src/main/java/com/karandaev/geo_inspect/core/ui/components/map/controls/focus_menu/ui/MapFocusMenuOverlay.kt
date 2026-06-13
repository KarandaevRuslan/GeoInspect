package com.karandaev.geo_inspect.core.ui.components.map.controls.focus_menu.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.core.ui.components.map.controls.focus_menu.model.OsmFocusMenuItem
import com.karandaev.geo_inspect.core.ui.components.map.controls.focus_menu.ui.component.FocusMenuEmptyState
import com.karandaev.geo_inspect.core.ui.components.map.controls.focus_menu.ui.component.FocusMenuHeader
import com.karandaev.geo_inspect.core.ui.components.map.controls.focus_menu.ui.component.FocusMenuItemList
import com.karandaev.geo_inspect.core.ui.components.map.controls.focus_menu.ui.component.FocusMenuSearchField
import com.karandaev.geo_inspect.core.ui.components.map.controls.focus_menu.ui.filter.filterByTitle

/**
 * Displays a centered overlay with available map focus destinations.
 *
 * @param items Focus destinations shown to the user.
 * @param showSearch Whether the title search field should be displayed.
 * @param onItemClick Called when a destination is selected.
 * @param onDismiss Called when the overlay background is clicked.
 */
@Composable
fun MapFocusMenuOverlay(
  items: List<OsmFocusMenuItem>,
  showSearch: Boolean,
  onItemClick: (OsmFocusMenuItem) -> Unit,
  onDismiss: () -> Unit
) {
  var searchQuery by rememberSaveable { mutableStateOf("") }
  val effectiveSearchQuery = searchQuery.takeIf { showSearch }.orEmpty()

  val filteredItems by remember(items, effectiveSearchQuery) {
    derivedStateOf {
      items.filterByTitle(effectiveSearchQuery)
    }
  }

  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    FocusMenuDismissBackground(onDismiss = onDismiss)

    FocusMenuCard {
      FocusMenuHeader()

      if (showSearch) {
        FocusMenuSearchField(
          query = searchQuery,
          onQueryChange = { value ->
            searchQuery = value
          },
          onClearClick = {
            searchQuery = ""
          }
        )
      }

      if (filteredItems.isEmpty()) {
        FocusMenuEmptyState()
      } else {
        FocusMenuItemList(
          items = filteredItems,
          showSearch = showSearch,
          onItemClick = onItemClick
        )
      }
    }
  }
}

/**
 * Displays a full-screen background layer that dismisses the focus menu.
 */
@Composable
private fun FocusMenuDismissBackground(
  onDismiss: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.32f))
      .clickable(onClick = onDismiss)
  )
}

/**
 * Displays the focus menu surface.
 */
@Composable
private fun FocusMenuCard(
  content: @Composable () -> Unit
) {
  Card(
    modifier = Modifier
      .padding(24.dp)
      .widthIn(max = 420.dp)
      .heightIn(max = 520.dp),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
  ) {
    Column(
      modifier = Modifier.padding(vertical = 16.dp)
    ) {
      content()
    }
  }
}