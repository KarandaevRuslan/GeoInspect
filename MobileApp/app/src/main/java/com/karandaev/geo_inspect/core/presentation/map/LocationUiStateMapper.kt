package com.karandaev.geo_inspect.core.presentation.map

import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import com.karandaev.geo_inspect.core.presentation.location.LocationUiState

fun LocationUiState.toMapPointOrNull(): MapPoint? {
  return when (this) {
    LocationUiState.Loading -> null

    is LocationUiState.Success -> {
      MapPoint(
        latitude = data.latitude,
        longitude = data.longitude
      )
    }

    is LocationUiState.Unknown -> null
  }
}