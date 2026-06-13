package com.karandaev.geo_inspect.feature.presentation.detection.components.navigation

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
 * Detection screen header with back navigation.
 *
 * @param onBackClick Called when back button is clicked.
 * @param modifier Modifier applied to root row.
 */
@Composable
internal fun DetectHeader(
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
      text = stringResource(R.string.destination_detection),
      style = MaterialTheme.typography.headlineSmall,
      color = MaterialTheme.colorScheme.onSurface,
      modifier = Modifier.weight(1f)
    )
  }
}