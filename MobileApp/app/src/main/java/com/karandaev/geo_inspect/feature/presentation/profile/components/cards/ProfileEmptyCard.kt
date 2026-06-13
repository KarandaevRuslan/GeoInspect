package com.karandaev.geo_inspect.feature.presentation.profile.components.cards

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R

/**
 * Displays empty profile state.
 *
 * @param modifier Modifier applied to the card.
 */
@Composable
internal fun ProfileEmptyCard(
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth()
  ) {
    Text(
      text = stringResource(R.string.profile_empty_message),
      style = MaterialTheme.typography.bodyLarge,
      color = MaterialTheme.colorScheme.onSurface,
      modifier = Modifier.padding(16.dp)
    )
  }
}