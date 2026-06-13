package com.karandaev.geo_inspect.feature.presentation.settings.components.server

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.core.presentation.settings.ServerAvailabilityState

/**
 * Settings row that displays configured server base URL and server availability marker.
 *
 * @param serverBaseUrl Currently saved server base URL.
 * @param availabilityState Current availability state of the saved server URL.
 * @param enabled Whether the row and check button are enabled.
 * @param onClick Called when the row is clicked.
 * @param onCheckServerClick Called when availability marker button is clicked.
 * @param modifier Modifier applied to the row.
 */
@Composable
internal fun SettingsServerBaseUrlRow(
  serverBaseUrl: String,
  availabilityState: ServerAvailabilityState,
  enabled: Boolean,
  onClick: () -> Unit,
  onCheckServerClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val normalizedServerBaseUrl = serverBaseUrl.trim()
  val hasServerBaseUrl = normalizedServerBaseUrl.isNotBlank()
  val isChecking = availabilityState == ServerAvailabilityState.Checking

  Row(
    modifier = modifier
      .clickable(
        enabled = enabled,
        onClick = onClick
      )
      .padding(16.dp),
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = Icons.Default.Dns,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Column(
      modifier = Modifier.weight(1f),
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Text(
        text = stringResource(R.string.settings_server_api_title),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface
      )

      Text(
        text = if (hasServerBaseUrl) {
          normalizedServerBaseUrl
        } else {
          stringResource(R.string.settings_server_api_not_configured)
        },
        style = MaterialTheme.typography.bodySmall,
        color = if (hasServerBaseUrl) {
          MaterialTheme.colorScheme.onSurfaceVariant
        } else {
          MaterialTheme.colorScheme.error
        },
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      ServerAvailabilityText(
        availabilityState = availabilityState
      )
    }

    ServerAvailabilityMarkerButton(
      availabilityState = availabilityState,
      enabled = enabled && !isChecking && hasServerBaseUrl,
      onClick = onCheckServerClick,
      modifier = Modifier.align(Alignment.CenterVertically)
    )
  }
}