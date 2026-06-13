package com.karandaev.geo_inspect.core.presentation.detection

import com.karandaev.geo_inspect.core.presentation.error.AppErrorMapper

/**
 * Converts detection errors into user-friendly messages.
 */
internal object DetectErrorMapper {

  /**
   * Returns a user-friendly message for the provided detection error.
   */
  fun toMessage(error: Throwable): String {
    return AppErrorMapper.toMessage(
      error = error,
      fallbackMessage = "Detection failed. Please try again."
    )
  }
}