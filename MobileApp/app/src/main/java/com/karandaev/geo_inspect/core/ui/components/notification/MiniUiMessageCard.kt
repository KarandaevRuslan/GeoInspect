package com.karandaev.geo_inspect.core.ui.components.notification

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.core.presentation.message.UiMessage
import com.karandaev.geo_inspect.core.presentation.message.shouldShowAsTextBlock

private val AuthMessageCardSideSlotSize = 32.dp
private val AuthMessageCardIconSize = 23.dp
private val AuthMessageCardClearIconSize = 23.dp

/**
 * Displays dialog message when it should be rendered as an inline text block.
 *
 * @param message Current auth UI message.
 * @param onClearMessageClick Called when message clear button is clicked.
 * @param modifier Modifier applied to the message card.
 */
@Composable
internal fun MiniUiMessageCard(
  message: UiMessage?,
  onClearMessageClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  if (message == null || !message.shouldShowAsTextBlock) {
    return
  }

  MiniUiMessageCard(
    message = message.text,
    isError = message.isError,
    onClearClick = onClearMessageClick,
    modifier = modifier
  )
}

/**
 * Displays auth info or error message.
 *
 * @param message Message text.
 * @param isError Whether message should use error styling.
 * @param onClearClick Called when the clear button is clicked.
 * @param modifier Modifier applied to the card.
 */
@Composable
internal fun MiniUiMessageCard(
  message: String,
  isError: Boolean,
  onClearClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val containerColor = if (isError) {
    MaterialTheme.colorScheme.errorContainer
  } else {
    MaterialTheme.colorScheme.primaryContainer
  }

  val contentColor = if (isError) {
    MaterialTheme.colorScheme.onErrorContainer
  } else {
    MaterialTheme.colorScheme.onPrimaryContainer
  }

  val accentColor = if (isError) {
    MaterialTheme.colorScheme.error
  } else {
    MaterialTheme.colorScheme.primary
  }

  Card(
    modifier = modifier.fillMaxWidth(),
    shape = MaterialTheme.shapes.medium,
    colors = CardDefaults.cardColors(
      containerColor = containerColor,
      contentColor = contentColor
    ),
    border = BorderStroke(
      width = 1.dp,
      color = accentColor.copy(alpha = 0.28f)
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(
          start = 10.dp,
          top = 12.dp,
          end = 6.dp,
          bottom = 12.dp
        ),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier.size(AuthMessageCardSideSlotSize),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = if (isError) {
            Icons.Default.ErrorOutline
          } else {
            Icons.Default.Info
          },
          contentDescription = null,
          tint = accentColor,
          modifier = Modifier.size(AuthMessageCardIconSize)
        )
      }

      Text(
        text = message,
        style = MaterialTheme.typography.bodyMedium,
        color = contentColor,
        modifier = Modifier.weight(1f)
      )

      IconButton(
        onClick = onClearClick,
        modifier = Modifier.size(AuthMessageCardSideSlotSize)
      ) {
        Icon(
          imageVector = Icons.Default.Close,
          contentDescription = "Dismiss message",
          tint = contentColor.copy(alpha = 0.78f),
          modifier = Modifier.size(AuthMessageCardClearIconSize)
        )
      }
    }
  }
}