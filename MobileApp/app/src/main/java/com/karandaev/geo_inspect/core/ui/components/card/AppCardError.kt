package com.karandaev.geo_inspect.core.ui.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppCardError(
  title: String,
  message: String,
  modifier: Modifier = Modifier,
  minHeight: Dp = 56.dp,
  onRetry: (() -> Unit)? = null,
  titleColor: Color = MaterialTheme.colorScheme.onSurface,
  messageColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
  actionColor: Color = MaterialTheme.colorScheme.onSurface
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .heightIn(min = minHeight),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = titleColor
      )

      Text(
        text = message,
        style = MaterialTheme.typography.bodySmall,
        color = messageColor
      )
    }

    if (onRetry != null) {
      IconButton(onClick = onRetry) {
        Icon(
          imageVector = Icons.Default.Refresh,
          contentDescription = "Retry",
          tint = actionColor
        )
      }
    }
  }
}