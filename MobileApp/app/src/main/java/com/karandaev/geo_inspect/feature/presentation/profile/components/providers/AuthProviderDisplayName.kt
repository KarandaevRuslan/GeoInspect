package com.karandaev.geo_inspect.feature.presentation.profile.components.providers

private const val FirebaseProviderId = "firebase"
private const val PasswordProviderId = "password"
private const val GoogleProviderId = "google.com"
private const val PhoneProviderId = "phone"
private const val AnonymousProviderId = "anonymous"

/**
 * Returns user-facing auth provider display name.
 */
internal fun String.toAuthProviderDisplayName(): String {
  return when (this) {
    FirebaseProviderId -> "Firebase"
    PasswordProviderId -> "Email"
    GoogleProviderId -> "Google"
    PhoneProviderId -> "Phone"
    AnonymousProviderId -> "Anonymous"
    else -> this
  }
}