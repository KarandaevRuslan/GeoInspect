package com.karandaev.geo_inspect.feature.presentation.auth.components.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.karandaev.geo_inspect.feature.presentation.auth.components.buttons.base.AuthOutlinedButton

/**
 * Google sign-in button.
 */
@Composable
internal fun GoogleSignInButton(
  isLoading: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  AuthOutlinedButton(
    text = "Continue with Google",
    isLoading = isLoading,
    onClick = onClick,
    modifier = modifier.fillMaxWidth()
  )
}