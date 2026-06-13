package com.karandaev.geo_inspect.feature.presentation.auth.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.karandaev.geo_inspect.core.presentation.auth.AuthViewModel

/**
 * Logical wrapper for the register screen.
 */
@Composable
fun RegisterRoute(
  authViewModel: AuthViewModel,
  onLoginClick: () -> Unit,
  onRegisterSuccess: () -> Unit
) {
  val state by authViewModel.state.collectAsStateWithLifecycle()
  val context = LocalContext.current

  RegisterScreen(
    state = state,
    onUserNameChange = authViewModel::setUserName,
    onUserNameClear = authViewModel::clearUserName,
    onEmailChange = authViewModel::setEmail,
    onEmailClear = authViewModel::clearEmail,
    onPasswordChange = authViewModel::setPassword,
    onPasswordClear = authViewModel::clearPassword,
    onRepeatedPasswordChange = authViewModel::setRepeatedPassword,
    onRepeatedPasswordClear = authViewModel::clearRepeatedPassword,
    onRegisterClick = {
      authViewModel.register(
        onSuccess = onRegisterSuccess
      )
    },
    onGoogleSignInClick = {
      authViewModel.signInWithGoogle(context)
    },
    onContinueAsGuestClick = authViewModel::continueAsGuest,
    onLoginClick = onLoginClick,
    onClearMessageClick = authViewModel::clearMessage
  )
}