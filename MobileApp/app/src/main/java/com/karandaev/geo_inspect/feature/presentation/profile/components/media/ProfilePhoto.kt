package com.karandaev.geo_inspect.feature.presentation.profile.components.media

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.karandaev.geo_inspect.R

/**
 * Displays user profile photo or fallback placeholder.
 *
 * @param photoUrl Optional profile photo URL.
 * @param size Photo size.
 * @param modifier Modifier applied to the photo container.
 */
@Composable
internal fun ProfilePhoto(
  photoUrl: String?,
  size: Dp = 72.dp,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.size(size),
    shape = CircleShape,
    color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
  ) {
    if (photoUrl.isNullOrBlank()) {
      ProfilePhotoPlaceholder(
        size = size
      )
    } else {
      AsyncImage(
        model = photoUrl,
        contentDescription = stringResource(R.string.profile_photo_content_description),
        modifier = Modifier
          .size(size)
          .clip(CircleShape)
      )
    }
  }
}

@Composable
private fun ProfilePhotoPlaceholder(
  size: Dp
) {
  Box(
    contentAlignment = Alignment.Center
  ) {
    Icon(
      imageVector = Icons.Default.Person,
      contentDescription = stringResource(R.string.profile_photo_placeholder_content_description),
      modifier = Modifier.size(size * 0.5f)
    )
  }
}