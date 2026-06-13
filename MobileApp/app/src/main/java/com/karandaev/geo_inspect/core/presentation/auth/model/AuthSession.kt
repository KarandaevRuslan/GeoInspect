package com.karandaev.geo_inspect.core.presentation.auth.model


/**
 * Authentication session state.
 */
sealed interface AuthSession {

  /**
   * User is not signed in.
   */
  data object SignedOut : AuthSession

  /**
   * User is signed in.
   *
   * @param profile Current signed-in user profile.
   */
  data class SignedIn(
    val profile: UserProfile
  ) : AuthSession
}
