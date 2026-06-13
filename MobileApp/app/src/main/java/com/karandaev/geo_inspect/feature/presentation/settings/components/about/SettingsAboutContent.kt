package com.karandaev.geo_inspect.feature.presentation.settings.components.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R

/**
 * Displays static information about the application.
 */
@Composable
internal fun SettingsAboutContent(
  appVersionName: String
) {
  Column(
    modifier = Modifier.padding(16.dp)
  ) {
    Text(
      text = stringResource(
        id = R.string.settings_about_app_title,
        appVersionName
      ),
      style = MaterialTheme.typography.titleMedium,
      color = MaterialTheme.colorScheme.onSurface
    )

    Text(
      text = stringResource(R.string.settings_about_app_description),
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.padding(top = 4.dp)
    )

    Text(
      text = stringResource(R.string.settings_about_app_developer),
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.padding(top = 8.dp)
    )
  }
}