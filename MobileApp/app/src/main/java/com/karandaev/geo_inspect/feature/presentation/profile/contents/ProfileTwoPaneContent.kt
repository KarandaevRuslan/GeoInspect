package com.karandaev.geo_inspect.feature.presentation.profile.contents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.core.presentation.auth.model.UserProfile
import com.karandaev.geo_inspect.feature.presentation.profile.components.actions.ProfileLogoutButton
import com.karandaev.geo_inspect.feature.presentation.profile.components.cards.ProfileCard
import com.karandaev.geo_inspect.feature.presentation.profile.components.cards.ProfileEmptyCard
import com.karandaev.geo_inspect.feature.presentation.profile.components.identity.ProfileIdentityCard
import com.karandaev.geo_inspect.feature.presentation.profile.components.navigation.ProfileHeader
import com.karandaev.geo_inspect.feature.presentation.profile.components.security.ProfileSecurityCard

@Composable
internal fun ProfileTwoPaneContent(
  profile: UserProfile?,
  isLoading: Boolean,
  onBackClick: () -> Unit,
  onAvatarClick: () -> Unit,
  onDisplayNameClick: () -> Unit,
  onChangePasswordClick: () -> Unit,
  onDeleteAccountClick: () -> Unit,
  onLogoutClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.padding(
      horizontal = 16.dp
    ),
    horizontalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxHeight()
        .verticalScroll(rememberScrollState())
        .padding(
          top = 16.dp,
          bottom = 16.dp
        ),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      ProfileHeader(
        onBackClick = onBackClick
      )

      ProfileIdentityCard(
        profile = profile,
        enabled = !isLoading && profile != null,
        onAvatarClick = onAvatarClick
      )

      ProfileLogoutButton(
        isLoading = isLoading,
        onClick = onLogoutClick
      )
    }

    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxHeight()
        .verticalScroll(rememberScrollState())
        .padding(
          top = 16.dp,
          bottom = 16.dp
        ),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      if (profile != null) {
        ProfileCard(
          profile = profile,
          enabled = !isLoading,
          onDisplayNameEditClick = onDisplayNameClick
        )

        ProfileSecurityCard(
          profile = profile,
          enabled = !isLoading,
          onChangePasswordClick = onChangePasswordClick,
          onDeleteAccountClick = onDeleteAccountClick
        )
      } else {
        ProfileEmptyCard()
      }
    }
  }
}