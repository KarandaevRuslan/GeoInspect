package com.karandaev.geo_inspect.core.presentation.auth.model

/**
 * Sensitive operation that should be retried after successful reauthentication.
 */
sealed interface PendingSensitiveOperation {

  /**
   * Password change should be retried.
   */
  data object ChangePassword : PendingSensitiveOperation

  /**
   * Account deletion should be retried.
   */
  data object DeleteAccount : PendingSensitiveOperation
}