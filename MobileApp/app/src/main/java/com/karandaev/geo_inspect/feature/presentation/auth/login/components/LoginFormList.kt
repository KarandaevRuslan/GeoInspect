package com.karandaev.geo_inspect.feature.presentation.auth.login.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.core.presentation.auth.model.AuthUiState
import com.karandaev.geo_inspect.feature.presentation.auth.components.cards.authMessageItem
import com.karandaev.geo_inspect.feature.ui.components.fields.MyEmailField
import com.karandaev.geo_inspect.feature.ui.components.fields.MyPasswordField

@Composable
internal fun LoginFormList(
  state: AuthUiState,
  contentPadding: PaddingValues,
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
  val isLoading = state.isLoading

  LazyColumn(
    modifier = modifier,
    contentPadding = contentPadding,
    verticalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    authMessageItem(
      state = state,
      onClearMessageClick = onClearMessageClick
    )

    item {
      Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        MyEmailField(
          email = state.email,
          enabled = !isLoading,
          onEmailChange = onEmailChange,
          onEmailClear = onEmailClear,
          modifier = Modifier.fillMaxWidth()
        )

        MyPasswordField(
          password = state.password,
          enabled = !isLoading,
          onPasswordChange = onPasswordChange,
          onPasswordClear = onPasswordClear,
          modifier = Modifier.fillMaxWidth()
        )
      }
    }

    item {
      LoginActions(
        isLoading = isLoading,
        onLoginClick = onLoginClick,
        onGoogleSignInClick = onGoogleSignInClick,
        onRegisterClick = onRegisterClick,
        onPasswordResetClick = onPasswordResetClick,
        onContinueAsGuestClick = onContinueAsGuestClick
      )
    }
  }
}