package com.karandaev.geo_inspect.feature.presentation.detection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.karandaev.geo_inspect.core.presentation.detection.DetectCompletedResult

/**
 * Navigates away from detection screen after successful detection.
 *
 * The result itself is not delivered through this callback.
 * Create/edit route reads it from shared [DetectCompletedResult] state.
 */
@Composable
internal fun DetectCompletionNavigationEffect(
  completedResult: DetectCompletedResult?,
  onDetectionComplete: () -> Unit
) {
  LaunchedEffect(completedResult?.refreshId) {
    if (completedResult == null) {
      return@LaunchedEffect
    }

    onDetectionComplete()
  }
}