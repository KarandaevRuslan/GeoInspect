package com.karandaev.geo_inspect.feature.presentation.auth.login.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.feature.presentation.auth.components.buttons.ContinueAsGuestButton
import com.karandaev.geo_inspect.feature.presentation.auth.components.buttons.GoogleSignInButton
import com.karandaev.geo_inspect.feature.presentation.auth.components.buttons.base.AuthPrimaryButton
import com.karandaev.geo_inspect.feature.presentation.auth.components.buttons.base.AuthTextButton

@Composable
internal fun LoginActions(
  isLoading: Boolean,
  onLoginClick: () -> Unit,
  onGoogleSignInClick: () -> Unit,
  onRegisterClick: () -> Unit,
  onPasswordResetClick: () -> Unit,
  onContinueAsGuestClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(14.dp),
    horizontalAlignment = Alignment.Start
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      AuthPrimaryButton(
        text = "Sign in",
        isLoading = isLoading,
        onClick = onLoginClick,
        modifier = Modifier.fillMaxWidth()
      )

      GoogleSignInButton(
        isLoading = isLoading,
        onClick = onGoogleSignInClick,
        modifier = Modifier.fillMaxWidth()
      )

      ContinueAsGuestButton(
        isLoading = isLoading,
        onClick = onContinueAsGuestClick
      )
    }

    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(2.dp),
      horizontalAlignment = Alignment.Start
    ) {
      AuthTextButton(
        text = "Create account",
        isLoading = isLoading,
        onClick = onRegisterClick,
        textAlignment = Alignment.CenterStart
      )

      AuthTextButton(
        text = "Forgot password?",
        isLoading = isLoading,
        onClick = onPasswordResetClick,
        textAlignment = Alignment.CenterStart
      )
    }
  }
}