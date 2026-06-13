package com.karandaev.geo_inspect.feature.presentation.profile.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.core.presentation.message.UiMessage
import com.karandaev.geo_inspect.core.ui.components.dialog.MyDialogHost
import com.karandaev.geo_inspect.core.ui.components.notification.MiniUiMessageCard
import com.karandaev.geo_inspect.feature.ui.components.fields.MyConfirmationWordField

/**
 * Dialog for deleting current account.
 *
 * Requires explicit confirmation word before delete action becomes available.
 */
@Composable
internal fun ProfileDeleteAccountDialogHost(
  showDialog: Boolean,
  confirmationText: String,
  requiredConfirmationWord: String,
  message: UiMessage?,
  isLoading: Boolean,
  onConfirmationTextChange: (String) -> Unit,
  onConfirmationTextClear: () -> Unit,
  onClearMessageClick: () -> Unit,
  onDismiss: () -> Unit,
  onConfirm: () -> Unit
) {
  val isConfirmationValid = confirmationText.trim() == requiredConfirmationWord
  val controlsEnabled = !isLoading

  MyDialogHost(
    showDialog = showDialog,
    title = stringResource(R.string.profile_delete_account_dialog_title),
    icon = Icons.Default.Delete,
    iconTint = MaterialTheme.colorScheme.error,
    confirmButtonText = stringResource(R.string.profile_delete_account_dialog_confirm),
    dismissButtonText = stringResource(R.string.profile_dialog_cancel),
    confirmButtonEnabled = controlsEnabled && isConfirmationValid,
    dismissButtonEnabled = controlsEnabled,
    onDismiss = onDismiss,
    onConfirm = onConfirm
  ) {
    MiniUiMessageCard(
      message = message,
      onClearMessageClick = onClearMessageClick
    )

    Text(
      text = stringResource(R.string.profile_delete_account_dialog_message),
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurface
    )

    MyConfirmationWordField(
      confirmationText = confirmationText,
      requiredConfirmationWord = requiredConfirmationWord,
      label = stringResource(R.string.confirmation_word_field_label),
      promptText = stringResource(
        id = R.string.profile_delete_account_dialog_confirmation_prompt,
        requiredConfirmationWord
      ),
      enabled = controlsEnabled,
      onConfirmationTextChange = onConfirmationTextChange,
      onConfirmationTextClear = onConfirmationTextClear
    )
  }
}