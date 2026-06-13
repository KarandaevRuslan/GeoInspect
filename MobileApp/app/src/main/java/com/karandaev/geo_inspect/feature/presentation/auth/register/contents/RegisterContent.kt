package com.karandaev.geo_inspect.feature.presentation.auth.register.contents

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.karandaev.geo_inspect.app.adaptive_layout.AdaptiveLayoutType
import com.karandaev.geo_inspect.app.adaptive_layout.rememberAdaptiveLayoutType
import com.karandaev.geo_inspect.core.presentation.auth.model.AuthUiState

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
internal fun RegisterContent(
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
  BoxWithConstraints(
    modifier = modifier.fillMaxSize()
  ) {
    val layoutType = rememberAdaptiveLayoutType(
      maxWidth = maxWidth,
      maxHeight = maxHeight
    )

    when (layoutType) {
      AdaptiveLayoutType.SinglePane -> {
        RegisterSinglePaneContent(
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
          modifier = Modifier.fillMaxSize()
        )
      }

      AdaptiveLayoutType.TwoPane -> {
        RegisterTwoPaneContent(
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
          modifier = Modifier.fillMaxSize()
        )
      }
    }
  }
}