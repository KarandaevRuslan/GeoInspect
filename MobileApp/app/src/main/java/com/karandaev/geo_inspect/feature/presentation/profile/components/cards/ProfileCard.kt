package com.karandaev.geo_inspect.feature.presentation.profile.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.core.presentation.auth.model.UserProfile
import com.karandaev.geo_inspect.feature.presentation.profile.components.fields.ProfileField
import com.karandaev.geo_inspect.feature.presentation.profile.components.providers.ProfileProvidersField

/**
 * Displays full profile details.
 *
 * @param profile User profile to display.
 * @param enabled Whether editable fields are enabled.
 * @param onDisplayNameEditClick Called when display name edit button is clicked.
 * @param modifier Modifier applied to the card.
 */
@Composable
internal fun ProfileCard(
  profile: UserProfile,
  enabled: Boolean,
  onDisplayNameEditClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val emptyValue = stringResource(R.string.profile_field_empty_value)

  Card(
    modifier = modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier.padding(12.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      ProfileCardHeader(
        title = stringResource(R.string.profile_details_title),
        description = stringResource(R.string.profile_details_copy_hint)
      )

      ProfileField(
        label = stringResource(R.string.profile_field_email),
        value = profile.email.orProfileFieldValue(emptyValue)
      )

      ProfileField(
        label = stringResource(R.string.profile_field_display_name),
        value = profile.displayName.orProfileFieldValue(emptyValue),
        enabled = enabled,
        onEditClick = onDisplayNameEditClick
      )

      ProfileProvidersField(
        providers = profile.providerIds
      )

      HorizontalDivider()

      ProfileField(
        label = stringResource(R.string.profile_field_user_id),
        value = profile.uid
      )
    }
  }
}

private fun String?.orProfileFieldValue(
  emptyValue: String
): String {
  return if (isNullOrBlank()) {
    emptyValue
  } else {
    this
  }
}