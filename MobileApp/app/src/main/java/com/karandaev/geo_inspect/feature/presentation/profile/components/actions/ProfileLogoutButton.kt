package com.karandaev.geo_inspect.feature.presentation.profile.components.actions

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Logout button.
 *
 * @param isLoading Whether auth action is currently running.
 * @param onClick Called when the button is clicked.
 * @param modifier Modifier applied to the button.
 */
@Composable
internal fun ProfileLogoutButton(
  isLoading: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Button(
    onClick = onClick,
    enabled = !isLoading,
    modifier = modifier.fillMaxWidth()
  ) {
    Text("Log out")
  }
}