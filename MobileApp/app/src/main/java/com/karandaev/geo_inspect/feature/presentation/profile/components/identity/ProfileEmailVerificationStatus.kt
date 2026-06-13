package com.karandaev.geo_inspect.feature.presentation.profile.components.identity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R

/**
 * Compact email verification status.
 *
 * @param isVerified Whether email is verified.
 */
@Composable
internal fun ProfileEmailVerificationStatus(
  isVerified: Boolean
) {
  val color = if (isVerified) {
    MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.78f)
  } else {
    MaterialTheme.colorScheme.error
  }

  val icon = if (isVerified) {
    Icons.Default.CheckCircle
  } else {
    Icons.Default.ErrorOutline
  }

  val text = if (isVerified) {
    stringResource(R.string.profile_email_verified)
  } else {
    stringResource(R.string.profile_email_not_verified)
  }

  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Icon(
      imageVector = icon,
      contentDescription = text,
      tint = color,
      modifier = Modifier.size(14.dp)
    )

    Text(
      text = text,
      style = MaterialTheme.typography.labelSmall,
      color = color,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )
  }
}