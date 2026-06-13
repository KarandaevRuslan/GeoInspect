package com.karandaev.geo_inspect.feature.presentation.auth.login.contents

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
internal fun LoginContent(
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
  BoxWithConstraints(
    modifier = modifier.fillMaxSize()
  ) {
    val layoutType = rememberAdaptiveLayoutType(
      maxWidth = maxWidth,
      maxHeight = maxHeight
    )

    when (layoutType) {
      AdaptiveLayoutType.SinglePane -> {
        LoginSinglePaneContent(
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
          modifier = Modifier.fillMaxSize()
        )
      }

      AdaptiveLayoutType.TwoPane -> {
        LoginTwoPaneContent(
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
          modifier = Modifier.fillMaxSize()
        )
      }
    }
  }
}