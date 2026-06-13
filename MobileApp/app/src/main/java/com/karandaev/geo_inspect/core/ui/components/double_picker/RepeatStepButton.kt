package com.karandaev.geo_inspect.core.ui.components.double_picker

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val InitialRepeatDelayMillis = 350L
private const val RepeatIntervalMillis = 60L

/**
 * Circular button that repeatedly invokes [onStep] while pressed.
 *
 * The first step is executed immediately on press. If the press is held, additional steps are
 * emitted after [InitialRepeatDelayMillis] and then every [RepeatIntervalMillis].
 *
 * @param text Text displayed inside the button.
 * @param onStep Callback invoked for every step action.
 * @param modifier Modifier applied to the button surface.
 * @param enabled Whether the button can receive press gestures.
 */
@Composable
fun RepeatStepButton(
  text: String,
  onStep: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true
) {
  val currentOnStep by rememberUpdatedState(onStep)

  Surface(
    modifier = modifier
      .size(44.dp)
      .then(
        if (enabled) {
          Modifier.pointerInput(Unit) {
            detectTapGestures(
              onPress = {
                currentOnStep()

                coroutineScope {
                  val repeatJob = launch {
                    delay(InitialRepeatDelayMillis)

                    while (true) {
                      currentOnStep()
                      delay(RepeatIntervalMillis)
                    }
                  }

                  try {
                    tryAwaitRelease()
                  } finally {
                    repeatJob.cancel()
                  }
                }
              }
            )
          }
        } else {
          Modifier
        }
      ),
    shape = RoundedCornerShape(22.dp),
    color = if (enabled) {
      MaterialTheme.colorScheme.primaryContainer
    } else {
      MaterialTheme.colorScheme.surfaceVariant
    },
    contentColor = if (enabled) {
      MaterialTheme.colorScheme.onPrimaryContainer
    } else {
      MaterialTheme.colorScheme.onSurfaceVariant
    }
  ) {
    Box(
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = text,
        style = MaterialTheme.typography.titleLarge
      )
    }
  }
}