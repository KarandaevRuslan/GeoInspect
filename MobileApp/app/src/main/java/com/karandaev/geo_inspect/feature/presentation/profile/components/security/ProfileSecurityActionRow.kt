package com.karandaev.geo_inspect.feature.presentation.profile.components.security

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

private val ProfileSecurityActionSize = 48.dp
private val ProfileSecurityActionIconSize = 24.dp

/**
 * Clickable security action row.
 *
 * @param title Action title.
 * @param description Action description.
 * @param icon Trailing action icon.
 * @param enabled Whether click action is enabled.
 * @param isDestructive Whether action should use destructive styling.
 * @param onClick Called when row is clicked.
 */
@Composable
internal fun ProfileSecurityActionRow(
  title: String,
  description: String,
  icon: ImageVector,
  enabled: Boolean,
  isDestructive: Boolean,
  onClick: () -> Unit
) {
  val actionColor = if (isDestructive) {
    MaterialTheme.colorScheme.error
  } else {
    MaterialTheme.colorScheme.primary
  }

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(
        enabled = enabled,
        role = Role.Button,
        onClick = onClick
      )
      .padding(
        horizontal = 4.dp,
        vertical = 8.dp
      ),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Column(
      modifier = Modifier.weight(1f),
      verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge,
        color = if (isDestructive) {
          MaterialTheme.colorScheme.error
        } else {
          MaterialTheme.colorScheme.onSurface
        }
      )

      Text(
        text = description,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    Box(
      modifier = Modifier.size(ProfileSecurityActionSize),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = title,
        tint = actionColor,
        modifier = Modifier.size(ProfileSecurityActionIconSize)
      )
    }
  }
}