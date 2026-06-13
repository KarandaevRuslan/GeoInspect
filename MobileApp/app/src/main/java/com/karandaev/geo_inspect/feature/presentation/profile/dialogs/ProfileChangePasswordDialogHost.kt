package com.karandaev.geo_inspect.feature.presentation.profile.dialogs

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.core.presentation.message.UiMessage
import com.karandaev.geo_inspect.core.ui.components.bars.PasswordStrengthBar
import com.karandaev.geo_inspect.core.ui.components.bars.PasswordStrengthVerticalBar
import com.karandaev.geo_inspect.core.ui.components.dialog.MyDialogHost
import com.karandaev.geo_inspect.core.ui.components.notification.MiniUiMessageCard
import com.karandaev.geo_inspect.feature.ui.components.fields.MyPasswordField

/**
 * Dialog for changing current user's password.
 */
@Composable
internal fun ProfileChangePasswordDialogHost(
  showDialog: Boolean,
  password: String,
  repeatedPassword: String,
  passwordStrength: Int,
  message: UiMessage?,
  enabled: Boolean,
  onPasswordChange: (String) -> Unit,
  onPasswordClear: () -> Unit,
  onRepeatedPasswordChange: (String) -> Unit,
  onRepeatedPasswordClear: () -> Unit,
  onClearMessageClick: () -> Unit,
  onDismiss: () -> Unit,
  onSave: () -> Unit
) {
  val isLandscape = LocalConfiguration.current.orientation ==
    Configuration.ORIENTATION_LANDSCAPE

  MyDialogHost(
    showDialog = showDialog,
    title = stringResource(R.string.profile_change_password_dialog_title),
    icon = Icons.Default.Lock,
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

    if (isLandscape) {
      ProfileChangePasswordLandscapeContent(
        password = password,
        repeatedPassword = repeatedPassword,
        passwordStrength = passwordStrength,
        enabled = enabled,
        onPasswordChange = onPasswordChange,
        onPasswordClear = onPasswordClear,
        onRepeatedPasswordChange = onRepeatedPasswordChange,
        onRepeatedPasswordClear = onRepeatedPasswordClear
      )
    } else {
      ProfileChangePasswordPortraitContent(
        password = password,
        repeatedPassword = repeatedPassword,
        passwordStrength = passwordStrength,
        enabled = enabled,
        onPasswordChange = onPasswordChange,
        onPasswordClear = onPasswordClear,
        onRepeatedPasswordChange = onRepeatedPasswordChange,
        onRepeatedPasswordClear = onRepeatedPasswordClear
      )
    }
  }
}

/**
 * Landscape password fields with vertical strength indicator.
 */
@Composable
private fun ProfileChangePasswordLandscapeContent(
  password: String,
  repeatedPassword: String,
  passwordStrength: Int,
  enabled: Boolean,
  onPasswordChange: (String) -> Unit,
  onPasswordClear: () -> Unit,
  onRepeatedPasswordChange: (String) -> Unit,
  onRepeatedPasswordClear: () -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    ProfileChangePasswordFields(
      password = password,
      repeatedPassword = repeatedPassword,
      enabled = enabled,
      onPasswordChange = onPasswordChange,
      onPasswordClear = onPasswordClear,
      onRepeatedPasswordChange = onRepeatedPasswordChange,
      onRepeatedPasswordClear = onRepeatedPasswordClear,
      modifier = Modifier.weight(1f)
    )

    PasswordStrengthVerticalBar(
      strength = passwordStrength
    )
  }
}

/**
 * Portrait password fields with horizontal strength indicator.
 */
@Composable
private fun ProfileChangePasswordPortraitContent(
  password: String,
  repeatedPassword: String,
  passwordStrength: Int,
  enabled: Boolean,
  onPasswordChange: (String) -> Unit,
  onPasswordClear: () -> Unit,
  onRepeatedPasswordChange: (String) -> Unit,
  onRepeatedPasswordClear: () -> Unit
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    ProfileChangePasswordFields(
      password = password,
      repeatedPassword = repeatedPassword,
      enabled = enabled,
      onPasswordChange = onPasswordChange,
      onPasswordClear = onPasswordClear,
      onRepeatedPasswordChange = onRepeatedPasswordChange,
      onRepeatedPasswordClear = onRepeatedPasswordClear,
      modifier = Modifier.fillMaxWidth()
    )

    PasswordStrengthBar(
      strength = passwordStrength
    )
  }
}

/**
 * Password input fields.
 */
@Composable
private fun ProfileChangePasswordFields(
  password: String,
  repeatedPassword: String,
  enabled: Boolean,
  onPasswordChange: (String) -> Unit,
  onPasswordClear: () -> Unit,
  onRepeatedPasswordChange: (String) -> Unit,
  onRepeatedPasswordClear: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(3.dp)
  ) {
    MyPasswordField(
      password = password,
      enabled = enabled,
      onPasswordChange = onPasswordChange,
      onPasswordClear = onPasswordClear,
      label = stringResource(R.string.profile_change_password_dialog_new_password),
      modifier = Modifier.fillMaxWidth()
    )

    MyPasswordField(
      password = repeatedPassword,
      enabled = enabled,
      onPasswordChange = onRepeatedPasswordChange,
      onPasswordClear = onRepeatedPasswordClear,
      label = stringResource(R.string.profile_change_password_dialog_repeat_new_password),
      labelTextStyle = MaterialTheme.typography.bodySmall,
      modifier = Modifier.fillMaxWidth()
    )
  }
}