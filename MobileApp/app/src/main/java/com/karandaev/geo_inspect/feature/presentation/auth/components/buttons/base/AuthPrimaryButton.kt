package com.karandaev.geo_inspect.feature.presentation.auth.components.buttons.base

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun AuthPrimaryButton(
  text: String,
  isLoading: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Button(
    onClick = onClick,
    enabled = !isLoading,
    shape = AuthButtonShape,
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