package com.karandaev.geo_inspect.feature.presentation.auth.components.buttons.base

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun AuthOutlinedButton(
  text: String,
  isLoading: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  OutlinedButton(
    onClick = onClick,
    enabled = !isLoading,
    shape = AuthButtonShape,
    border = BorderStroke(
      width = 1.dp,
      color = MaterialTheme.colorScheme.outline
    ),
    colors = ButtonDefaults.outlinedButtonColors(
      contentColor = MaterialTheme.colorScheme.primary
    ),
    modifier = modifier.defaultMinSize(
      minHeight = AuthButtonMinHeight
    )
  ) {
    AuthButtonContent(
      text = text,
      isLoading = isLoading
    )
  }
}