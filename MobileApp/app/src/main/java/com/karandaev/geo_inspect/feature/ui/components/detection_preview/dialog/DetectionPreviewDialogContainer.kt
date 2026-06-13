package com.karandaev.geo_inspect.feature.ui.components.detection_preview.dialog

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.karandaev.geo_inspect.core.ui.components.dialog.rememberDialogCenterOffset

/**
 * Shared fullscreen dialog container for detection preview dialogs.
 *
 * Content is visually centered between left and right navigation insets.
 *
 * @param onDismissRequest Called when dialog should be dismissed.
 * @param content Dialog content.
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
internal fun DetectionPreviewDialogContainer(
  onDismissRequest: () -> Unit,
  content: @Composable BoxWithConstraintsScope.() -> Unit
) {
  val backgroundInteractionSource = remember {
    MutableInteractionSource()
  }

  val dialogCenterOffset = rememberDialogCenterOffset()

  Dialog(
    onDismissRequest = onDismissRequest,
    properties = DialogProperties(
      usePlatformDefaultWidth = false,
      dismissOnBackPress = true,
      dismissOnClickOutside = false,
      decorFitsSystemWindows = false
    )
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .clickable(
          interactionSource = backgroundInteractionSource,
          indication = null,
          onClick = onDismissRequest
        )
    ) {
      BoxWithConstraints(
        modifier = Modifier
          .fillMaxSize()
          .offset(x = dialogCenterOffset)
          .padding(DetectionPreviewDialogScreenPadding),
        contentAlignment = Alignment.Center
      ) {
        content()
      }
    }
  }
}