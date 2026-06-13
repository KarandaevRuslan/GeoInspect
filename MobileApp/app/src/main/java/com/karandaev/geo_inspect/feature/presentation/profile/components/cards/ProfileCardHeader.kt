package com.karandaev.geo_inspect.feature.presentation.profile.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val ProfileCardHeaderIndicatorWidth = 4.dp
private val ProfileCardHeaderIndicatorMinHeight = 42.dp

/**
 * Common visually separated header for profile cards.
 *
 * @param title Header title.
 * @param description Header description.
 * @param modifier Modifier applied to the root.
 */
@Composable
internal fun ProfileCardHeader(
  title: String,
  description: String,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(
        horizontal = 4.dp,
        vertical = 6.dp
      ),
    horizontalArrangement = Arrangement.spacedBy(10.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Surface(
      modifier = Modifier
        .width(ProfileCardHeaderIndicatorWidth)
        .heightIn(min = ProfileCardHeaderIndicatorMinHeight),
      shape = RoundedCornerShape(50),
      color = MaterialTheme.colorScheme.primary
    ) {}

    Column(
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface
      )

      Text(
        text = description,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}