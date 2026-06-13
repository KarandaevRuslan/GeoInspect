package com.karandaev.geo_inspect.core.ui.components.bars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val PasswordStrengthHorizontalSegmentHeight = 6.dp
private val PasswordStrengthVerticalSegmentWidth = 6.dp
private val PasswordStrengthVerticalSegmentHeight = 18.dp
private val PasswordStrengthSegmentShape = RoundedCornerShape(999.dp)

private val PasswordStrengthHorizontalSegmentSpacing = 4.dp
private val PasswordStrengthVerticalSegmentSpacing = 4.dp

/**
 * Password strength bar orientation.
 */
internal enum class PasswordStrengthSegmentsOrientation {
  Horizontal,
  Vertical
}

/**
 * Shared segmented password strength indicator.
 *
 * Horizontal mode fills segments left-to-right.
 * Vertical mode fills segments bottom-to-top.
 */
@Composable
internal fun PasswordStrengthSegments(
  filledSegments: Int,
  filledColor: Color,
  orientation: PasswordStrengthSegmentsOrientation,
  modifier: Modifier = Modifier
) {
  val emptyColor = MaterialTheme.colorScheme.surfaceVariant

  when (orientation) {
    PasswordStrengthSegmentsOrientation.Horizontal -> {
      PasswordStrengthHorizontalSegments(
        filledSegments = filledSegments,
        filledColor = filledColor,
        emptyColor = emptyColor,
        modifier = modifier
      )
    }

    PasswordStrengthSegmentsOrientation.Vertical -> {
      PasswordStrengthVerticalSegments(
        filledSegments = filledSegments,
        filledColor = filledColor,
        emptyColor = emptyColor,
        modifier = modifier
      )
    }
  }
}

@Composable
private fun PasswordStrengthHorizontalSegments(
  filledSegments: Int,
  filledColor: Color,
  emptyColor: Color,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(PasswordStrengthHorizontalSegmentSpacing)
  ) {
    repeat(PasswordStrengthSegmentCount) { index ->
      val isFilled = index < filledSegments

      PasswordStrengthSegment(
        color = if (isFilled) {
          filledColor
        } else {
          emptyColor
        },
        modifier = Modifier
          .weight(1f)
          .height(PasswordStrengthHorizontalSegmentHeight)
      )
    }
  }
}

@Composable
private fun PasswordStrengthVerticalSegments(
  filledSegments: Int,
  filledColor: Color,
  emptyColor: Color,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(PasswordStrengthVerticalSegmentSpacing)
  ) {
    repeat(PasswordStrengthSegmentCount) { index ->
      val isFilled = index >= PasswordStrengthSegmentCount - filledSegments

      PasswordStrengthSegment(
        color = if (isFilled) {
          filledColor
        } else {
          emptyColor
        },
        modifier = Modifier
          .width(PasswordStrengthVerticalSegmentWidth)
          .height(PasswordStrengthVerticalSegmentHeight)
      )
    }
  }
}

@Composable
private fun PasswordStrengthSegment(
  color: Color,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(PasswordStrengthSegmentShape)
      .background(color)
  )
}