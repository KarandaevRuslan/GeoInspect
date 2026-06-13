package com.karandaev.geo_inspect.feature.presentation.settings.components.sections

import androidx.compose.runtime.Composable
import com.karandaev.geo_inspect.core.presentation.settings.ServerAvailabilityState
import com.karandaev.geo_inspect.feature.presentation.settings.components.server.SettingsServerBaseUrlRow

/**
 * Settings section that displays road crack backend server configuration.
 *
 * @param serverBaseUrl Currently saved server base URL.
 * @param availabilityState Current availability state of the saved server URL.
 * @param enabled Whether the card and check button are enabled.
 * @param onServerBaseUrlClick Called when server base URL card is clicked.
 * @param onCheckServerClick Called when availability marker button is clicked.
 */
@Composable
internal fun SettingsServerSection(
  serverBaseUrl: String,
  availabilityState: ServerAvailabilityState,
  enabled: Boolean,
  onServerBaseUrlClick: () -> Unit,
  onCheckServerClick: () -> Unit
) {
  SettingsSection(
    title = "Server"
  ) {
    SettingsServerBaseUrlRow(
      serverBaseUrl = serverBaseUrl,
      availabilityState = availabilityState,
      enabled = enabled,
      onClick = onServerBaseUrlClick,
      onCheckServerClick = onCheckServerClick
    )
  }
}