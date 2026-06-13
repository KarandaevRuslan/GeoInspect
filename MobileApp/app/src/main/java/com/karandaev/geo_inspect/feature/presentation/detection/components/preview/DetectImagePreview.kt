package com.karandaev.geo_inspect.feature.presentation.detection.components.preview

import java.io.File
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.karandaev.geo_inspect.core.image.source_file.ImageSourceFile

private val DetectImagePreviewHeight = 260.dp

/**
 * Displays selected detection image preview.
 */
@Composable
internal fun DetectImagePreview(
  imageSource: ImageSourceFile?,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
      .fillMaxWidth()
      .height(DetectImagePreviewHeight),
    shape = MaterialTheme.shapes.medium,
    color = MaterialTheme.colorScheme.surfaceContainerHighest
  ) {
    Box(
      contentAlignment = Alignment.Center
    ) {
      if (imageSource == null) {
        Icon(
          imageVector = Icons.Default.Image,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
      } else {
        AsyncImage(
          model = File(imageSource.sourcePath),
          contentDescription = "Selected image",
          modifier = Modifier.matchParentSize(),
          contentScale = ContentScale.Fit
        )
      }
    }
  }
}