package com.karandaev.geo_inspect.feature.presentation.settings.components.sections

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Visual section container used on the settings screen.
 *
 * @param title Section title displayed above the card.
 * @param content Section content displayed inside the card.
 */
@Composable
internal fun SettingsSection(
  title: String,
  content: @Composable () -> Unit
) {
  Column {
    Text(
      text = title,
      style = MaterialTheme.typography.labelLarge,
      color = MaterialTheme.colorScheme.primary,
      modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
    )

    Card(
      shape = RoundedCornerShape(12.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
      ),
      border = BorderStroke(
        width = 1.dp,
        color = MaterialTheme.colorScheme.outlineVariant
      )
    ) {
      content()
    }
  }
}