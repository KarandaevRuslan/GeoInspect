package com.karandaev.geo_inspect.core.domain.model.profile

/**
 * Authenticated backend user profile.
 */
data class MeProfile(
  val uid: String,
  val name: String?,
  val email: String?,
  val emailVerified: Boolean,
  val roles: List<String>
)