package com.karandaev.geo_inspect.feature.presentation.profile.components.copy

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import com.karandaev.geo_inspect.core.util.other.toast

/**
 * Container that copies provided text to clipboard when clicked.
 *
 * @param copyText Text copied to clipboard.
 * @param copiedMessage Toast message shown after copying.
 * @param enabled Whether copy action is enabled.
 * @param modifier Modifier applied to the container.
 * @param content Copyable content.
 */
@Composable
internal fun ProfileCopyableContainer(
  copyText: String,
  copiedMessage: String,
  enabled: Boolean,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current

  androidx.compose.foundation.layout.Box(
    modifier = modifier.clickable(
      enabled = enabled
    ) {
      clipboardManager.setText(
        AnnotatedString(copyText)
      )

      context.toast(copiedMessage)
    }
  ) {
    content()
  }
}