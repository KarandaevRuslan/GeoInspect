package com.karandaev.geo_inspect.feature.presentation.profile.components.identity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.core.presentation.auth.model.UserProfile

/**
 * Compact profile identity card.
 *
 * @param profile Current user profile, or null.
 * @param enabled Whether avatar editing is enabled.
 * @param onAvatarClick Called when avatar or avatar edit button is clicked.
 * @param modifier Modifier applied to the card.
 */
@Composable
internal fun ProfileIdentityCard(
  profile: UserProfile?,
  enabled: Boolean,
  onAvatarClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      ProfileAvatarEditBox(
        profile = profile,
        enabled = enabled,
        onClick = onAvatarClick
      )

      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Text(
          text = profile?.displayName
            ?: profile?.email
            ?: stringResource(R.string.profile_identity_signed_in_user),
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.onPrimaryContainer,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        Text(
          text = profile?.email ?: stringResource(R.string.profile_identity_no_email),
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.78f),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        if (profile != null) {
          ProfileEmailVerificationStatus(
            isVerified = profile.emailVerified
          )
        }
      }
    }
  }
}