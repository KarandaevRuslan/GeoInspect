package com.karandaev.geo_inspect.feature.presentation.settings.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.core.presentation.settings.ServerAvailabilityState
import com.karandaev.geo_inspect.feature.presentation.settings.components.server.ServerAvailabilityMarkerButton
import com.karandaev.geo_inspect.feature.presentation.settings.components.server.ServerAvailabilityText
import com.karandaev.geo_inspect.core.ui.components.dialog.MyDialogHost
import com.karandaev.geo_inspect.feature.ui.components.fields.MyServerBaseUrlField

/**
 * Shows server base URL edit dialog only when it is requested.
 *
 * @param showDialog Whether the dialog should be visible.
 * @param serverBaseUrl Current editable server base URL draft.
 * @param availabilityState Current server availability check state.
 * @param enabled Whether dialog controls are enabled.
 * @param onServerBaseUrlChange Called when server base URL draft changes.
 * @param onServerBaseUrlClear Called when clear button is clicked.
 * @param onCheckServerClick Called when server availability check button is clicked.
 * @param onDismiss Called when the dialog should be dismissed.
 * @param onSave Called when the user saves the server base URL.
 */
@Composable
internal fun SettingsServerBaseUrlDialogHost(
  showDialog: Boolean,
  serverBaseUrl: String,
  availabilityState: ServerAvailabilityState,
  enabled: Boolean,
  onServerBaseUrlChange: (String) -> Unit,
  onServerBaseUrlClear: () -> Unit,
  onCheckServerClick: () -> Unit,
  onDismiss: () -> Unit,
  onSave: () -> Unit
) {
  val isChecking = availabilityState == ServerAvailabilityState.Checking
  val hasServerBaseUrl = serverBaseUrl.isNotBlank()
  val controlsEnabled = enabled && !isChecking

  MyDialogHost(
    showDialog = showDialog,
    title = stringResource(R.string.settings_server_api_title),
    icon = Icons.Default.Dns,
    iconTint = MaterialTheme.colorScheme.primary,
    confirmButtonText = stringResource(R.string.settings_server_api_dialog_save),
    dismissButtonText = stringResource(R.string.settings_server_api_dialog_cancel),
    confirmButtonEnabled = controlsEnabled,
    dismissButtonEnabled = controlsEnabled,
    onDismiss = onDismiss,
    onConfirm = onSave
  ) {
    Text(
      text = stringResource(R.string.settings_server_api_dialog_description),
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Column {
      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        MyServerBaseUrlField(
          serverBaseUrl = serverBaseUrl,
          enabled = controlsEnabled,
          onServerBaseUrlChange = onServerBaseUrlChange,
          onServerBaseUrlClear = onServerBaseUrlClear,
          modifier = Modifier.weight(1f)
        )

        ServerAvailabilityMarkerButton(
          availabilityState = availabilityState,
          enabled = controlsEnabled && hasServerBaseUrl,
          onClick = onCheckServerClick,
          modifier = Modifier.padding(top = 10.dp)
        )
      }

      Text(
        text = "Example: https://192.168.1.10:8443",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
          .fillMaxWidth()
          .padding(
            start = 16.dp,
            top = 4.dp,
            end = 48.dp
          )
      )
    }

    ServerAvailabilityText(
      availabilityState = availabilityState
    )
  }
}