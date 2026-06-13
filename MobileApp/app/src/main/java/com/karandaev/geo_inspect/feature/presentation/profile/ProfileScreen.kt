package com.karandaev.geo_inspect.feature.presentation.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.karandaev.geo_inspect.core.presentation.auth.model.UserProfile
import com.karandaev.geo_inspect.feature.presentation.profile.contents.ProfileContent

/**
 * Profile screen UI.
 *
 * This composable is UI-only and does not know about ViewModels or Firebase.
 */
@Composable
fun ProfileScreen(
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
  ProfileContent(
    profile = profile,
    isLoading = isLoading,
    onBackClick = onBackClick,
    onAvatarClick = onAvatarClick,
    onDisplayNameClick = onDisplayNameClick,
    onChangePasswordClick = onChangePasswordClick,
    onDeleteAccountClick = onDeleteAccountClick,
    onLogoutClick = onLogoutClick,
    modifier = modifier
  )
}