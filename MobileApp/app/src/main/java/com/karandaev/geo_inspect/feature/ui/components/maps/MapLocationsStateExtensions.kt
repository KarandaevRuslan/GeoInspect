package com.karandaev.geo_inspect.feature.ui.components.maps

import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import com.karandaev.geo_inspect.core.util.mappers.toMapPoint
import com.karandaev.geo_inspect.core.presentation.location.LocationResolveMode
import com.karandaev.geo_inspect.core.presentation.location.LocationUiState

internal val LocationUiState.currentLocationPoint: MapPoint?
  get() = when (this) {
    LocationUiState.Loading -> null
    is LocationUiState.Success -> data.toMapPoint()
    is LocationUiState.Unknown -> null
  }

internal val LocationUiState.isInitialLocationLoading: Boolean
  get() = when (this) {
    LocationUiState.Loading -> true

    is LocationUiState.Success -> {
      resolveMode == LocationResolveMode.FullLoading
    }

    is LocationUiState.Unknown -> false
  }