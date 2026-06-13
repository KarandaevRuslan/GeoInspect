package com.karandaev.geo_inspect.feature.presentation.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.karandaev.geo_inspect.core.presentation.auth.AuthViewModel

/**
 * Logical wrapper for the login screen.
 */
@Composable
fun LoginRoute(
  authViewModel: AuthViewModel,
  onRegisterClick: () -> Unit,
  onPasswordResetClick: () -> Unit
) {
  val state by authViewModel.state.collectAsStateWithLifecycle()
  val context = LocalContext.current

  LoginScreen(
    state = state,
    onEmailChange = authViewModel::setEmail,
    onEmailClear = authViewModel::clearEmail,
    onPasswordChange = authViewModel::setPassword,
    onPasswordClear = authViewModel::clearPassword,
    onLoginClick = authViewModel::login,
    onGoogleSignInClick = {
      authViewModel.signInWithGoogle(context)
    },
    onContinueAsGuestClick = authViewModel::continueAsGuest,
    onRegisterClick = onRegisterClick,
    onPasswordResetClick = onPasswordResetClick,
    onClearMessageClick = authViewModel::clearMessage
  )
}