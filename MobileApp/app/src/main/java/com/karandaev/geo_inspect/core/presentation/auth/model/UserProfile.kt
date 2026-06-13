package com.karandaev.geo_inspect.core.presentation.auth.model

/**
 * User profile data displayed by the UI.
 *
 * @param uid Firebase user ID.
 * @param email User email address.
 * @param displayName User display name.
 * @param photoUrl User profile photo URL currently displayed by UI.
 * @param originalPhotoUrl Original provider/Firebase profile photo URL used as fallback.
 * @param emailVerified Whether the email address is verified.
 * @param providerIds Authentication provider IDs linked to the user.
 */
data class UserProfile(
  val uid: String,
  val email: String?,
  val displayName: String?,
  val photoUrl: String?,
  val originalPhotoUrl: String?,
  val emailVerified: Boolean,
  val providerIds: List<String>
)

/**
 * Returns the photo URL that should be displayed by the UI.
 *
 * Custom/backend photoUrl has priority. If it is missing or blank,
 * original provider/Firebase photo URL is used as fallback.
 */
val UserProfile.effectivePhotoUrl: String?
  get() = photoUrl
    ?.takeIf { it.isNotBlank() }
    ?: originalPhotoUrl?.takeIf { it.isNotBlank() }