package com.karandaev.geo_inspect.feature.presentation.auth.login.contents

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.core.presentation.auth.model.AuthUiState
import com.karandaev.geo_inspect.feature.presentation.auth.login.components.LoginFormList


@Composable
internal fun LoginSinglePaneContent(
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
  LoginFormList(
    state = state,
    contentPadding = PaddingValues(
      horizontal = 16.dp,
      vertical = 12.dp
    ),
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