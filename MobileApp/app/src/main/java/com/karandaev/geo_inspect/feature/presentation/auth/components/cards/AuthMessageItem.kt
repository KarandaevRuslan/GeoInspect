package com.karandaev.geo_inspect.feature.presentation.auth.components.cards

import androidx.compose.foundation.lazy.LazyListScope
import com.karandaev.geo_inspect.core.presentation.auth.model.AuthUiState
import com.karandaev.geo_inspect.core.presentation.message.shouldShowAsTextBlock
import com.karandaev.geo_inspect.core.ui.components.notification.MiniUiMessageCard

/**
 * Adds auth message item to the list when current state contains a message.
 *
 * @param state Current auth UI state.
 * @param onClearMessageClick Called when the user clears the message.
 */
internal fun LazyListScope.authMessageItem(
  state: AuthUiState,
  onClearMessageClick: () -> Unit
) {
  val message = state.message ?: return

  if (!message.shouldShowAsTextBlock) {
    return
  }

  item {
    MiniUiMessageCard(
      message = message.text,
      isError = message.isError,
      onClearClick = onClearMessageClick
    )
  }
}