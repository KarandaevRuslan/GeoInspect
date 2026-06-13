package com.karandaev.geo_inspect.feature.presentation.detection.components.crop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
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
import com.karandaev.geo_inspect.core.image.crop.NormalizedCropBox
import com.karandaev.geo_inspect.core.image.source_file.ImageSourceFile
import com.karandaev.geo_inspect.feature.presentation.detection.components.crop.cropper.DetectImageCropper

private val DetectCropCardPadding = 16.dp
private val DetectCropCardSpacing = 12.dp

/**
 * Card with image cropper and optional inference actions.
 */
@Composable
internal fun DetectCropCard(
  imageSource: ImageSourceFile?,
  enabled: Boolean,
  canDetect: Boolean,
  onCropBoxChange: (NormalizedCropBox?) -> Unit,
  onResetCropClick: () -> Unit,
  onClearImageClick: () -> Unit,
  onDetectClick: () -> Unit,
  modifier: Modifier = Modifier,
  showActions: Boolean = true
) {
  Card(
    modifier = modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier.padding(DetectCropCardPadding),
      verticalArrangement = Arrangement.spacedBy(DetectCropCardSpacing)
    ) {
      Text(
        text = stringResource(R.string.detect_crop_title),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface
      )

      Text(
        text = stringResource(R.string.detect_crop_description),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      if (imageSource != null) {
        DetectImageCropper(
          imageSource = imageSource,
          enabled = enabled,
          onCropBoxChange = onCropBoxChange,
          modifier = Modifier.fillMaxWidth()
        )
      }

      if (showActions) {
        DetectCropActions(
          imageSource = imageSource,
          enabled = enabled,
          canDetect = canDetect,
          onResetCropClick = onResetCropClick,
          onClearImageClick = onClearImageClick,
          onDetectClick = onDetectClick
        )
      }
    }
  }
}

/**
 * Landscape actions card for selected detection image.
 */
@Composable
internal fun DetectCropActionsCard(
  imageSource: ImageSourceFile?,
  enabled: Boolean,
  canDetect: Boolean,
  onResetCropClick: () -> Unit,
  onClearImageClick: () -> Unit,
  onDetectClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier.padding(DetectCropCardPadding),
      verticalArrangement = Arrangement.spacedBy(DetectCropCardSpacing)
    ) {
      DetectCropActions(
        imageSource = imageSource,
        enabled = enabled,
        canDetect = canDetect,
        onResetCropClick = onResetCropClick,
        onClearImageClick = onClearImageClick,
        onDetectClick = onDetectClick
      )
    }
  }
}

/**
 * Crop action buttons shared between portrait and landscape layouts.
 */
@Composable
private fun DetectCropActions(
  imageSource: ImageSourceFile?,
  enabled: Boolean,
  canDetect: Boolean,
  onResetCropClick: () -> Unit,
  onClearImageClick: () -> Unit,
  onDetectClick: () -> Unit
) {
  OutlinedButton(
    enabled = enabled && imageSource != null,
    onClick = onResetCropClick,
    modifier = Modifier.fillMaxWidth()
  ) {
    Icon(
      imageVector = Icons.Default.RestartAlt,
      contentDescription = null
    )

    Text(stringResource(R.string.detect_crop_reset))
  }

  Button(
    enabled = canDetect,
    onClick = onDetectClick,
    modifier = Modifier.fillMaxWidth()
  ) {
    Icon(
      imageVector = Icons.Default.PlayArrow,
      contentDescription = null
    )

    Text(stringResource(R.string.detect_crop_run_detection))
  }

  OutlinedButton(
    enabled = enabled && imageSource != null,
    onClick = onClearImageClick,
    modifier = Modifier.fillMaxWidth()
  ) {
    Icon(
      imageVector = Icons.Default.Close,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.error
    )

    Text(
      text = stringResource(R.string.detect_crop_clear_image),
      color = MaterialTheme.colorScheme.error
    )
  }
}