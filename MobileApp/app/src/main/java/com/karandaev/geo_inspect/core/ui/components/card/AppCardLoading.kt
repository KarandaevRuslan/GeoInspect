package com.karandaev.geo_inspect.core.ui.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppCardLoading(
  message: String,
  modifier: Modifier = Modifier,
  minHeight: Dp = 56.dp,
  contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .heightIn(min = minHeight),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    CircularProgressIndicator(
      color = ProgressIndicatorDefaults.circularColor,
      strokeWidth = 2.dp,
      modifier = Modifier.size(24.dp)
    )

    Text(
      text = message,
      style = MaterialTheme.typography.bodyMedium,
      color = contentColor
    )
  }
}