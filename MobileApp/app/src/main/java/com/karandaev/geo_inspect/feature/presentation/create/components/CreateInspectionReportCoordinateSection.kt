package com.karandaev.geo_inspect.feature.presentation.create.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import com.karandaev.geo_inspect.core.ui.components.double_picker.DoublePicker

/**
 * Manual coordinate editing section.
 *
 * @param point Current effective map point.
 * @param enabled Whether coordinate inputs are enabled.
 * @param onLatitudeChange Called when latitude value changes.
 * @param onLongitudeChange Called when longitude value changes.
 * @param modifier Modifier applied to the section root.
 */
@Composable
internal fun CreateNoteCoordinateSection(
  point: MapPoint?,
  enabled: Boolean,
  onLatitudeChange: (Double, Double) -> Unit,
  onLongitudeChange: (Double, Double) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier.padding(top = 4.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    if (point != null) {
      DoublePicker(
        label = "Latitude",
        value = point.latitude,
        onValueChange = { newLatitude ->
          onLatitudeChange(newLatitude, point.longitude)
        },
        min = -90.0,
        max = 90.0,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth()
      )

      DoublePicker(
        label = "Longitude",
        value = point.longitude,
        onValueChange = { newLongitude ->
          onLongitudeChange(newLongitude, point.latitude)
        },
        min = -180.0,
        max = 180.0,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth()
      )
    } else {
      Text(
        text = "Location is not available yet...",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}