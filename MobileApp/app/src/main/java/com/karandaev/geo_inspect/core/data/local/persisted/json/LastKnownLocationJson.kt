package com.karandaev.geo_inspect.core.data.local.persisted.json

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class LastKnownLocationJson(
  val latitude: Double,
  val longitude: Double,
  val placeName: String,
  val updatedAt: Long
)