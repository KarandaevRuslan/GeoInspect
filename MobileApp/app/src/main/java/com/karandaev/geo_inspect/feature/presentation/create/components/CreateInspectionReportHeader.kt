package com.karandaev.geo_inspect.feature.presentation.create.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.app.components.AppBackButton

@Composable
internal fun CreateInspectionReportHeader(
  title: String,
  onBackClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    AppBackButton(
      onClick = onBackClick
    )

    Text(
      text = title,
      style = MaterialTheme.typography.headlineSmall,
      color = MaterialTheme.colorScheme.onSurface,
      modifier = Modifier.weight(1f)
    )
  }
}