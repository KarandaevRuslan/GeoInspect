package com.karandaev.geo_inspect.feature.ui.components.detection_preview.dialog.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.feature.ui.components.detection_preview.dialog.DetectionPreviewDetailsItemSpacing

/**
 * Compact visual section for details panel.
 */
@Composable
internal fun DetectionPreviewDetailsSection(
  title: String,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    shape = MaterialTheme.shapes.medium,
    tonalElevation = 1.dp
  ) {
    Column(
      modifier = Modifier.padding(10.dp),
      verticalArrangement = Arrangement.spacedBy(DetectionPreviewDetailsItemSpacing)
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface
      )

      HorizontalDivider()

      content()
    }
  }
}

/**
 * Shared compact body text for details panel.
 */
@Composable
internal fun DetectionPreviewDetailsText(
  text: String,
  modifier: Modifier = Modifier
) {
  Text(
    text = text,
    style = MaterialTheme.typography.bodySmall,
    color = MaterialTheme.colorScheme.onSurfaceVariant,
    modifier = modifier
  )
}