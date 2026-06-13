package com.karandaev.geo_inspect.app.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CreateNoteFab(
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  FloatingActionButton(
    onClick = onClick,
    modifier = modifier,
    containerColor = MaterialTheme.colorScheme.primaryContainer,
    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
  ) {
    Icon(
      imageVector = Icons.Default.Add,
      contentDescription = "New note"
    )
  }
}