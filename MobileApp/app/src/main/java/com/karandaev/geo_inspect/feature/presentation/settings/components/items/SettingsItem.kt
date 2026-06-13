package com.karandaev.geo_inspect.feature.presentation.settings.components.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

private const val DisabledSettingsItemAlpha = 0.56f
private val SettingsItemMinHeight = 72.dp

/**
 * Base settings row with a title, description, and trailing action content.
 *
 * @param label Main row title.
 * @param description Secondary row description.
 * @param enabled Whether row click is enabled.
 * @param onClick Optional row click action.
 * @param action Trailing composable content, usually an icon, switch, or selector.
 */
@Composable
internal fun SettingsItem(
  label: String,
  description: String,
  enabled: Boolean = true,
  onClick: (() -> Unit)? = null,
  action: @Composable () -> Unit
) {
  val clickableModifier = if (onClick != null) {
    Modifier.clickable(
      enabled = enabled,
      role = Role.Button,
      onClick = onClick
    )
  } else {
    Modifier
  }

  val contentAlpha = if (enabled) {
    1f
  } else {
    DisabledSettingsItemAlpha
  }

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .then(clickableModifier)
      .sizeIn(minHeight = SettingsItemMinHeight)
      .padding(horizontal = 16.dp, vertical = 12.dp)
      .alpha(contentAlpha),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(
      modifier = Modifier.weight(1f),
      verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
      Text(
        text = label,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface
      )

      Text(
        text = description,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    action()
  }
}