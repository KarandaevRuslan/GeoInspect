package com.karandaev.geo_inspect.core.inspection_api.dto.profile

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Avatar response returned by the backend.
 *
 * @param avatarUrl Public avatar URL, or null when user has no custom avatar.
 */
@JsonClass(generateAdapter = true)
data class AvatarResponseDto(
  @Json(name = "avatarUrl")
  val avatarUrl: String?
)