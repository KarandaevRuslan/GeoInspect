package com.karandaev.geo_inspect.feature.presentation.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun ViewInspectionReportLastUpdatedRow(
  text: String,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(6.dp)
  ) {
    Icon(
      imageVector = Icons.Default.AccessTime,
      contentDescription = null,
      modifier = Modifier.size(16.dp),
      tint = MaterialTheme.colorScheme.outline
    )

    Text(
      text = "Updated: $text",
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.outline
    )
  }
}