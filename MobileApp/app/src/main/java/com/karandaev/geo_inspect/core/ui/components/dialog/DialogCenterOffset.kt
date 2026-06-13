package com.karandaev.geo_inspect.core.ui.components.dialog

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp

/**
 * Calculates horizontal dialog offset for landscape system navigation bar.
 *
 * If navigation bar is on the right, content shifts left by half of that inset.
 * If navigation bar is on the left, content shifts right.
 */
@Composable
internal fun rememberDialogCenterOffset(): Dp {
  val density = LocalDensity.current
  val layoutDirection = LocalLayoutDirection.current

  val leftInset = WindowInsets.navigationBars.getLeft(
    density = density,
    layoutDirection = layoutDirection
  )

  val rightInset = WindowInsets.navigationBars.getRight(
    density = density,
    layoutDirection = layoutDirection
  )

  return with(density) {
    ((leftInset - rightInset) / 2).toDp()
  }
}