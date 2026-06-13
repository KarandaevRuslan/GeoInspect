package com.karandaev.geo_inspect.core.ui.components.card

import androidx.annotation.DrawableRes as DrawableResource
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest

/**
 * Generic media source for card leading/trailing/hero content.
 */
sealed interface AppCardMedia {

  /**
   * No media is available.
   */
  data object None : AppCardMedia

  /**
   * Vector icon.
   */
  data class Icon(
    val imageVector: ImageVector,
    val tint: Color? = null
  ) : AppCardMedia

  /**
   * Drawable resource.
   */
  data class DrawableRes(
    @DrawableResource val value: Int
  ) : AppCardMedia

  /**
   * Remote image URL.
   */
  data class Url(
    val value: String
  ) : AppCardMedia

  /**
   * Any Coil-supported image model: Uri, File, Bitmap, Drawable, resource id, etc.
   */
  data class Model(
    val value: Any
  ) : AppCardMedia

  /**
   * Fully custom composable media.
   */
  data class Custom(
    val content: @Composable (Modifier) -> Unit
  ) : AppCardMedia
}

@Composable
fun AppCardMediaContent(
  media: AppCardMedia,
  modifier: Modifier = Modifier,
  contentDescription: String? = null,
  fallbackIcon: ImageVector? = null,
  fallbackTint: Color = androidx.compose.material3.LocalContentColor.current,
  contentScale: ContentScale = ContentScale.Fit
) {
  when (media) {
    AppCardMedia.None -> {
      if (fallbackIcon != null) {
        Icon(
          imageVector = fallbackIcon,
          contentDescription = contentDescription,
          tint = fallbackTint,
          modifier = modifier
        )
      }
    }

    is AppCardMedia.Icon -> {
      Icon(
        imageVector = media.imageVector,
        contentDescription = contentDescription,
        tint = media.tint ?: fallbackTint,
        modifier = modifier
      )
    }

    is AppCardMedia.Custom -> {
      media.content(modifier)
    }

    else -> {
      val model = when (media) {
        is AppCardMedia.DrawableRes -> media.value
        is AppCardMedia.Url -> media.value
        is AppCardMedia.Model -> media.value
        AppCardMedia.None,
        is AppCardMedia.Icon,
        is AppCardMedia.Custom -> null
      }

      SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
          .data(model)
          .crossfade(true)
          .build(),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
      ) {
        val state = painter.state

        if (state is AsyncImagePainter.State.Error || state is AsyncImagePainter.State.Empty) {
          if (fallbackIcon != null) {
            Icon(
              imageVector = fallbackIcon,
              contentDescription = null,
              tint = fallbackTint,
              modifier = modifier
            )
          }
        } else {
          SubcomposeAsyncImageContent()
        }
      }
    }
  }
}