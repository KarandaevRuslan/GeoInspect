package com.karandaev.geo_inspect.feature.presentation.auth.register

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.karandaev.geo_inspect.core.presentation.auth.model.AuthUiState
import com.karandaev.geo_inspect.feature.presentation.auth.register.contents.RegisterContent

/**
 * Register screen UI.
 */
@Composable
fun RegisterScreen(
  state: AuthUiState,
  onUserNameChange: (String) -> Unit,
  onUserNameClear: () -> Unit,
  onEmailChange: (String) -> Unit,
  onEmailClear: () -> Unit,
  onPasswordChange: (String) -> Unit,
  onPasswordClear: () -> Unit,
  onRepeatedPasswordChange: (String) -> Unit,
  onRepeatedPasswordClear: () -> Unit,
  onRegisterClick: () -> Unit,
  onGoogleSignInClick: () -> Unit,
  onContinueAsGuestClick: () -> Unit,
  onLoginClick: () -> Unit,
  onClearMessageClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  RegisterContent(
    state = state,
    onUserNameChange = onUserNameChange,
    onUserNameClear = onUserNameClear,
    onEmailChange = onEmailChange,
    onEmailClear = onEmailClear,
    onPasswordChange = onPasswordChange,
    onPasswordClear = onPasswordClear,
    onRepeatedPasswordChange = onRepeatedPasswordChange,
    onRepeatedPasswordClear = onRepeatedPasswordClear,
    onRegisterClick = onRegisterClick,
    onGoogleSignInClick = onGoogleSignInClick,
    onContinueAsGuestClick = onContinueAsGuestClick,
    onLoginClick = onLoginClick,
    onClearMessageClick = onClearMessageClick,
    modifier = modifier
  )
}