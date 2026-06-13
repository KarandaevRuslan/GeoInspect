package com.karandaev.geo_inspect.core.image.source_file

import android.content.Context
import android.net.Uri

/**
 * Creates local source image files from Android content URIs.
 */
interface ImageSourceFileProvider {

  /**
   * Copies the content referenced by [uri] into a local file and returns image metadata.
   *
   * @param context Android context used to access the content resolver and cache directory.
   * @param uri URI of the selected content.
   *
   * @return Local image source file metadata.
   */
  fun createSourceFile(
    context: Context,
    uri: Uri
  ): ImageSourceFile
}