package com.karandaev.geo_inspect.feature.presentation.auth.navigation

import com.karandaev.geo_inspect.app.navigation.Routes
import com.karandaev.geo_inspect.feature.presentation.auth.model.AuthMode

/**
 * Checks whether the route belongs to auth flow.
 */
fun String?.isAuth(): Boolean {
  return this in setOf(
    Routes.AUTH_LOGIN,
    Routes.AUTH_REGISTER,
    Routes.AUTH_PASSWORD_RESET
  )
}

/**
 * Maps app-level auth routes to auth screen modes.
 */
fun String?.toAuthModeOrNull(): AuthMode? {
  return when (this) {
    Routes.AUTH_LOGIN -> AuthMode.Login
    Routes.AUTH_REGISTER -> AuthMode.Register
    Routes.AUTH_PASSWORD_RESET -> AuthMode.PasswordReset
    else -> null
  }
}