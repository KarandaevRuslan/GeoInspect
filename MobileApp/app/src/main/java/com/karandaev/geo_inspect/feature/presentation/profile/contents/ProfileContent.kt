package com.karandaev.geo_inspect.feature.presentation.profile.contents

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.karandaev.geo_inspect.app.adaptive_layout.AdaptiveLayoutType
import com.karandaev.geo_inspect.app.adaptive_layout.rememberAdaptiveLayoutType
import com.karandaev.geo_inspect.core.presentation.auth.model.UserProfile

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
internal fun ProfileContent(
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
  BoxWithConstraints(
    modifier = modifier.fillMaxSize()
  ) {
    val layoutType = rememberAdaptiveLayoutType(
      maxWidth = maxWidth,
      maxHeight = maxHeight
    )

    when (layoutType) {
      AdaptiveLayoutType.SinglePane -> {
        ProfileSinglePaneContent(
          profile = profile,
          isLoading = isLoading,
          onAvatarClick = onAvatarClick,
          onDisplayNameClick = onDisplayNameClick,
          onChangePasswordClick = onChangePasswordClick,
          onDeleteAccountClick = onDeleteAccountClick,
          onLogoutClick = onLogoutClick,
          modifier = Modifier.fillMaxSize()
        )
      }

      AdaptiveLayoutType.TwoPane -> {
        ProfileTwoPaneContent(
          profile = profile,
          isLoading = isLoading,
          onBackClick = onBackClick,
          onAvatarClick = onAvatarClick,
          onDisplayNameClick = onDisplayNameClick,
          onChangePasswordClick = onChangePasswordClick,
          onDeleteAccountClick = onDeleteAccountClick,
          onLogoutClick = onLogoutClick,
          modifier = Modifier.fillMaxSize()
        )
      }
    }
  }
}