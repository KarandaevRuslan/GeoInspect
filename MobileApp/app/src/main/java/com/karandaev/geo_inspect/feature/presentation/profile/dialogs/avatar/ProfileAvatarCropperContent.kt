package com.karandaev.geo_inspect.feature.presentation.profile.dialogs.avatar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.core.image.crop.NormalizedCropBox
import com.karandaev.geo_inspect.core.image.source_file.ImageSourceFile
import com.karandaev.geo_inspect.feature.presentation.profile.components.cropper.ProfileAvatarCropper

/**
 * Compact cropper used in landscape layout.
 */
@Composable
internal fun ProfileAvatarCompactCropperContent(
  avatarImageSource: ImageSourceFile,
  enabled: Boolean,
  onCropBoxChange: (NormalizedCropBox) -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier.fillMaxWidth(),
    contentAlignment = Alignment.Center
  ) {
    ProfileAvatarCropper(
      imageSource = avatarImageSource,
      enabled = enabled,
      onCropBoxChange = onCropBoxChange,
      cropperSize = ProfileAvatarLandscapeCropperSize,
      showGestureHint = false,
      modifier = Modifier.size(ProfileAvatarLandscapeCropperSize)
    )
  }
}

/**
 * Full width cropper used in portrait layout.
 */
@Composable
internal fun ProfileAvatarFullWidthCropperContent(
  avatarImageSource: ImageSourceFile,
  enabled: Boolean,
  onCropBoxChange: (NormalizedCropBox) -> Unit
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(ProfileAvatarContentSpacing)
  ) {
    ProfileAvatarCropper(
      imageSource = avatarImageSource,
      enabled = enabled,
      onCropBoxChange = onCropBoxChange,
      showGestureHint = true,
      modifier = Modifier.fillMaxWidth()
    )

    Text(
      text = stringResource(R.string.profile_avatar_dialog_crop_hint),
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}

/**
 * Landscape crop hints placed in the left dialog column.
 */
@Composable
internal fun ProfileAvatarLandscapeCropHintColumn(
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(ProfileAvatarContentSpacing)
  ) {
    Text(
      text = stringResource(R.string.profile_avatar_dialog_crop_hint),
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Text(
      text = stringResource(R.string.profile_avatar_cropper_gesture_hint),
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}