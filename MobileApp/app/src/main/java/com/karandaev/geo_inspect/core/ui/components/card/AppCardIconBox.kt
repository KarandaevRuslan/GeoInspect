package com.karandaev.geo_inspect.core.ui.components.card

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppCardIconBox(
  modifier: Modifier = Modifier,
  size: Dp = 40.dp,
  shape: Shape = RoundedCornerShape(8.dp),
  content: @Composable () -> Unit
) {
  Card(
    shape = shape,
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer
    ),
    modifier = modifier.size(size)
  ) {
    CompositionLocalProvider(
      LocalContentColor provides MaterialTheme.colorScheme.onPrimaryContainer
    ) {
      content()
    }
  }
}