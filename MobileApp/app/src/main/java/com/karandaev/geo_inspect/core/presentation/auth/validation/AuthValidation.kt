package com.karandaev.geo_inspect.core.presentation.auth.validation

import android.util.Patterns

internal object AuthValidation {

  fun requireValidEmail(
    value: String,
    blankMessage: String = "Please enter your email address."
  ): String {
    val email = value.trim()

    require(email.isNotBlank()) {
      blankMessage
    }

    require(Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      "Please enter a valid email address."
    }

    return email
  }

  fun requireStrongRepeatedPassword(
    password: String,
    repeatedPassword: String,
    passwordStrength: Int,
    passwordBlankMessage: String = "Please enter your password.",
    repeatedPasswordBlankMessage: String = "Please repeat your password."
  ) {
    require(password.isNotBlank()) {
      passwordBlankMessage
    }

    require(repeatedPassword.isNotBlank()) {
      repeatedPasswordBlankMessage
    }

    require(password == repeatedPassword) {
      "Passwords do not match."
    }

    require(passwordStrength >= 100) {
      "Password is not strong enough. Password strength must be 100/100."
    }
  }
}