package com.karandaev.geo_inspect.core.image.source_file

import android.net.Uri
import com.karandaev.geo_inspect.core.image.crop.NormalizedCropBox

/**
 * Local image source selected by the user.
 *
 * @param uri Original Android content URI selected by the user.
 * @param originalSourcePath Local path to the original copied image file.
 * @param sourcePath Local path currently used for detection/upload. Can point to original or cropped file.
 * @param originalWidth Width of the original source image in pixels.
 * @param originalHeight Height of the original source image in pixels.
 * @param cropBox Normalized crop box relative to the original image, or null if no crop is applied.
 */
data class ImageSourceFile(
  val uri: Uri,
  val originalSourcePath: String,
  val sourcePath: String,
  val originalWidth: Int,
  val originalHeight: Int,
  val cropBox: NormalizedCropBox? = null
)