package com.karandaev.geo_inspect.feature.presentation.profile.components.cropper

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import com.karandaev.geo_inspect.R

/**
 * Visual circular avatar cropper surface.
 *
 * @param imageBitmap Decoded selected image.
 * @param cropperState Current cropper transform state.
 * @param enabled Whether touch gestures are enabled.
 * @param cropperSize Visible cropper viewport size.
 * @param modifier Modifier applied to the outer surface.
 */
@Composable
internal fun ProfileAvatarCropperCanvas(
  imageBitmap: ImageBitmap,
  cropperState: AvatarCropperState,
  enabled: Boolean,
  cropperSize: Dp = AvatarCropperSize,
  modifier: Modifier = Modifier
) {
  val borderColor = MaterialTheme.colorScheme.primary
  val contentDescription = stringResource(
    R.string.profile_avatar_cropper_content_description
  )

  Surface(
    modifier = modifier,
    shape = CircleShape,
    color = MaterialTheme.colorScheme.surfaceContainerHighest,
    tonalElevation = AvatarCropperTonalElevation,
    shadowElevation = AvatarCropperShadowElevation
  ) {
    Box(
      modifier = Modifier.size(
        cropperSize + AvatarCropperOuterPadding * 2
      ),
      contentAlignment = Alignment.Center
    ) {
      Surface(
        modifier = Modifier.size(cropperSize),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.secondaryContainer
      ) {
        Canvas(
          modifier = Modifier
            .size(cropperSize)
            .clip(CircleShape)
            .semantics {
              this.contentDescription = contentDescription
            }
            .pointerInput(
              enabled,
              imageBitmap,
              cropperSize
            ) {
              if (!enabled) return@pointerInput

              detectTransformGestures { _, pan, zoom, _ ->
                cropperState.applyGesture(
                  imageWidth = imageBitmap.width,
                  imageHeight = imageBitmap.height,
                  viewportSize = size.width.toFloat(),
                  pan = pan,
                  zoom = zoom
                )
              }
            }
        ) {
          val viewportSize = size.minDimension

          val placement = calculateAvatarImagePlacement(
            imageWidth = imageBitmap.width,
            imageHeight = imageBitmap.height,
            viewportSize = viewportSize,
            scale = cropperState.scale,
            offset = cropperState.offset
          )

          drawImage(
            image = imageBitmap,
            srcOffset = AvatarImageSrcOffset,
            srcSize = imageBitmap.toIntSize(),
            dstOffset = placement.offset,
            dstSize = placement.size
          )

          drawCircle(
            color = Color.Black.copy(alpha = AvatarCropperScrimAlpha),
            radius = viewportSize / 2f
          )

          drawCircle(
            color = borderColor,
            radius = viewportSize / 2f - AvatarCropperBorderWidth.toPx() / 2f,
            style = Stroke(
              width = AvatarCropperBorderWidth.toPx()
            )
          )
        }
      }
    }
  }
}