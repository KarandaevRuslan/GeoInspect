package com.karandaev.geo_inspect.feature.presentation.profile.dialogs.avatar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R

/**
 * Avatar dialog secondary actions.
 */
@Composable
internal fun ProfileAvatarDialogActions(
  hasSelectedImage: Boolean,
  enabled: Boolean,
  onChooseImageClick: () -> Unit,
  onClearSelectedImageClick: () -> Unit,
  onResetAvatarClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    if (hasSelectedImage) {
      OutlinedButton(
        enabled = enabled,
        onClick = onChooseImageClick,
        modifier = Modifier.fillMaxWidth()
      ) {
        Icon(
          imageVector = Icons.Default.Image,
          contentDescription = null
        )

        Text(
          text = stringResource(R.string.profile_avatar_dialog_choose_another_image)
        )
      }

      OutlinedButton(
        enabled = enabled,
        onClick = onClearSelectedImageClick,
        modifier = Modifier.fillMaxWidth()
      ) {
        Icon(
          imageVector = Icons.Default.DeleteOutline,
          contentDescription = null
        )

        Text(
          text = stringResource(R.string.profile_avatar_dialog_clear_selected_image)
        )
      }
    } else {
      Button(
        enabled = enabled,
        onClick = onChooseImageClick,
        modifier = Modifier.fillMaxWidth()
      ) {
        Icon(
          imageVector = Icons.Default.Image,
          contentDescription = null
        )

        Text(
          text = stringResource(R.string.profile_avatar_dialog_choose_image)
        )
      }
    }

    OutlinedButton(
      enabled = enabled,
      onClick = onResetAvatarClick,
      modifier = Modifier.fillMaxWidth()
    ) {
      Icon(
        imageVector = Icons.Default.Refresh,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.error
      )

      Text(
        text = stringResource(R.string.profile_avatar_dialog_reset_avatar),
        color = MaterialTheme.colorScheme.error
      )
    }
  }
}