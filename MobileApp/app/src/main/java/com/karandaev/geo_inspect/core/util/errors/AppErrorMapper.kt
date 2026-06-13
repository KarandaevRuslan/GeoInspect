package com.karandaev.geo_inspect.core.presentation.error

import com.google.firebase.auth.FirebaseAuthException
import com.karandaev.geo_inspect.core.image.validation.exception.ImageValidationErrorCode
import com.karandaev.geo_inspect.core.image.validation.exception.ImageValidationException
import retrofit2.HttpException

private const val RetryAfterHeader = "Retry-After"
private const val SecondsInMinute = 60L
private const val SupportedImageFormats = "JPEG, PNG or WEBP"

/**
 * Converts app-level errors into user-friendly messages.
 */
object AppErrorMapper {

  /**
   * Returns a user-friendly message for the provided error.
   */
  fun toMessage(
    error: Throwable,
    fallbackMessage: String = "Something went wrong. Please try again."
  ): String {
    return when (error) {
      is ImageValidationException -> error.toMessage()
      is FirebaseAuthException -> error.toMessage()
      is HttpException -> error.toMessage()
      is IllegalArgumentException -> error.message ?: "Invalid input."
      is IllegalStateException -> error.message ?: "App state is invalid."
      else -> error.message ?: fallbackMessage
    }
  }

  private fun FirebaseAuthException.toMessage(): String {
    return when (errorCode) {
      "ERROR_INVALID_EMAIL" -> "The email address is not valid."
      "ERROR_USER_NOT_FOUND" -> "No account found for this email."
      "ERROR_WRONG_PASSWORD" -> "Incorrect password. Please try again."
      "ERROR_WEAK_PASSWORD" -> "Password is too weak. Please choose a stronger password."
      "ERROR_USER_TOKEN_EXPIRED" -> "Your session expired. Please sign in again."
      "ERROR_INVALID_USER_TOKEN" -> "Your session is no longer valid. Please sign in again."
      "ERROR_EMAIL_ALREADY_IN_USE" -> "This email is already registered. Try signing in instead."
      "ERROR_TOO_MANY_REQUESTS" -> "Too many attempts. Please wait a bit and try again."
      "ERROR_NETWORK_REQUEST_FAILED" -> "Network error. Please check your internet connection."
      "ERROR_REQUIRES_RECENT_LOGIN" -> {
        "Please sign in again before changing sensitive account settings."
      }

      else -> message ?: "Authentication failed. Please try again."
    }
  }

  private fun ImageValidationException.toMessage(): String {
    return when (code) {
      ImageValidationErrorCode.EmptyFile -> {
        "The selected file is empty. Please choose another image."
      }

      ImageValidationErrorCode.FileTooLarge -> {
        "Image is too large. Max size is 10 MB."
      }

      ImageValidationErrorCode.UnsupportedMediaType -> {
        "Unsupported image format. Please use $SupportedImageFormats."
      }

      ImageValidationErrorCode.InvalidImageDimensions -> {
        "The image dimensions look invalid. Please choose another image."
      }

      ImageValidationErrorCode.ImageDimensionsTooLarge -> {
        "Image dimensions are too large. Please choose a smaller image."
      }

      ImageValidationErrorCode.ImageMegapixelsTooLarge -> {
        "Image resolution is too large. Please choose a smaller image."
      }

      ImageValidationErrorCode.DecodeFailed -> {
        "The image cannot be decoded. Please choose another image."
      }
    }
  }

  private fun HttpException.toMessage(): String {
    return when (code()) {
      400 -> "The request is invalid. Please check your input and try again."
      401, 403 -> "You are not authorized. Please sign in again."
      413 -> "File is too large."
      415 -> "Unsupported file format."
      429 -> rateLimitMessage()
      500 -> "Server error. Please try again later."
      502, 503, 504 -> "Service is temporarily unavailable. Please try again later."
      else -> "Request failed (HTTP ${code()}). Please try again."
    }
  }

  private fun HttpException.rateLimitMessage(): String {
    val retryAfterSeconds = response()
      ?.headers()
      ?.get(RetryAfterHeader)
      ?.toLongOrNull()

    return if (retryAfterSeconds != null) {
      "Too many requests. Please try again in ${retryAfterSeconds.toReadableDelay()}."
    } else {
      "Too many requests. Please wait a bit and try again."
    }
  }

  private fun Long.toReadableDelay(): String {
    return when {
      this <= 1L -> "1 second"
      this < SecondsInMinute -> "$this seconds"
      this == SecondsInMinute -> "1 minute"
      else -> {
        val minutes = this / SecondsInMinute
        val seconds = this % SecondsInMinute

        if (seconds == 0L) {
          "$minutes minutes"
        } else {
          "$minutes min $seconds sec"
        }
      }
    }
  }
}