package com.karandaev.geo_inspect.feature.presentation.auth.register.contents

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.core.presentation.auth.model.AuthUiState
import com.karandaev.geo_inspect.feature.presentation.auth.register.components.RegisterFormList

@Composable
internal fun RegisterSinglePaneContent(
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
  RegisterFormList(
    state = state,
    contentPadding = PaddingValues(
      horizontal = 16.dp,
      vertical = 12.dp
    ),
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