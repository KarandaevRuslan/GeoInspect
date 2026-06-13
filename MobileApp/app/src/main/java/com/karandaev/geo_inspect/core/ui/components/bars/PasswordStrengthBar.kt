package com.karandaev.geo_inspect.core.ui.components.bars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val PasswordStrengthBarSpacing = 6.dp
private val PasswordStrengthScoreCircleSize = 32.dp
private val PasswordStrengthHorizontalSpacing = 8.dp

/**
 * Displays compact horizontal segmented password strength indicator.
 *
 * @param strength Password strength from 0 to 100.
 * @param showText Whether strength label should be shown.
 * @param showScore Whether numeric score should be shown.
 * @param modifier Modifier applied to root content.
 */
@Composable
internal fun PasswordStrengthBar(
  strength: Int,
  showText: Boolean = true,
  showScore: Boolean = true,
  modifier: Modifier = Modifier
) {
  val state = resolvePasswordStrengthState(
    strength = strength
  )

  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(PasswordStrengthBarSpacing)
  ) {
    if (showText) {
      Text(
        text = "Password strength: ${state.level.label}",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(PasswordStrengthHorizontalSpacing),
      verticalAlignment = Alignment.CenterVertically
    ) {
      PasswordStrengthSegments(
        filledSegments = state.filledSegments,
        filledColor = state.level.color,
        orientation = PasswordStrengthSegmentsOrientation.Horizontal,
        modifier = Modifier.weight(1f)
      )

      if (showScore) {
        PasswordStrengthScoreCircle(
          state = state
        )
      }
    }
  }
}

/**
 * Displays compact vertical segmented password strength indicator.
 *
 * Uses no text label. Current score is shown inside the circle below the bar.
 *
 * @param strength Password strength from 0 to 100.
 * @param modifier Modifier applied to root content.
 */
@Composable
internal fun PasswordStrengthVerticalBar(
  strength: Int,
  modifier: Modifier = Modifier
) {
  val state = resolvePasswordStrengthState(
    strength = strength
  )

  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(PasswordStrengthBarSpacing)
  ) {
    PasswordStrengthSegments(
      filledSegments = state.filledSegments,
      filledColor = state.level.color,
      orientation = PasswordStrengthSegmentsOrientation.Vertical
    )

    PasswordStrengthScoreCircle(
      state = state
    )
  }
}

/**
 * Numeric password strength score badge.
 */
@Composable
private fun PasswordStrengthScoreCircle(
  state: PasswordStrengthState
) {
  Surface(
    modifier = Modifier.size(PasswordStrengthScoreCircleSize),
    shape = CircleShape,
    color = state.level.color,
    contentColor = state.level.contentColor
  ) {
    Box(
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = state.normalizedStrength.toString(),
        style = MaterialTheme.typography.labelSmall
      )
    }
  }
}