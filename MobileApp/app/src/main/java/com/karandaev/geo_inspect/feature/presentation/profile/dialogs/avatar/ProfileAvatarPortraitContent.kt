package com.karandaev.geo_inspect.feature.presentation.profile.dialogs.avatar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.karandaev.geo_inspect.core.image.crop.NormalizedCropBox
import com.karandaev.geo_inspect.core.image.source_file.ImageSourceFile
import com.karandaev.geo_inspect.core.presentation.auth.model.UserProfile

/**
 * Portrait avatar dialog content.
 */
@Composable
internal fun ProfileAvatarPortraitContent(
  profile: UserProfile?,
  avatarImageSource: ImageSourceFile?,
  hasSelectedImage: Boolean,
  enabled: Boolean,
  onChooseImageClick: () -> Unit,
  onCropBoxChange: (NormalizedCropBox) -> Unit,
  onClearSelectedImageClick: () -> Unit,
  onResetAvatarClick: () -> Unit
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(ProfileAvatarPortraitSpacing)
  ) {
    if (avatarImageSource == null) {
      ProfileAvatarPreviewMainContent(
        profile = profile
      )
    } else {
      ProfileAvatarFullWidthCropperContent(
        avatarImageSource = avatarImageSource,
        enabled = enabled,
        onCropBoxChange = onCropBoxChange
      )
    }

    ProfileAvatarDialogActions(
      hasSelectedImage = hasSelectedImage,
      enabled = enabled,
      onChooseImageClick = onChooseImageClick,
      onClearSelectedImageClick = onClearSelectedImageClick,
      onResetAvatarClick = onResetAvatarClick
    )
  }
}