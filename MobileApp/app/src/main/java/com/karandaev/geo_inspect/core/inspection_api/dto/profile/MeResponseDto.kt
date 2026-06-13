package com.karandaev.geo_inspect.core.inspection_api.dto.profile

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Current authenticated Firebase user returned by /v1/me.
 */
@JsonClass(generateAdapter = true)
data class MeResponseDto(
  @Json(name = "uid")
  val uid: String,

  @Json(name = "name")
  val name: String?,

  @Json(name = "email")
  val email: String?,

  @Json(name = "emailVerified")
  val emailVerified: Boolean,

  @Json(name = "roles")
  val roles: List<String> = emptyList()
)