package com.karandaev.geo_inspect.core.domain.model.persisted

import com.karandaev.geo_inspect.core.domain.model.location.MapPoint

data class LastKnownLocation(
  val point: MapPoint,
  val placeName: String,
  val updatedAt: Long
) {
  val latitude: Double
    get() = point.latitude

  val longitude: Double
    get() = point.longitude
}