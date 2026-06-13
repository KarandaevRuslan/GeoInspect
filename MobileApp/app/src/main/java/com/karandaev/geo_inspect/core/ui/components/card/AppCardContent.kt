package com.karandaev.geo_inspect.core.ui.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AppCardContentHorizontalSpacing = 14.dp
private val AppCardContentTextVerticalSpacing = 3.dp
private val AppCardContentSubtitleLineHeight = 20.sp
private val AppCardContentSupportingTextLineHeight = 16.sp

/**
 * Unified card content layout.
 *
 * Structure is always the same:
 * leading content -> text column -> trailing content.
 *
 * Feature cards should customize this component through parameters,
 * not by creating separate unrelated layouts.
 */
@Composable
fun AppCardContent(
  title: String,
  modifier: Modifier = Modifier,
  subtitle: String? = null,
  supportingText: String? = null,
  leadingContent: (@Composable () -> Unit)? = null,
  trailingContent: (@Composable () -> Unit)? = null,
  verticalAlignment: Alignment.Vertical = Alignment.Top,
  horizontalSpacing: Dp = AppCardContentHorizontalSpacing,
  textVerticalSpacing: Dp = AppCardContentTextVerticalSpacing,
  titleStyle: TextStyle = MaterialTheme.typography.titleMedium,
  subtitleStyle: TextStyle = MaterialTheme.typography.bodyMedium,
  supportingTextStyle: TextStyle = MaterialTheme.typography.bodySmall,
  titleColor: Color = MaterialTheme.colorScheme.onSurface,
  subtitleColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
  supportingTextColor: Color = MaterialTheme.colorScheme.outline,
  titleMaxLines: Int = 1,
  subtitleMaxLines: Int = 2,
  supportingTextMaxLines: Int = 1
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
    verticalAlignment = verticalAlignment
  ) {
    if (leadingContent != null) {
      leadingContent()
    }

    Column(
      modifier = Modifier.weight(1f),
      verticalArrangement = Arrangement.spacedBy(textVerticalSpacing)
    ) {
      Text(
        text = title,
        style = titleStyle,
        color = titleColor,
        maxLines = titleMaxLines,
        overflow = TextOverflow.Ellipsis
      )

      if (!subtitle.isNullOrBlank()) {
        Text(
          text = subtitle,
          style = subtitleStyle.copy(
            lineHeight = 17.sp
          ),
          color = subtitleColor,
          maxLines = subtitleMaxLines,
          overflow = TextOverflow.Ellipsis
        )
      }

      if (!supportingText.isNullOrBlank()) {
        Text(
          text = supportingText,
          style = supportingTextStyle.withDefaultLineHeight(
            defaultLineHeight = AppCardContentSupportingTextLineHeight
          ),
          color = supportingTextColor,
          maxLines = supportingTextMaxLines,
          overflow = TextOverflow.Ellipsis
        )
      }
    }

    if (trailingContent != null) {
      trailingContent()
    }
  }
}

/**
 * Adds default line height only when caller style does not define it.
 */
private fun TextStyle.withDefaultLineHeight(
  defaultLineHeight: TextUnit
): TextStyle {
  return if (lineHeight == TextUnit.Unspecified) {
    copy(lineHeight = defaultLineHeight)
  } else {
    this
  }
}