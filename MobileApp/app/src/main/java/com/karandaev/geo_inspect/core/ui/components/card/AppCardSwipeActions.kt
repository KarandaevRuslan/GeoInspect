package com.karandaev.geo_inspect.core.ui.components.card

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Describes an action displayed behind a swipeable card.
 *
 * The action itself does not perform any work. It only describes how the swipe
 * background should look. Actual callbacks are passed separately to keep the model
 * reusable and UI-focused.
 *
 * @param label Text shown in the swipe background.
 * @param icon Icon shown in the swipe background.
 * @param backgroundColor Background color of the swipe action area.
 * @param contentColor Foreground color used for the icon and label.
 */
data class AppCardSwipeAction(
  val label: String,
  val icon: ImageVector,
  val backgroundColor: Color,
  val contentColor: Color
)