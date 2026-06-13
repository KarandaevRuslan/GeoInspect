package com.karandaev.geo_inspect.feature.presentation.profile.components.identity

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.core.presentation.auth.model.UserProfile
import com.karandaev.geo_inspect.core.presentation.auth.model.effectivePhotoUrl
import com.karandaev.geo_inspect.feature.presentation.profile.components.media.ProfilePhoto

private val ProfileAvatarSize = 72.dp
private val ProfileAvatarEditButtonSize = 28.dp
private val ProfileAvatarEditIconSize = 16.dp
private val ProfileAvatarEditButtonOffset = 5.5.dp

/**
 * Avatar with edit action overlay.
 *
 * @param profile Current user profile, or null.
 * @param enabled Whether edit action is enabled.
 * @param onClick Called when avatar or edit button is clicked.
 */
@Composable
internal fun ProfileAvatarEditBox(
  profile: UserProfile?,
  enabled: Boolean,
  onClick: () -> Unit
) {
  Box {
    ProfilePhoto(
      photoUrl = profile?.effectivePhotoUrl,
      size = ProfileAvatarSize,
      modifier = Modifier.clickable(
        enabled = enabled,
        onClick = onClick
      )
    )

    FilledTonalIconButton(
      enabled = enabled,
      onClick = onClick,
      modifier = Modifier
        .align(Alignment.TopEnd)
        .offset(
          x = ProfileAvatarEditButtonOffset,
          y = -ProfileAvatarEditButtonOffset
        )
        .size(ProfileAvatarEditButtonSize)
    ) {
      Icon(
        imageVector = Icons.Default.Edit,
        contentDescription = stringResource(
          R.string.profile_avatar_edit_content_description
        ),
        modifier = Modifier.size(ProfileAvatarEditIconSize)
      )
    }
  }
}