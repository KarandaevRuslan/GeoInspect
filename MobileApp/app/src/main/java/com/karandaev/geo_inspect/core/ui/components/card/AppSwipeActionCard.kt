@file:OptIn(ExperimentalMaterial3Api::class)

package com.karandaev.geo_inspect.core.ui.components.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * Generic swipeable card wrapper.
 *
 * This component only provides reusable swipe behavior. It does not know anything
 * about notes, exporting, deleting, repositories, navigation, or persistence.
 *
 * The swipe background uses the same [shape] as the foreground card, so actions
 * visually match rounded card styles.
 *
 * @param modifier Modifier applied to the swipe container.
 * @param shape Shape used by the swipe background.
 * @param startToEndAction Optional action shown when swiping from start to end.
 * @param endToStartAction Optional action shown when swiping from end to start.
 * @param onStartToEnd Called when the start-to-end swipe threshold is reached.
 * @param onEndToStart Called when the end-to-start swipe threshold is reached.
 * @param content Swipeable foreground content.
 */
@Composable
fun AppSwipeActionCard(
  modifier: Modifier = Modifier,
  shape: Shape = AppCardDefaults.shape,
  startToEndAction: AppCardSwipeAction? = null,
  endToStartAction: AppCardSwipeAction? = null,
  onStartToEnd: (() -> Unit)? = null,
  onEndToStart: (() -> Unit)? = null,
  content: @Composable () -> Unit
) {
  val dismissState = rememberSwipeToDismissBoxState(
    confirmValueChange = { value ->
      when (value) {
        SwipeToDismissBoxValue.StartToEnd -> {
          onStartToEnd?.invoke()
          false
        }

        SwipeToDismissBoxValue.EndToStart -> {
          onEndToStart?.invoke()
          false
        }

        SwipeToDismissBoxValue.Settled -> false
      }
    },
    positionalThreshold = { distance ->
      distance * 0.35f
    }
  )

  SwipeToDismissBox(
    modifier = modifier.clip(shape),
    state = dismissState,
    enableDismissFromStartToEnd = startToEndAction != null && onStartToEnd != null,
    enableDismissFromEndToStart = endToStartAction != null && onEndToStart != null,
    backgroundContent = {
      AppSwipeActionBackground(
        dismissValue = dismissState.dismissDirection,
        shape = shape,
        startToEndAction = startToEndAction,
        endToStartAction = endToStartAction
      )
    },
    content = {
      content()
    }
  )
}

/**
 * Draws the rounded background displayed behind the foreground card during swipe.
 *
 * @param dismissValue Current swipe direction.
 * @param shape Shape used to clip the swipe background.
 * @param startToEndAction Action shown for start-to-end swipe.
 * @param endToStartAction Action shown for end-to-start swipe.
 */
@Composable
private fun AppSwipeActionBackground(
  dismissValue: SwipeToDismissBoxValue,
  shape: Shape,
  startToEndAction: AppCardSwipeAction?,
  endToStartAction: AppCardSwipeAction?
) {
  val action = when (dismissValue) {
    SwipeToDismissBoxValue.StartToEnd -> startToEndAction
    SwipeToDismissBoxValue.EndToStart -> endToStartAction
    SwipeToDismissBoxValue.Settled -> null
  }

  val horizontalArrangement = when (dismissValue) {
    SwipeToDismissBoxValue.StartToEnd -> Arrangement.Start
    SwipeToDismissBoxValue.EndToStart -> Arrangement.End
    SwipeToDismissBoxValue.Settled -> Arrangement.Start
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .clip(shape)
      .background(action?.backgroundColor ?: MaterialTheme.colorScheme.surface)
  ) {
    AnimatedVisibility(
      visible = action != null,
      modifier = Modifier.fillMaxSize()
    ) {
      if (action != null) {
        Row(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
          horizontalArrangement = horizontalArrangement,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = action.icon,
            contentDescription = action.label,
            tint = action.contentColor,
            modifier = Modifier.size(24.dp)
          )

          Text(
            text = action.label,
            color = action.contentColor,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 8.dp)
          )
        }
      }
    }
  }
}