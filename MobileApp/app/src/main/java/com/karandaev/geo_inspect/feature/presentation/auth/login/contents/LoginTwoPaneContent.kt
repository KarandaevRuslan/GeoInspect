package com.karandaev.geo_inspect.feature.presentation.auth.login.contents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.core.presentation.auth.model.AuthUiState
import com.karandaev.geo_inspect.feature.presentation.auth.components.navigation.AuthHeader
import com.karandaev.geo_inspect.feature.presentation.auth.login.components.LoginFormList

@Composable
internal fun LoginTwoPaneContent(
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
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    LazyColumn(
      modifier = Modifier
        .weight(0.9f)
        .fillMaxHeight(),
      contentPadding = PaddingValues(
        start = 16.dp,
        top = 16.dp,
        bottom = 16.dp
      ),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      item {
        AuthHeader(title = stringResource(id = R.string.destination_auth_login))
      }

      item {
        Text(
          text = stringResource(id = R.string.auth_login_description),
          style = MaterialTheme.typography.bodyLarge,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    LoginFormList(
      state = state,
      contentPadding = PaddingValues(
        top = 16.dp,
        end = 16.dp,
        bottom = 16.dp
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
      modifier = Modifier
        .weight(1.1f)
        .fillMaxHeight()
    )
  }
}