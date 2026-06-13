package com.karandaev.geo_inspect.feature.presentation.view.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.core.ui.components.dialog.MyDialogHost
import com.karandaev.geo_inspect.feature.ui.components.fields.MyConfirmationWordField

private const val DeleteConfirmationWord = "DELETE"

/**
 * Shows delete confirmation dialog only when it is requested.
 *
 * Requires explicit confirmation word before delete action becomes available.
 */
@Composable
internal fun ViewInspectionReportDeleteDialogHost(
  showDeleteDialog: Boolean,
  inspectionReportTitle: String,
  confirmationText: String,
  onConfirmationTextChange: (String) -> Unit,
  onConfirmationTextClear: () -> Unit,
  onDismiss: () -> Unit,
  onConfirm: () -> Unit,
  requiredConfirmationWord: String = DeleteConfirmationWord
) {
  val isConfirmationValid = confirmationText.trim() == requiredConfirmationWord

  MyDialogHost(
    showDialog = showDeleteDialog,
    title = stringResource(R.string.view_delete_report_dialog_title),
    icon = Icons.Default.Delete,
    iconTint = MaterialTheme.colorScheme.error,
    confirmButtonText = stringResource(R.string.view_delete_report_dialog_confirm),
    dismissButtonText = stringResource(R.string.view_delete_report_dialog_cancel),
    confirmButtonEnabled = isConfirmationValid,
    dismissButtonEnabled = true,
    onDismiss = onDismiss,
    onConfirm = onConfirm
  ) {
    Text(
      text = stringResource(
        id = R.string.view_delete_report_dialog_message,
        inspectionReportTitle.ifBlank {
          stringResource(R.string.view_delete_report_dialog_untitled)
        }
      ),
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurface
    )

    MyConfirmationWordField(
      confirmationText = confirmationText,
      requiredConfirmationWord = requiredConfirmationWord,
      label = stringResource(R.string.view_delete_report_dialog_confirmation_label),
      promptText = stringResource(
        id = R.string.view_delete_report_dialog_confirmation_prompt,
        requiredConfirmationWord
      ),
      enabled = true,
      onConfirmationTextChange = onConfirmationTextChange,
      onConfirmationTextClear = onConfirmationTextClear
    )
  }
}