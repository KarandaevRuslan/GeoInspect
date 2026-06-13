package com.karandaev.geo_inspect.feature.presentation.profile.dialogs.avatar

import androidx.compose.runtime.Composable
import com.karandaev.geo_inspect.core.image.crop.NormalizedCropBox
import com.karandaev.geo_inspect.core.image.source_file.ImageSourceFile
import com.karandaev.geo_inspect.core.presentation.auth.model.UserProfile

/**
 * Selects avatar dialog layout by current orientation.
 */
@Composable
internal fun ProfileAvatarAdaptiveContent(
  isLandscape: Boolean,
  profile: UserProfile?,
  avatarImageSource: ImageSourceFile?,
  hasSelectedImage: Boolean,
  enabled: Boolean,
  onChooseImageClick: () -> Unit,
  onCropBoxChange: (NormalizedCropBox) -> Unit,
  onClearSelectedImageClick: () -> Unit,
  onResetAvatarClick: () -> Unit
) {
  if (isLandscape) {
    ProfileAvatarLandscapeContent(
      profile = profile,
      avatarImageSource = avatarImageSource,
      hasSelectedImage = hasSelectedImage,
      enabled = enabled,
      onChooseImageClick = onChooseImageClick,
      onCropBoxChange = onCropBoxChange,
      onClearSelectedImageClick = onClearSelectedImageClick,
      onResetAvatarClick = onResetAvatarClick
    )
  } else {
    ProfileAvatarPortraitContent(
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