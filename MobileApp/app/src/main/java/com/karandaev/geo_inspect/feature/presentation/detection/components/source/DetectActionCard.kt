package com.karandaev.geo_inspect.feature.presentation.detection.components.source

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R

private val DetectActionCardPadding = 16.dp
private val DetectActionCardSpacing = 12.dp

/**
 * Card with image source actions.
 */
@Composable
internal fun DetectActionCard(
  enabled: Boolean,
  onTakePhotoClick: () -> Unit,
  onChooseImageClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier.padding(DetectActionCardPadding),
      verticalArrangement = Arrangement.spacedBy(DetectActionCardSpacing)
    ) {
      Text(
        text = stringResource(R.string.detect_source_title),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface
      )

      Text(
        text = stringResource(R.string.detect_source_description),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Button(
        enabled = enabled,
        onClick = onTakePhotoClick,
        modifier = Modifier.fillMaxWidth()
      ) {
        Icon(
          imageVector = Icons.Default.PhotoCamera,
          contentDescription = null
        )

        Text(stringResource(R.string.detect_source_take_photo))
      }

      OutlinedButton(
        enabled = enabled,
        onClick = onChooseImageClick,
        modifier = Modifier.fillMaxWidth()
      ) {
        Icon(
          imageVector = Icons.Default.AddPhotoAlternate,
          contentDescription = null
        )

        Text(stringResource(R.string.detect_source_choose_from_gallery))
      }
    }
  }
}