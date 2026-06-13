package com.karandaev.geo_inspect.feature.presentation.auth.reset

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.karandaev.geo_inspect.core.presentation.auth.model.AuthUiState
import com.karandaev.geo_inspect.feature.presentation.auth.reset.contents.PasswordResetContent

/**
 * Password reset screen UI.
 */
@Composable
fun PasswordResetScreen(
  state: AuthUiState,
  onEmailChange: (String) -> Unit,
  onEmailClear: () -> Unit,
  onResetClick: () -> Unit,
  onLoginClick: () -> Unit,
  onContinueAsGuestClick: () -> Unit,
  onClearMessageClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  PasswordResetContent(
    state = state,
    onEmailChange = onEmailChange,
    onEmailClear = onEmailClear,
    onResetClick = onResetClick,
    onLoginClick = onLoginClick,
    onContinueAsGuestClick = onContinueAsGuestClick,
    onClearMessageClick = onClearMessageClick,
    modifier = modifier
  )
}