package com.karandaev.geo_inspect.core.ui.components.map.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.osmdroid.views.MapView

/**
 * Connects an OSMDroid [MapView] to the current Compose lifecycle.
 */
@Composable
internal fun MapLifecycleEffect(
  mapView: MapView
) {
  val lifecycleOwner = LocalLifecycleOwner.current

  DisposableEffect(mapView, lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event ->
      when (event) {
        Lifecycle.Event.ON_RESUME -> mapView.onResume()
        Lifecycle.Event.ON_PAUSE -> mapView.onPause()
        else -> Unit
      }
    }

    lifecycleOwner.lifecycle.addObserver(observer)

    onDispose {
      lifecycleOwner.lifecycle.removeObserver(observer)
      mapView.onPause()
      mapView.onDetach()
    }
  }
}