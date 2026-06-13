package com.karandaev.geo_inspect.feature.presentation.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.app.components.AppBackButton
import com.karandaev.geo_inspect.core.domain.model.InspectionReport

@Composable
internal fun ViewInspectionReportContentSection(
  inspectionReport: InspectionReport,
  lastUpdatedText: String,
  modifier: Modifier = Modifier,
  showTitle: Boolean = false,
  showBackButton: Boolean = false,
  onBackClick: () -> Unit = {}
) {
  val hasContent = inspectionReport.content.isNotBlank()

  Column(
    modifier = modifier
  ) {
    if (showTitle) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        if (showBackButton) {
          AppBackButton(
            onClick = onBackClick
          )
        }

        Text(
          text = inspectionReport.title,
          style = MaterialTheme.typography.headlineSmall,
          color = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.weight(1f)
        )
      }
    }

    ViewInspectionReportLastUpdatedRow(
      text = lastUpdatedText,
      modifier = Modifier.padding(
        top = if (showTitle) 8.dp else 0.dp
      )
    )

    if (hasContent) {
      Text(
        text = inspectionReport.content,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(top = 16.dp)
      )
    }
  }
}