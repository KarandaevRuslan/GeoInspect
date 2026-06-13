package com.karandaev.geo_inspect.core.presentation.auth.model

/**
 * UI state for authentication-related dialogs.
 */
sealed interface AuthDialogState {

  /**
   * No dialog is currently shown.
   */
  data object None : AuthDialogState

  /**
   * Change display name dialog is currently shown.
   */
  data object ChangeUserName : AuthDialogState

  /**
   * Change password dialog is currently shown.
   */
  data object ChangePassword : AuthDialogState

  /**
   * Change profile avatar dialog is currently shown.
   */
  data object ChangeAvatar : AuthDialogState

  /**
   * Delete account confirmation dialog is currently shown.
   */
  data object DeleteAccount : AuthDialogState
}