package com.karandaev.geo_inspect.feature.presentation.auth.reset.contents

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
internal fun PasswordResetContent(
  state: AuthUiState,
  onEmailChange: (String) -> Unit,
  onEmailClear: () -> Unit,
  onResetClick: () -> Unit,
  onLoginClick: () -> Unit,
  onContinueAsGuestClick: () -> Unit,
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
        PasswordResetSinglePaneContent(
          state = state,
          onEmailChange = onEmailChange,
          onEmailClear = onEmailClear,
          onResetClick = onResetClick,
          onLoginClick = onLoginClick,
          onContinueAsGuestClick = onContinueAsGuestClick,
          onClearMessageClick = onClearMessageClick,
          modifier = Modifier.fillMaxSize()
        )
      }

      AdaptiveLayoutType.TwoPane -> {
        PasswordResetTwoPaneContent(
          state = state,
          onEmailChange = onEmailChange,
          onEmailClear = onEmailClear,
          onResetClick = onResetClick,
          onLoginClick = onLoginClick,
          onContinueAsGuestClick = onContinueAsGuestClick,
          onClearMessageClick = onClearMessageClick,
          modifier = Modifier.fillMaxSize()
        )
      }
    }
  }
}