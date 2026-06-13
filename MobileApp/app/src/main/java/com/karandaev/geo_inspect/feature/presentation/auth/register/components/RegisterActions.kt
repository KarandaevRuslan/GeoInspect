package com.karandaev.geo_inspect.feature.presentation.auth.register.components

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
internal fun RegisterActions(
  isLoading: Boolean,
  onRegisterClick: () -> Unit,
  onGoogleSignInClick: () -> Unit,
  onContinueAsGuestClick: () -> Unit,
  onLoginClick: () -> Unit,
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
        text = "Sign up",
        isLoading = isLoading,
        onClick = onRegisterClick,
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
        text = "Already have an account? Sign in",
        isLoading = isLoading,
        onClick = onLoginClick,
        textAlignment = Alignment.CenterStart
      )
    }
  }
}