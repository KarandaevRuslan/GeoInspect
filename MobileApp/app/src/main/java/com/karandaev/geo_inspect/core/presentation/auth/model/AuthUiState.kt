package com.karandaev.geo_inspect.core.presentation.auth.model

import com.karandaev.geo_inspect.core.image.source_file.ImageSourceFile
import com.karandaev.geo_inspect.core.presentation.message.UiMessage

/**
 * UI state for the authentication screen.
 *
 * @param userName Current user name input value.
 * @param email Current email input value.
 * @param password Current password input value.
 * @param repeatedPassword Current repeated password input value.
 * @param originalPassword Current password used for reauthentication before sensitive operations.
 * @param passwordStrength The number representing the reliability of the password.
 * @param session Current Firebase authentication session.
 * @param isGuestModeEnabled Whether the user chose to access the app without signing in.
 * @param isLoading Whether an auth operation is currently running.
 * @param isReauthenticationRequired Whether reauthentication dialog should be shown.
 * @param deleteAccountConfirmationText Current delete account confirmation input value.
 * @param message One-shot UI message.
 */
data class AuthUiState(
  val userName: String = "",
  val email: String = "",
  val password: String = "",
  val repeatedPassword: String = "",
  val originalPassword: String = "",
  val passwordStrength: Int = 0,
  val session: AuthSession = AuthSession.SignedOut,
  val isGuestModeEnabled: Boolean = false,
  val isLoading: Boolean = false,
  val isReauthenticationRequired: Boolean = false,
  val deleteAccountConfirmationText: String = "",
  val message: UiMessage? = null,
  val avatarImageSource: ImageSourceFile? = null,
  val dialogState: AuthDialogState = AuthDialogState.None
)


/**
 * Returns whether this state represents a signed-in user.
 */
val AuthUiState.isSignedIn: Boolean
  get() = session is AuthSession.SignedIn

/**
 * Returns the current signed-in user profile if available.
 */
val AuthUiState.currentProfile: UserProfile?
  get() = when (val currentSession = session) {
    AuthSession.SignedOut -> null
    is AuthSession.SignedIn -> currentSession.profile
  }

/**
 * Returns whether the user can access the app.
 *
 * The app is accessible either with a Firebase account session or in guest mode.
 */
val AuthUiState.canAccessApp: Boolean
  get() = isSignedIn || isGuestModeEnabled