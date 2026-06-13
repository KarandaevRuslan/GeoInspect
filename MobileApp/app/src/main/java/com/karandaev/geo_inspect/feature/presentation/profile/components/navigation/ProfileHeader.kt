package com.karandaev.geo_inspect.feature.presentation.profile.components.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.app.components.AppBackButton

/**
 * Profile title row with back navigation.
 *
 * @param onBackClick Called when the back button is clicked.
 * @param modifier Modifier applied to the root.
 */
@Composable
internal fun ProfileHeader(
  onBackClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    AppBackButton(
      onClick = onBackClick
    )

    Text(
      text = stringResource(id = R.string.destination_profile),
      style = MaterialTheme.typography.headlineSmall,
      color = MaterialTheme.colorScheme.onSurface,
      modifier = Modifier.weight(1f)
    )
  }
}