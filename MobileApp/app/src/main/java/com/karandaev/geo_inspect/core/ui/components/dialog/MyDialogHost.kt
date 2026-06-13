package com.karandaev.geo_inspect.core.ui.components.dialog

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

private val LandscapeDialogActionButtonShape = RoundedCornerShape(999.dp)
private val LandscapeDialogActionButtonHorizontalPadding = 10.dp
private val LandscapeDialogActionButtonVerticalPadding = 4.dp

/**
 * Common scrollable dialog.
 *
 * Uses a more compact title and actions layout in landscape to save vertical space.
 *
 * @param showDialog Whether dialog should be visible.
 * @param title Dialog title.
 * @param icon Dialog top icon.
 * @param iconTint Dialog icon tint.
 * @param confirmButtonText Confirm button text.
 * @param dismissButtonText Dismiss button text.
 * @param confirmButtonEnabled Whether confirm button is enabled.
 * @param dismissButtonEnabled Whether dismiss button and outside dismiss are enabled.
 * @param onDismiss Called when dialog should be dismissed.
 * @param onConfirm Called when confirm button is clicked.
 * @param modifier Modifier applied to dialog.
 * @param usePlatformDefaultWidth Whether platform default dialog width should be used.
 * @param content Dialog scrollable content.
 */
@Composable
internal fun MyDialogHost(
  showDialog: Boolean,
  title: String,
  icon: ImageVector,
  iconTint: Color,
  confirmButtonText: String,
  dismissButtonText: String,
  confirmButtonEnabled: Boolean,
  dismissButtonEnabled: Boolean,
  onDismiss: () -> Unit,
  onConfirm: () -> Unit,
  modifier: Modifier = Modifier,
  usePlatformDefaultWidth: Boolean = true,
  content: @Composable ColumnScope.() -> Unit
) {
  if (!showDialog) return

  val scrollState = rememberScrollState()
  val dialogCenterOffset = rememberDialogCenterOffset()
  val isLandscape = LocalConfiguration.current.orientation ==
    Configuration.ORIENTATION_LANDSCAPE

  val contentSpacing = if (isLandscape) {
    8.dp
  } else {
    12.dp
  }

  AlertDialog(
    modifier = modifier.offset(x = dialogCenterOffset),
    properties = DialogProperties(
      usePlatformDefaultWidth = usePlatformDefaultWidth
    ),
    onDismissRequest = {
      if (dismissButtonEnabled) {
        onDismiss()
      }
    },
    icon = if (isLandscape) {
      null
    } else {
      {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = iconTint
        )
      }
    },
    title = {
      if (isLandscape) {
        MyDialogLandscapeTitle(
          title = title,
          icon = icon,
          iconTint = iconTint
        )
      } else {
        Text(
          text = title
        )
      }
    },
    text = {
      Column(
        modifier = Modifier.verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(contentSpacing),
        content = content
      )
    },
    confirmButton = {
      MyDialogActionButton(
        text = confirmButtonText,
        enabled = confirmButtonEnabled,
        compact = isLandscape,
        onClick = onConfirm
      )
    },
    dismissButton = {
      MyDialogActionButton(
        text = dismissButtonText,
        enabled = dismissButtonEnabled,
        compact = isLandscape,
        onClick = onDismiss
      )
    }
  )
}

/**
 * Compact landscape dialog title with inline icon.
 */
@Composable
private fun MyDialogLandscapeTitle(
  title: String,
  icon: ImageVector,
  iconTint: Color
) {
  Row(
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = iconTint,
      modifier = Modifier.size(20.dp)
    )

    Text(
      text = title,
      style = MaterialTheme.typography.titleMedium
    )
  }
}

/**
 * Dialog action button.
 *
 * Landscape uses compact clickable text to avoid the tall Material button min height.
 */
@Composable
private fun MyDialogActionButton(
  text: String,
  enabled: Boolean,
  compact: Boolean,
  onClick: () -> Unit
) {
  if (!compact) {
    TextButton(
      enabled = enabled,
      onClick = onClick
    ) {
      Text(
        text = text
      )
    }

    return
  }

  Text(
    text = text,
    style = MaterialTheme.typography.labelLarge,
    color = if (enabled) {
      MaterialTheme.colorScheme.primary
    } else {
      MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
    },
    modifier = Modifier
      .clip(LandscapeDialogActionButtonShape)
      .clickable(
        enabled = enabled,
        onClick = onClick
      )
      .padding(
        horizontal = LandscapeDialogActionButtonHorizontalPadding,
        vertical = LandscapeDialogActionButtonVerticalPadding
      )
  )
}