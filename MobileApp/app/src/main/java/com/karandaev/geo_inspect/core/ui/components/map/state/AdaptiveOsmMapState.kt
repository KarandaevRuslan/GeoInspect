package com.karandaev.geo_inspect.core.ui.components.map.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.karandaev.geo_inspect.core.ui.components.map.marker.model.MarkerPalette
import com.karandaev.geo_inspect.core.ui.components.map.marker.model.MarkerRenderKey

internal class AdaptiveOsmMapState(
  initialFocusRequest: Int
) {
  val lastMarkersKey = mutableStateOf<List<MarkerRenderKey>?>(null)
  val lastPalette = mutableStateOf<MarkerPalette?>(null)
  val lastHandledFocusRequest = mutableIntStateOf(initialFocusRequest)
}

@Composable
internal fun rememberAdaptiveOsmMapState(
  focusRequest: Int
): AdaptiveOsmMapState {
  return remember {
    AdaptiveOsmMapState(
      initialFocusRequest = focusRequest
    )
  }
}