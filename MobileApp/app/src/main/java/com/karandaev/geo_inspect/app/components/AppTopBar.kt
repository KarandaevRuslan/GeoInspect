package com.karandaev.geo_inspect.app.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

/**
 * Top app bar used by MapNotes screens.
 *
 * @param title Title displayed in the app bar.
 * @param showBackButton Whether the navigation back button should be displayed.
 * @param onBackClick Callback invoked when the back button is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
  title: String,
  showBackButton: Boolean,
  onBackClick: () -> Unit
) {
  TopAppBar(
    title = { Text(title) },
    navigationIcon = {
      if (showBackButton) {
        IconButton(onClick = onBackClick) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back"
          )
        }
      }
    },
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.surface,
      titleContentColor = MaterialTheme.colorScheme.onSurface
    )
  )
}