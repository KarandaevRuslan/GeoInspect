package com.karandaev.geo_inspect.feature.presentation.profile.dialogs

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.core.image.crop.NormalizedCropBox
import com.karandaev.geo_inspect.core.image.source_file.ImageSourceFile
import com.karandaev.geo_inspect.core.presentation.auth.model.UserProfile
import com.karandaev.geo_inspect.core.presentation.message.UiMessage
import com.karandaev.geo_inspect.core.ui.components.dialog.MyDialogHost
import com.karandaev.geo_inspect.core.ui.components.notification.MiniUiMessageCard
import com.karandaev.geo_inspect.feature.presentation.profile.dialogs.avatar.ProfileAvatarAdaptiveContent
import com.karandaev.geo_inspect.feature.presentation.profile.dialogs.avatar.profileAvatarDialogWidth

/**
 * Dialog for choosing, cropping, updating and resetting profile avatar.
 */
@Composable
internal fun ProfileAvatarDialogHost(
  showDialog: Boolean,
  profile: UserProfile?,
  avatarImageSource: ImageSourceFile?,
  message: UiMessage?,
  enabled: Boolean,
  onChooseImageClick: () -> Unit,
  onCropBoxChange: (NormalizedCropBox) -> Unit,
  onClearSelectedImageClick: () -> Unit,
  onClearMessageClick: () -> Unit,
  onDismiss: () -> Unit,
  onUpdateAvatarClick: () -> Unit,
  onResetAvatarClick: () -> Unit
) {
  val hasSelectedImage = avatarImageSource != null
  val isLandscape = LocalConfiguration.current.orientation ==
    Configuration.ORIENTATION_LANDSCAPE

  MyDialogHost(
    showDialog = showDialog,
    title = stringResource(R.string.profile_avatar_dialog_title),
    icon = Icons.Default.Image,
    iconTint = MaterialTheme.colorScheme.primary,
    confirmButtonText = stringResource(R.string.profile_avatar_dialog_update),
    dismissButtonText = stringResource(R.string.profile_dialog_cancel),
    confirmButtonEnabled = enabled && hasSelectedImage,
    dismissButtonEnabled = enabled,
    onDismiss = onDismiss,
    onConfirm = onUpdateAvatarClick,
    usePlatformDefaultWidth = !isLandscape,
    modifier = Modifier.profileAvatarDialogWidth(
      isLandscape = isLandscape
    )
  ) {
    MiniUiMessageCard(
      message = message,
      onClearMessageClick = onClearMessageClick
    )

    ProfileAvatarAdaptiveContent(
      isLandscape = isLandscape,
      profile = profile,
      avatarImageSource = avatarImageSource,
      hasSelectedImage = hasSelectedImage,
      enabled = enabled,
      onChooseImageClick = onChooseImageClick,
      onCropBoxChange = onCropBoxChange,
      onClearSelectedImageClick = onClearSelectedImageClick,
      onResetAvatarClick = onResetAvatarClick
    )
  }
}