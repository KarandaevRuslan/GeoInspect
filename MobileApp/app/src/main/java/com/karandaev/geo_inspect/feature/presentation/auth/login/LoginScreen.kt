package com.karandaev.geo_inspect.feature.presentation.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.karandaev.geo_inspect.core.presentation.auth.model.AuthUiState
import com.karandaev.geo_inspect.feature.presentation.auth.login.contents.LoginContent

/**
 * Login screen UI.
 */
@Composable
fun LoginScreen(
  state: AuthUiState,
  onEmailChange: (String) -> Unit,
  onEmailClear: () -> Unit,
  onPasswordChange: (String) -> Unit,
  onPasswordClear: () -> Unit,
  onLoginClick: () -> Unit,
  onGoogleSignInClick: () -> Unit,
  onContinueAsGuestClick: () -> Unit,
  onRegisterClick: () -> Unit,
  onPasswordResetClick: () -> Unit,
  onClearMessageClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  LoginContent(
    state = state,
    onEmailChange = onEmailChange,
    onEmailClear = onEmailClear,
    onPasswordChange = onPasswordChange,
    onPasswordClear = onPasswordClear,
    onLoginClick = onLoginClick,
    onGoogleSignInClick = onGoogleSignInClick,
    onContinueAsGuestClick = onContinueAsGuestClick,
    onRegisterClick = onRegisterClick,
    onPasswordResetClick = onPasswordResetClick,
    onClearMessageClick = onClearMessageClick,
    modifier = modifier
  )
}