package com.karandaev.geo_inspect.core.presentation.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private val MapLocationPollingInterval = 30.seconds

/**
 * Polls current location while the map screen is resumed.
 */
@Composable
internal fun MapLocationPollingEffect(
  pollLocation: () -> Unit,
  interval: Duration = MapLocationPollingInterval
) {
  val lifecycleOwner = LocalLifecycleOwner.current

  DisposableEffect(lifecycleOwner, pollLocation, interval) {
    var pollingJob: Job? = null

    fun startPolling() {
      if (pollingJob?.isActive == true) return

      pollingJob = CoroutineScope(Dispatchers.Main.immediate).launch {
        while (isActive) {
          delay(interval)
          pollLocation()
        }
      }
    }

    fun stopPolling() {
      pollingJob?.cancel()
      pollingJob = null
    }

    val observer = LifecycleEventObserver { _, event ->
      when (event) {
        Lifecycle.Event.ON_RESUME -> startPolling()

        Lifecycle.Event.ON_PAUSE,
        Lifecycle.Event.ON_STOP,
        Lifecycle.Event.ON_DESTROY -> stopPolling()

        else -> Unit
      }
    }

    lifecycleOwner.lifecycle.addObserver(observer)

    if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
      startPolling()
    }

    onDispose {
      stopPolling()
      lifecycleOwner.lifecycle.removeObserver(observer)
    }
  }
}