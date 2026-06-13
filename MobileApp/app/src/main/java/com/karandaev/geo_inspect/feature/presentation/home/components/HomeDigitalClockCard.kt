package com.karandaev.geo_inspect.feature.presentation.home.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlinx.coroutines.delay

private val HomeDigitalClockCardHorizontalPadding = 14.dp
private val HomeDigitalClockCardVerticalPadding = 12.dp
private val HomeDigitalClockCardSpacing = 4.dp
private val HomeDigitalClockAnalogSize = 72.dp

private const val HomeDigitalClockRefreshDelayMillis = 1_000L
private const val HomeAnalogClockTickCount = 12

/**
 * Digital clock card for landscape home layout.
 */
@Composable
internal fun HomeDigitalClockCard(
  modifier: Modifier = Modifier
) {
  val locale = Locale.getDefault()

  val timeFormatter = remember(locale) {
    SimpleDateFormat("HH:mm:ss", locale)
  }

  val dateFormatter = remember(locale) {
    SimpleDateFormat("EEE, d MMM", locale)
  }

  val clockState by produceState(
    initialValue = createHomeDigitalClockState(
      timeFormatter = timeFormatter,
      dateFormatter = dateFormatter
    ),
    key1 = timeFormatter,
    key2 = dateFormatter
  ) {
    while (true) {
      value = createHomeDigitalClockState(
        timeFormatter = timeFormatter,
        dateFormatter = dateFormatter
      )

      delay(HomeDigitalClockRefreshDelayMillis)
    }
  }

  val clockContentDescription = stringResource(
    id = R.string.home_digital_clock_content_description,
    clockState.timeText
  )

  Surface(
    modifier = modifier
      .fillMaxWidth()
      .semantics {
        contentDescription = clockContentDescription
      },
    shape = MaterialTheme.shapes.large,
    color = MaterialTheme.colorScheme.surfaceContainer,
    tonalElevation = 1.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(
          horizontal = HomeDigitalClockCardHorizontalPadding,
          vertical = HomeDigitalClockCardVerticalPadding
        ),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(HomeDigitalClockCardSpacing)
      ) {
        Text(
          text = stringResource(R.string.home_digital_clock_title),
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
          text = clockState.timeText,
          style = MaterialTheme.typography.headlineLarge,
          color = MaterialTheme.colorScheme.onSurface,
          fontFamily = FontFamily.Monospace
        )

        Text(
          text = clockState.dateText,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      HomeAnalogClock(
        hour = clockState.hour,
        minute = clockState.minute,
        second = clockState.second
      )
    }
  }
}

/**
 * Compact analog clock for the digital clock card.
 */
@Composable
private fun HomeAnalogClock(
  hour: Int,
  minute: Int,
  second: Int,
  modifier: Modifier = Modifier
) {
  val outlineColor = MaterialTheme.colorScheme.outlineVariant
  val tickColor = MaterialTheme.colorScheme.onSurfaceVariant
  val hourHandColor = MaterialTheme.colorScheme.onSurface
  val minuteHandColor = MaterialTheme.colorScheme.onSurface
  val secondHandColor = MaterialTheme.colorScheme.primary
  val centerColor = MaterialTheme.colorScheme.primary

  Canvas(
    modifier = modifier.size(HomeDigitalClockAnalogSize)
  ) {
    val radius = size.minDimension / 2f
    val center = Offset(
      x = size.width / 2f,
      y = size.height / 2f
    )

    drawCircle(
      color = outlineColor,
      radius = radius - 1.dp.toPx(),
      style = Stroke(
        width = 1.dp.toPx()
      )
    )

    repeat(HomeAnalogClockTickCount) { index ->
      val angle = index.toClockAngle(
        total = HomeAnalogClockTickCount
      )

      val outer = center.pointOnCircle(
        radius = radius - 7.dp.toPx(),
        angle = angle
      )

      val inner = center.pointOnCircle(
        radius = radius - 12.dp.toPx(),
        angle = angle
      )

      drawLine(
        color = tickColor,
        start = inner,
        end = outer,
        strokeWidth = 1.4.dp.toPx(),
        cap = StrokeCap.Round
      )
    }

    drawClockHand(
      color = hourHandColor,
      center = center,
      angle = hour.toHourAngle(
        minute = minute
      ),
      length = radius * 0.44f,
      width = 3.dp.toPx()
    )

    drawClockHand(
      color = minuteHandColor,
      center = center,
      angle = minute.toMinuteAngle(
        second = second
      ),
      length = radius * 0.64f,
      width = 2.dp.toPx()
    )

    drawClockHand(
      color = secondHandColor,
      center = center,
      angle = second.toClockAngle(
        total = 60
      ),
      length = radius * 0.7f,
      width = 1.dp.toPx()
    )

    drawCircle(
      color = centerColor,
      radius = 3.2.dp.toPx(),
      center = center
    )
  }
}

/**
 * UI-ready digital clock state.
 */
private data class HomeDigitalClockState(
  val timeText: String,
  val dateText: String,
  val hour: Int,
  val minute: Int,
  val second: Int
)

/**
 * Creates current clock state.
 */
private fun createHomeDigitalClockState(
  timeFormatter: SimpleDateFormat,
  dateFormatter: SimpleDateFormat
): HomeDigitalClockState {
  val calendar = Calendar.getInstance()
  val currentDate = calendar.time

  return HomeDigitalClockState(
    timeText = timeFormatter.format(currentDate),
    dateText = dateFormatter.format(currentDate),
    hour = calendar.get(Calendar.HOUR_OF_DAY),
    minute = calendar.get(Calendar.MINUTE),
    second = calendar.get(Calendar.SECOND)
  )
}

/**
 * Converts hour value to analog clock angle.
 */
private fun Int.toHourAngle(
  minute: Int
): Float {
  val hourProgress = (this % 12 + minute / 60f) / 12f

  return progressToClockAngle(
    progress = hourProgress
  )
}

/**
 * Converts minute value to analog clock angle.
 */
private fun Int.toMinuteAngle(
  second: Int
): Float {
  val minuteProgress = (this + second / 60f) / 60f

  return progressToClockAngle(
    progress = minuteProgress
  )
}

/**
 * Converts indexed clock value to analog clock angle.
 */
private fun Int.toClockAngle(
  total: Int
): Float {
  return progressToClockAngle(
    progress = this / total.toFloat()
  )
}

/**
 * Converts progress to canvas angle where 12 o'clock is at the top.
 */
private fun progressToClockAngle(
  progress: Float
): Float {
  return (2f * PI.toFloat() * progress) - (PI.toFloat() / 2f)
}

/**
 * Calculates point on a circle by angle.
 */
private fun Offset.pointOnCircle(
  radius: Float,
  angle: Float
): Offset {
  return Offset(
    x = x + cos(angle) * radius,
    y = y + sin(angle) * radius
  )
}

/**
 * Draws a single analog clock hand.
 */
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawClockHand(
  color: Color,
  center: Offset,
  angle: Float,
  length: Float,
  width: Float
) {
  drawLine(
    color = color,
    start = center,
    end = center.pointOnCircle(
      radius = length,
      angle = angle
    ),
    strokeWidth = width,
    cap = StrokeCap.Round
  )
}