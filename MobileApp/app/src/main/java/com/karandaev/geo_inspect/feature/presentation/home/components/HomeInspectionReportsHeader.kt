package com.karandaev.geo_inspect.feature.presentation.home.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import com.karandaev.geo_inspect.R

/**
 * Header displayed above the user's notes list.
 *
 * @param modifier Modifier applied to the header text.
 */
@Composable
internal fun HomeInspectionReportsHeader(
  modifier: Modifier = Modifier
) {
  Text(
    text = stringResource(id = R.string.home_inspection_reports_section_title),
    style = MaterialTheme.typography.labelSmall,
    color = MaterialTheme.colorScheme.onSurfaceVariant,
    modifier = modifier.padding(
      start = 4.dp,
      top = 4.dp
    )
  )
}