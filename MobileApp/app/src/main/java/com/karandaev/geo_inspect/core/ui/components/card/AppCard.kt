package com.karandaev.geo_inspect.core.ui.components.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape

/**
 * Base application card component.
 *
 * This component is intentionally simple and does not own feature-specific behavior
 * such as swipe actions, navigation, deletion, exporting, or persistence.
 *
 * Feature-specific cards should compose this component instead of extending it with
 * business-specific parameters.
 *
 * @param modifier Modifier applied to the card.
 * @param onClick Optional click callback. If null, the card is not clickable.
 * @param shape Card shape.
 * @param colors Card colors.
 * @param border Optional card border.
 * @param background Optional custom background applied inside the card content area.
 * @param contentPadding Padding applied around the card content.
 * @param content Card content.
 */
@Composable
fun AppCard(
  modifier: Modifier = Modifier,
  onClick: (() -> Unit)? = null,
  shape: Shape = AppCardDefaults.shape,
  colors: CardColors = AppCardDefaults.colors(),
  border: BorderStroke? = AppCardDefaults.border(),
  background: AppCardBackground = AppCardBackground.Default,
  contentPadding: PaddingValues = AppCardDefaults.contentPadding,
  content: @Composable () -> Unit
) {
  val clickableModifier = if (onClick != null) {
    Modifier.clickable(onClick = onClick)
  } else {
    Modifier
  }

  Card(
    modifier = modifier.then(clickableModifier),
    shape = shape,
    colors = colors,
    border = border
  ) {
    val backgroundModifier = when (background) {
      AppCardBackground.Default -> Modifier
      is AppCardBackground.Color -> Modifier.background(background.value)
      is AppCardBackground.Brush -> Modifier.background(background.value)
    }

    Box(
      modifier = backgroundModifier.padding(contentPadding)
    ) {
      content()
    }
  }
}