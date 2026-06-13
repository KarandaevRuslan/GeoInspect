package com.karandaev.geo_inspect.feature.presentation.profile.components.providers

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Small badge for a single auth provider.
 *
 * @param providerId Auth provider id.
 */
@Composable
internal fun ProfileProviderBadge(
  providerId: String
) {
  Surface(
    shape = RoundedCornerShape(50),
    color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
  ) {
    Text(
      text = providerId.toAuthProviderDisplayName(),
      style = MaterialTheme.typography.labelMedium,
      modifier = Modifier.padding(
        horizontal = 10.dp,
        vertical = 5.dp
      )
    )
  }
}