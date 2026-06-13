package com.karandaev.geo_inspect.core.presentation.message

/**
 * Describes how a one-shot UI message should be displayed.
 */
enum class UiMessageDisplayMode {

  /**
   * Message should be rendered as an inline text block.
   */
  TextBlock,

  /**
   * Message should be shown as a toast only.
   */
  Toast,

  /**
   * Message should be rendered as an inline text block and also shown as a toast.
   */
  TextBlockAndToast
}

/**
 * One-shot UI message.
 *
 * [refreshId] changes after every new message, even if the text is the same.
 *
 * @param text User-friendly message text.
 * @param isError Whether this message represents an error.
 * @param displayMode Describes where this message should be displayed.
 * @param refreshId Monotonic id of emitted messages.
 */
data class UiMessage(
  val text: String,
  val isError: Boolean,
  val displayMode: UiMessageDisplayMode = UiMessageDisplayMode.TextBlock,
  val refreshId: Long = 0L
) {

  companion object {

    /**
     * Creates informational UI message.
     */
    fun info(
      text: String,
      displayMode: UiMessageDisplayMode = UiMessageDisplayMode.TextBlock,
      refreshId: Long = 0L
    ): UiMessage {
      return UiMessage(
        text = text,
        isError = false,
        displayMode = displayMode,
        refreshId = refreshId
      )
    }

    /**
     * Creates error UI message.
     */
    fun error(
      text: String,
      displayMode: UiMessageDisplayMode = UiMessageDisplayMode.TextBlock,
      refreshId: Long = 0L
    ): UiMessage {
      return UiMessage(
        text = text,
        isError = true,
        displayMode = displayMode,
        refreshId = refreshId
      )
    }
  }
}

/**
 * Returns whether this message should be rendered as an inline text block.
 */
val UiMessage.shouldShowAsTextBlock: Boolean
  get() = displayMode == UiMessageDisplayMode.TextBlock ||
    displayMode == UiMessageDisplayMode.TextBlockAndToast

/**
 * Returns whether this message should be shown as a toast.
 */
val UiMessage.shouldShowAsToast: Boolean
  get() = displayMode == UiMessageDisplayMode.Toast ||
    displayMode == UiMessageDisplayMode.TextBlockAndToast