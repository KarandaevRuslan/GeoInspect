package com.karandaev.geo_inspect.feature.presentation.profile.components.fields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.feature.presentation.profile.components.copy.ProfileCopyableContainer

private const val EmptyProfileFieldValue = "-"
private val ProfileFieldEditIconSize = 24.dp

/**
 * Displays a profile field.
 *
 * The left content copies value to clipboard. Optional trailing edit button opens edit dialog.
 *
 * @param label Field label.
 * @param value Field value.
 * @param enabled Whether edit action is enabled.
 * @param onEditClick Optional edit action.
 * @param modifier Modifier applied to the root.
 */
@Composable
internal fun ProfileField(
  label: String,
  value: String,
  enabled: Boolean = true,
  onEditClick: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    CopyableProfileFieldContent(
      label = label,
      value = value,
      modifier = Modifier.weight(1f)
    )

    if (onEditClick != null) {
      IconButton(
        enabled = enabled,
        onClick = onEditClick
      ) {
        Icon(
          imageVector = Icons.Default.Edit,
          contentDescription = stringResource(
            R.string.profile_field_edit_content_description,
            label
          ),
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(ProfileFieldEditIconSize)
        )
      }
    }
  }
}

@Composable
private fun CopyableProfileFieldContent(
  label: String,
  value: String,
  modifier: Modifier = Modifier
) {
  ProfileCopyableContainer(
    copyText = value,
    copiedMessage = stringResource(
      R.string.profile_field_copied,
      label
    ),
    enabled = value != EmptyProfileFieldValue,
    modifier = modifier
  ) {
    Column(
      modifier = Modifier.padding(
        horizontal = 4.dp,
        vertical = 6.dp
      ),
      verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
      Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Text(
        text = value,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface
      )
    }
  }
}