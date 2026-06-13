package com.karandaev.geo_inspect.feature.presentation.auth.components.buttons.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun AuthTextButton(
  text: String,
  isLoading: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  textAlignment: Alignment = Alignment.Center
) {
  TextButton(
    onClick = onClick,
    enabled = !isLoading,
    shape = AuthButtonShape,
    modifier = modifier
      .fillMaxWidth()
      .defaultMinSize(
        minHeight = 44.dp
      )
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp),
      contentAlignment = textAlignment
    ) {
      Text(text = text)
    }
  }
}