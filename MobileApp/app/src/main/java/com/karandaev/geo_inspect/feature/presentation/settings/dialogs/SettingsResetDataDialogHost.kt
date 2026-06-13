package com.karandaev.geo_inspect.feature.presentation.settings.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.core.ui.components.dialog.MyDialogHost
import com.karandaev.geo_inspect.feature.ui.components.fields.MyResetConfirmationField

/**
 * Shows reset data confirmation dialog only when it is requested.
 *
 * @param showDialog Whether the reset dialog should be visible.
 * @param confirmationText Current confirmation text.
 * @param requiredConfirmationWord Word required to confirm reset.
 * @param isResetting Whether reset operation is currently running.
 * @param onConfirmationTextChange Called when confirmation text changes.
 * @param onConfirmationTextClear Called when clear button is clicked.
 * @param onDismiss Called when the dialog should be dismissed.
 * @param onConfirm Called when the user confirms data reset.
 */
@Composable
internal fun SettingsResetDataDialogHost(
  showDialog: Boolean,
  confirmationText: String,
  requiredConfirmationWord: String,
  isResetting: Boolean,
  onConfirmationTextChange: (String) -> Unit,
  onConfirmationTextClear: () -> Unit,
  onDismiss: () -> Unit,
  onConfirm: () -> Unit
) {
  val isConfirmationValid = confirmationText.trim() == requiredConfirmationWord
  val controlsEnabled = !isResetting

  MyDialogHost(
    showDialog = showDialog,
    title = stringResource(R.string.settings_reset_data_dialog_title),
    icon = Icons.Default.Warning,
    iconTint = MaterialTheme.colorScheme.error,
    confirmButtonText = stringResource(R.string.settings_reset_data_dialog_confirm),
    dismissButtonText = stringResource(R.string.settings_reset_data_dialog_cancel),
    confirmButtonEnabled = controlsEnabled && isConfirmationValid,
    dismissButtonEnabled = controlsEnabled,
    onDismiss = onDismiss,
    onConfirm = onConfirm
  ) {
    Text(
      text = stringResource(R.string.settings_reset_data_dialog_message),
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurface
    )

    MyResetConfirmationField(
      confirmationText = confirmationText,
      requiredConfirmationWord = requiredConfirmationWord,
      enabled = controlsEnabled,
      onConfirmationTextChange = onConfirmationTextChange,
      onConfirmationTextClear = onConfirmationTextClear
    )
  }
}