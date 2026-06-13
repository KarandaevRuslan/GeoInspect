package com.karandaev.geo_inspect.feature.presentation.auth.components.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.karandaev.geo_inspect.feature.presentation.auth.components.buttons.base.AuthTextButton

/**
 * Button that allows the user to continue without signing in.
 */
@Composable
internal fun ContinueAsGuestButton(
  isLoading: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  AuthTextButton(
    text = "Continue as guest",
    isLoading = isLoading,
    onClick = onClick,
    modifier = modifier.fillMaxWidth()
  )
}