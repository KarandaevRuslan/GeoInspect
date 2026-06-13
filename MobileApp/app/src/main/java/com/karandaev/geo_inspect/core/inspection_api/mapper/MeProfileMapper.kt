package com.karandaev.geo_inspect.core.inspection_api.mapper

import com.karandaev.geo_inspect.core.domain.model.profile.MeProfile
import com.karandaev.geo_inspect.core.inspection_api.dto.profile.MeResponseDto

/**
 * Maps profile DTOs returned by road crack backend to domain models.
 */
fun MeResponseDto.toMeProfile(): MeProfile {
  return MeProfile(
    uid = uid,
    name = name,
    email = email,
    emailVerified = emailVerified,
    roles = roles
  )
}