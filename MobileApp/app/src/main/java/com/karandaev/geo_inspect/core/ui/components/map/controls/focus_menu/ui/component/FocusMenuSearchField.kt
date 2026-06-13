package com.karandaev.geo_inspect.core.ui.components.map.controls.focus_menu.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Displays a title search field for focus menu destinations.
 */
@Composable
internal fun FocusMenuSearchField(
  query: String,
  onQueryChange: (String) -> Unit,
  onClearClick: () -> Unit
) {
  OutlinedTextField(
    value = query,
    onValueChange = onQueryChange,
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp, vertical = 12.dp),
    singleLine = true,
    label = {
      Text(text = "Search by title")
    },
    trailingIcon = {
      if (query.isNotEmpty()) {
        TextButton(
          onClick = onClearClick
        ) {
          Text(text = "Clear")
        }
      }
    }
  )
}