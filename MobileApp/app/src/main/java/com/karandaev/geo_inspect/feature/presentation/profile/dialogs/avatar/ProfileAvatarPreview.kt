package com.karandaev.geo_inspect.feature.presentation.profile.dialogs.avatar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.core.presentation.auth.model.UserProfile
import com.karandaev.geo_inspect.core.presentation.auth.model.effectivePhotoUrl

private val AvatarPreviewSize = 88.dp
private val AvatarPreviewPlaceholderIconSize = 40.dp

/**
 * Shows current profile avatar preview.
 */
@Composable
internal fun ProfileAvatarPreview(
  profile: UserProfile?,
  modifier: Modifier = Modifier
) {
  val photoUrl = profile?.effectivePhotoUrl

  Row(
    modifier = modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    Surface(
      modifier = Modifier.size(AvatarPreviewSize),
      shape = CircleShape,
      color = MaterialTheme.colorScheme.secondaryContainer,
      contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
      tonalElevation = 2.dp
    ) {
      Box(
        contentAlignment = Alignment.Center
      ) {
        if (photoUrl.isNullOrBlank()) {
          Icon(
            imageVector = Icons.Default.Image,
            contentDescription = null,
            modifier = Modifier.size(AvatarPreviewPlaceholderIconSize)
          )
        } else {
          AsyncImage(
            model = photoUrl,
            contentDescription = stringResource(
              R.string.profile_avatar_preview_content_description
            ),
            modifier = Modifier
              .size(AvatarPreviewSize)
              .clip(CircleShape)
          )
        }
      }
    }

    Column(
      modifier = Modifier.weight(1f),
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Text(
        text = stringResource(R.string.profile_avatar_dialog_current_avatar),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Text(
        text = stringResource(R.string.profile_avatar_dialog_no_new_image),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}