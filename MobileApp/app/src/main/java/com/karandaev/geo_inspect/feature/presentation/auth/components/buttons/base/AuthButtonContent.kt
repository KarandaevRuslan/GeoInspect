package com.karandaev.geo_inspect.feature.presentation.auth.components.buttons.base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun AuthButtonContent(
  text: String,
  isLoading: Boolean
) {
  Row(
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    if (isLoading) {
      CircularProgressIndicator(
        modifier = Modifier.size(AuthButtonLoaderSize),
        strokeWidth = 2.dp,
        color = MaterialTheme.colorScheme.onPrimary
      )
    }

    Text(text = text)
  }
}