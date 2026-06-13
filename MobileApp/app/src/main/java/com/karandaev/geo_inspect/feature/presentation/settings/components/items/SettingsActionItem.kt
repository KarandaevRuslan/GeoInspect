package com.karandaev.geo_inspect.feature.presentation.settings.components.items

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

private val SettingsActionContainerSize = 40.dp
private val SettingsActionIconSize = 22.dp
private val SettingsActionProgressSize = 22.dp

/**
 * Settings row with a trailing action icon or loading indicator.
 *
 * The whole row is clickable, not only the trailing icon.
 *
 * @param label Main row title.
 * @param description Secondary row description.
 * @param icon Icon displayed as trailing action.
 * @param contentDescription Accessibility description for the action icon.
 * @param isLoading Whether the loading indicator should be shown instead of the icon.
 * @param isDestructive Whether this action represents a destructive operation.
 * @param onClick Called when the user clicks the row.
 */
@Composable
internal fun SettingsActionItem(
  label: String,
  description: String,
  icon: ImageVector,
  contentDescription: String,
  isLoading: Boolean,
  isDestructive: Boolean = false,
  onClick: () -> Unit
) {
  val actionColor = if (isDestructive) {
    MaterialTheme.colorScheme.error
  } else {
    MaterialTheme.colorScheme.primary
  }

  val actionContainerColor = if (isDestructive) {
    MaterialTheme.colorScheme.errorContainer
  } else {
    MaterialTheme.colorScheme.primaryContainer
  }

  SettingsItem(
    label = label,
    description = description,
    enabled = !isLoading,
    onClick = onClick
  ) {
    SettingsActionIndicator(
      icon = icon,
      contentDescription = contentDescription,
      isLoading = isLoading,
      contentColor = actionColor,
      containerColor = actionContainerColor
    )
  }
}

@Composable
private fun SettingsActionIndicator(
  icon: ImageVector,
  contentDescription: String,
  isLoading: Boolean,
  contentColor: Color,
  containerColor: Color
) {
  Surface(
    modifier = Modifier.size(SettingsActionContainerSize),
    shape = CircleShape,
    color = containerColor.copy(alpha = 0.72f)
  ) {
    Box(
      contentAlignment = Alignment.Center
    ) {
      if (isLoading) {
        CircularProgressIndicator(
          modifier = Modifier.size(SettingsActionProgressSize),
          strokeWidth = 2.dp,
          color = contentColor
        )
      } else {
        Icon(
          imageVector = icon,
          contentDescription = contentDescription,
          tint = contentColor,
          modifier = Modifier.size(SettingsActionIconSize)
        )
      }
    }
  }
}