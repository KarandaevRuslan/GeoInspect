package com.karandaev.geo_inspect.core.presentation.auth

import com.karandaev.geo_inspect.core.presentation.error.AppErrorMapper

/**
 * Converts authentication errors into user-friendly messages.
 */
internal object AuthErrorMapper {

  /**
   * Returns a user-friendly message for the provided authentication error.
   */
  fun toMessage(error: Throwable): String {
    return AppErrorMapper.toMessage(
      error = error,
      fallbackMessage = "Authentication failed. Please try again."
    )
  }
}