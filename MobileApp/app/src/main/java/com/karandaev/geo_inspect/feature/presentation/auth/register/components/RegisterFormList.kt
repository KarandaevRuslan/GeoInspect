package com.karandaev.geo_inspect.feature.presentation.auth.register.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.core.presentation.auth.model.AuthUiState
import com.karandaev.geo_inspect.core.ui.components.bars.PasswordStrengthBar
import com.karandaev.geo_inspect.feature.presentation.auth.components.cards.authMessageItem
import com.karandaev.geo_inspect.feature.ui.components.fields.MyEmailField
import com.karandaev.geo_inspect.feature.ui.components.fields.MyPasswordField
import com.karandaev.geo_inspect.feature.ui.components.fields.MyUserNameField

@Composable
internal fun RegisterFormList(
  state: AuthUiState,
  contentPadding: PaddingValues,
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
        MyUserNameField(
          userName = state.userName,
          enabled = !isLoading,
          onUserNameChange = onUserNameChange,
          onUserNameClear = onUserNameClear,
          modifier = Modifier.fillMaxWidth()
        )

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

        MyPasswordField(
          password = state.repeatedPassword,
          enabled = !isLoading,
          onPasswordChange = onRepeatedPasswordChange,
          onPasswordClear = onRepeatedPasswordClear,
          modifier = Modifier.fillMaxWidth(),
          label = "Repeat password"
        )

        PasswordStrengthBar(
          strength = state.passwordStrength
        )
      }
    }

    item {
      RegisterActions(
        isLoading = isLoading,
        onRegisterClick = onRegisterClick,
        onGoogleSignInClick = onGoogleSignInClick,
        onContinueAsGuestClick = onContinueAsGuestClick,
        onLoginClick = onLoginClick
      )
    }
  }
}