package com.karandaev.geo_inspect.feature.presentation.profile.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.core.presentation.message.UiMessage
import com.karandaev.geo_inspect.core.ui.components.dialog.MyDialogHost
import com.karandaev.geo_inspect.core.ui.components.notification.MiniUiMessageCard
import com.karandaev.geo_inspect.feature.ui.components.fields.MyUserNameField

/**
 * Dialog for changing current user's display name.
 */
@Composable
internal fun ProfileChangeUserNameDialogHost(
  showDialog: Boolean,
  userName: String,
  message: UiMessage?,
  enabled: Boolean,
  onUserNameChange: (String) -> Unit,
  onUserNameClear: () -> Unit,
  onClearMessageClick: () -> Unit,
  onDismiss: () -> Unit,
  onSave: () -> Unit
) {
  MyDialogHost(
    showDialog = showDialog,
    title = stringResource(R.string.profile_change_user_name_dialog_title),
    icon = Icons.Default.Person,
    iconTint = MaterialTheme.colorScheme.primary,
    confirmButtonText = stringResource(R.string.profile_dialog_save),
    dismissButtonText = stringResource(R.string.profile_dialog_cancel),
    confirmButtonEnabled = enabled,
    dismissButtonEnabled = enabled,
    onDismiss = onDismiss,
    onConfirm = onSave
  ) {
    MiniUiMessageCard(
      message = message,
      onClearMessageClick = onClearMessageClick
    )

    MyUserNameField(
      userName = userName,
      enabled = enabled,
      onUserNameChange = onUserNameChange,
      onUserNameClear = onUserNameClear,
      modifier = Modifier.fillMaxWidth()
    )
  }
}