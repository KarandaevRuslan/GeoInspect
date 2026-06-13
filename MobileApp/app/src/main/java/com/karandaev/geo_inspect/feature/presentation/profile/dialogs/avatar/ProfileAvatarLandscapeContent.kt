package com.karandaev.geo_inspect.feature.presentation.profile.dialogs.avatar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.karandaev.geo_inspect.core.image.crop.NormalizedCropBox
import com.karandaev.geo_inspect.core.image.source_file.ImageSourceFile
import com.karandaev.geo_inspect.core.presentation.auth.model.UserProfile

/**
 * Landscape avatar dialog content.
 */
@Composable
internal fun ProfileAvatarLandscapeContent(
  profile: UserProfile?,
  avatarImageSource: ImageSourceFile?,
  hasSelectedImage: Boolean,
  enabled: Boolean,
  onChooseImageClick: () -> Unit,
  onCropBoxChange: (NormalizedCropBox) -> Unit,
  onClearSelectedImageClick: () -> Unit,
  onResetAvatarClick: () -> Unit
) {
  if (avatarImageSource == null) {
    ProfileAvatarLandscapePreviewContent(
      profile = profile,
      hasSelectedImage = hasSelectedImage,
      enabled = enabled,
      onChooseImageClick = onChooseImageClick,
      onClearSelectedImageClick = onClearSelectedImageClick,
      onResetAvatarClick = onResetAvatarClick
    )
  } else {
    ProfileAvatarLandscapeCropContent(
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

/**
 * Landscape content before image is selected.
 */
@Composable
private fun ProfileAvatarLandscapePreviewContent(
  profile: UserProfile?,
  hasSelectedImage: Boolean,
  enabled: Boolean,
  onChooseImageClick: () -> Unit,
  onClearSelectedImageClick: () -> Unit,
  onResetAvatarClick: () -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(ProfileAvatarLandscapeSpacing),
    verticalAlignment = Alignment.CenterVertically
  ) {
    ProfileAvatarPreviewMainContent(
      profile = profile,
      modifier = Modifier.weight(ProfileAvatarLandscapePreviewWeight)
    )

    ProfileAvatarDialogActions(
      hasSelectedImage = hasSelectedImage,
      enabled = enabled,
      onChooseImageClick = onChooseImageClick,
      onClearSelectedImageClick = onClearSelectedImageClick,
      onResetAvatarClick = onResetAvatarClick,
      modifier = Modifier.weight(ProfileAvatarLandscapeActionsWeight)
    )
  }
}

/**
 * Landscape content after image is selected.
 */
@Composable
private fun ProfileAvatarLandscapeCropContent(
  avatarImageSource: ImageSourceFile,
  hasSelectedImage: Boolean,
  enabled: Boolean,
  onChooseImageClick: () -> Unit,
  onCropBoxChange: (NormalizedCropBox) -> Unit,
  onClearSelectedImageClick: () -> Unit,
  onResetAvatarClick: () -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(ProfileAvatarLandscapeSpacing),
    verticalAlignment = Alignment.CenterVertically
  ) {
    ProfileAvatarLandscapeCropHintColumn(
      modifier = Modifier.weight(ProfileAvatarLandscapeHintWeight)
    )

    ProfileAvatarCompactCropperContent(
      avatarImageSource = avatarImageSource,
      enabled = enabled,
      onCropBoxChange = onCropBoxChange,
      modifier = Modifier.weight(ProfileAvatarLandscapeCropperWeight)
    )

    ProfileAvatarDialogActions(
      hasSelectedImage = hasSelectedImage,
      enabled = enabled,
      onChooseImageClick = onChooseImageClick,
      onClearSelectedImageClick = onClearSelectedImageClick,
      onResetAvatarClick = onResetAvatarClick,
      modifier = Modifier.weight(ProfileAvatarLandscapeActionsWeight)
    )
  }
}