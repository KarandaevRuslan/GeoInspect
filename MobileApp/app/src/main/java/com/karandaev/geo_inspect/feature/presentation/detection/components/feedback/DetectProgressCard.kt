package com.karandaev.geo_inspect.feature.presentation.detection.components.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Displays detection loading state.
 */
@Composable
internal fun DetectProgressCard(
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier.padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      CircularProgressIndicator(
        modifier = Modifier.size(24.dp),
        strokeWidth = 2.dp
      )

      Text(
        text = "Running infrastructure damage detection…",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface
      )
    }
  }
}