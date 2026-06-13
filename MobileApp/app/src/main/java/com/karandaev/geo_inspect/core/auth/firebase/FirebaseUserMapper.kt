package com.karandaev.geo_inspect.core.auth.firebase

import com.google.firebase.auth.FirebaseUser
import com.karandaev.geo_inspect.core.presentation.auth.model.UserProfile

/**
 * Converts a Firebase user into a UI profile model.
 */
internal fun FirebaseUser.toUserProfile(): UserProfile {
  val providers = providerData
    .mapNotNull { provider -> provider.providerId }
    .filter { providerId -> providerId.isNotBlank() }
    .distinct()

  val providerPhotoUrl = providerData
    .firstOrNull { provider ->
      provider.photoUrl != null && provider.providerId != "firebase"
    }
    ?.photoUrl
    ?.toString()

  val firebasePhotoUrl = photoUrl?.toString()

  val originalPhotoUrl = providerPhotoUrl ?: firebasePhotoUrl

  return UserProfile(
    uid = uid,
    email = email,
    displayName = displayName,
    photoUrl = firebasePhotoUrl,
    originalPhotoUrl = originalPhotoUrl,
    emailVerified = isEmailVerified,
    providerIds = providers
  )
}