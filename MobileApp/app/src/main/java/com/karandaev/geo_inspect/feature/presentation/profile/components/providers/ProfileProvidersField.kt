package com.karandaev.geo_inspect.feature.presentation.profile.components.providers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.feature.presentation.profile.components.copy.ProfileCopyableContainer

/**
 * Displays auth providers as copyable badges.
 *
 * Clicking the section copies all providers as a comma-separated list.
 *
 * @param providers Provider ids linked to the current account.
 * @param modifier Modifier applied to the root.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ProfileProvidersField(
  providers: List<String>,
  modifier: Modifier = Modifier
) {
  val normalizedProviders = providers.normalizedProviderIds()
  val providerText = normalizedProviders.joinToString()
  val canCopy = providerText.isNotBlank()

  ProfileCopyableContainer(
    copyText = providerText,
    copiedMessage = stringResource(R.string.profile_providers_copied),
    enabled = canCopy,
    modifier = modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier.padding(
        horizontal = 4.dp,
        vertical = 6.dp
      ),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Text(
        text = stringResource(R.string.profile_field_providers),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      if (normalizedProviders.isEmpty()) {
        Text(
          text = stringResource(R.string.profile_field_empty_value),
          style = MaterialTheme.typography.bodyLarge,
          color = MaterialTheme.colorScheme.onSurface
        )
      } else {
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          normalizedProviders.forEach { provider ->
            ProfileProviderBadge(
              providerId = provider
            )
          }
        }
      }
    }
  }
}

private fun List<String>.normalizedProviderIds(): List<String> {
  return filter { provider -> provider.isNotBlank() }
    .distinct()
}