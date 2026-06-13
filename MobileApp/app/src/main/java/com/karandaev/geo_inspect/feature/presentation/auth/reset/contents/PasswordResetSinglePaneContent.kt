package com.karandaev.geo_inspect.feature.presentation.auth.reset.contents

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.core.presentation.auth.model.AuthUiState
import com.karandaev.geo_inspect.feature.presentation.auth.reset.components.PasswordResetFormList

@Composable
internal fun PasswordResetSinglePaneContent(
  state: AuthUiState,
  onEmailChange: (String) -> Unit,
  onEmailClear: () -> Unit,
  onResetClick: () -> Unit,
  onLoginClick: () -> Unit,
  onContinueAsGuestClick: () -> Unit,
  onClearMessageClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  PasswordResetFormList(
    state = state,
    contentPadding = PaddingValues(
      horizontal = 16.dp,
      vertical = 12.dp
    ),
    onEmailChange = onEmailChange,
    onEmailClear = onEmailClear,
    onResetClick = onResetClick,
    onLoginClick = onLoginClick,
    onContinueAsGuestClick = onContinueAsGuestClick,
    onClearMessageClick = onClearMessageClick,
    modifier = modifier
  )
}