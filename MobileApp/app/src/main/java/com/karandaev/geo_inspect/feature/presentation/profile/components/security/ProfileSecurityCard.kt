package com.karandaev.geo_inspect.feature.presentation.profile.components.security

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.core.presentation.auth.model.UserProfile
import com.karandaev.geo_inspect.feature.presentation.profile.components.cards.ProfileCardHeader

private const val EmailPasswordProviderId = "password"

/**
 * Displays sensitive account actions.
 *
 * @param profile User profile.
 * @param enabled Whether actions are enabled.
 * @param onChangePasswordClick Called when change password action is clicked.
 * @param onDeleteAccountClick Called when delete account action is clicked.
 * @param modifier Modifier applied to the card.
 */
@Composable
internal fun ProfileSecurityCard(
  profile: UserProfile,
  enabled: Boolean,
  onChangePasswordClick: () -> Unit,
  onDeleteAccountClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val canChangePassword = profile.providerIds.contains(EmailPasswordProviderId)

  Card(
    modifier = modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier.padding(12.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      ProfileCardHeader(
        title = stringResource(R.string.profile_security_title),
        description = stringResource(R.string.profile_security_description)
      )

      if (canChangePassword) {
        ProfileSecurityActionRow(
          title = stringResource(R.string.profile_security_change_password_title),
          description = stringResource(R.string.profile_security_change_password_description),
          icon = Icons.Default.Edit,
          enabled = enabled,
          isDestructive = false,
          onClick = onChangePasswordClick
        )

        HorizontalDivider()
      }

      ProfileSecurityActionRow(
        title = stringResource(R.string.profile_security_delete_account_title),
        description = stringResource(R.string.profile_security_delete_account_description),
        icon = Icons.Default.Delete,
        enabled = enabled,
        isDestructive = true,
        onClick = onDeleteAccountClick
      )
    }
  }
}