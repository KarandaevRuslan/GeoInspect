package com.karandaev.geo_inspect.app.adaptive_layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class AdaptiveLayoutType {
  SinglePane,
  TwoPane
}

@Composable
fun rememberAdaptiveLayoutType(
  maxWidth: Dp,
  maxHeight: Dp,
  mediumWidthThreshold: Dp = 600.dp
): AdaptiveLayoutType {
  return remember(maxWidth, maxHeight, mediumWidthThreshold) {
    val isLandscape = maxWidth > maxHeight
    val isAtLeastMediumWidth = maxWidth >= mediumWidthThreshold

    if (isLandscape || isAtLeastMediumWidth) {
      AdaptiveLayoutType.TwoPane
    } else {
      AdaptiveLayoutType.SinglePane
    }
  }
}