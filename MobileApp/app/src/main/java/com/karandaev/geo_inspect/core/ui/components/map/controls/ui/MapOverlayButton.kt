package com.karandaev.geo_inspect.core.ui.components.map.controls.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

private const val PRESS_REPEAT_START_DELAY_MILLIS = 350L
private const val PRESS_REPEAT_INTERVAL_MILLIS = 90L

/**
 * Reusable circular overlay button for map controls.
 *
 * Supports:
 * - regular click;
 * - optional long click;
 * - optional repeated action while the button is pressed.
 *
 * Repeated press action is useful for zoom controls. Long click remains useful for
 * controls like focus menu opening.
 */
@Composable
internal fun MapOverlayButton(
  text: String,
  modifier: Modifier = Modifier,
  size: Dp,
  onClick: () -> Unit,
  onLongClick: (() -> Unit)? = null,
  onPressRepeat: (() -> Unit)? = null
) {
  val currentOnClick = rememberUpdatedState(onClick)
  val currentOnLongClick = rememberUpdatedState(onLongClick)
  val currentOnPressRepeat = rememberUpdatedState(onPressRepeat)

  Surface(
    modifier = modifier
      .size(size)
      .pointerInput(Unit) {
        detectTapGestures(
          onTap = {
            currentOnClick.value()
          },
          onLongPress = {
            if (currentOnPressRepeat.value == null) {
              currentOnLongClick.value?.invoke()
            }
          },
          onPress = {
            val repeatAction = currentOnPressRepeat.value

            if (repeatAction == null) {
              tryAwaitRelease()
              return@detectTapGestures
            }

            coroutineScope {
              val repeatJob = launch {
                delay(PRESS_REPEAT_START_DELAY_MILLIS)

                while (isActive) {
                  repeatAction()
                  delay(PRESS_REPEAT_INTERVAL_MILLIS)
                }
              }

              tryAwaitRelease()
              repeatJob.cancel()
            }
          }
        )
      },
    shape = RoundedCornerShape(size / 2),
    color = MaterialTheme.colorScheme.surface,
    contentColor = MaterialTheme.colorScheme.primary,
    tonalElevation = 4.dp,
    shadowElevation = 4.dp
  ) {
    Box(
      contentAlignment = Center
    ) {
      Text(
        text = text,
        style = MaterialTheme.typography.titleLarge
      )
    }
  }
}