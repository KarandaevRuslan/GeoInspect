package com.karandaev.geo_inspect.feature.presentation.settings.components.account

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage


@Composable
internal fun AccountAvatar(
  photoUrl: String?,
  isSignedIn: Boolean
) {
  Surface(
    modifier = Modifier.size(40.dp),
    shape = CircleShape,
    color = if (isSignedIn) {
      MaterialTheme.colorScheme.secondaryContainer
    } else {
      MaterialTheme.colorScheme.primaryContainer
    }
  ) {
    if (!photoUrl.isNullOrBlank()) {
      AsyncImage(
        model = photoUrl,
        contentDescription = "Profile photo",
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
      )
    } else {
      Icon(
        imageVector = if (isSignedIn) {
          Icons.Default.Person
        } else {
          Icons.AutoMirrored.Filled.Login
        },
        contentDescription = null,
        tint = if (isSignedIn) {
          MaterialTheme.colorScheme.onSecondaryContainer
        } else {
          MaterialTheme.colorScheme.onPrimaryContainer
        },
        modifier = Modifier.padding(8.dp)
      )
    }
  }
}