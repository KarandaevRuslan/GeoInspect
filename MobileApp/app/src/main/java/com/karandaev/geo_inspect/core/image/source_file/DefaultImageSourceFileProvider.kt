package com.karandaev.geo_inspect.core.image.source_file

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import com.karandaev.geo_inspect.core.image.cache.ImageSourceCacheDirectoryName
import com.karandaev.geo_inspect.core.image.cache.ImageSourceFilePrefix
import com.karandaev.geo_inspect.core.image.upload.toFileExtension
import java.io.File
import java.io.FileOutputStream

/**
 * Fallback MIME type used when Android cannot resolve the MIME type of the selected content.
 */
private const val DefaultMimeType = "application/octet-stream"

/**
 * Default implementation of [ImageSourceFileProvider].
 *
 * Copies Android content URI data into the app image source cache directory
 * and returns local file metadata.
 */
class DefaultImageSourceFileProvider : ImageSourceFileProvider {

  override fun createSourceFile(
    context: Context,
    uri: Uri
  ): ImageSourceFile {
    val sourcePath = copyUriToCacheFile(
      context = context,
      uri = uri
    )

    val dimensions = readImageDimensions(sourcePath)

    return ImageSourceFile(
      uri = uri,
      originalSourcePath = sourcePath,
      sourcePath = sourcePath,
      originalWidth = dimensions.width,
      originalHeight = dimensions.height,
      cropBox = null
    )
  }

  private fun copyUriToCacheFile(
    context: Context,
    uri: Uri
  ): String {
    val mimeType = context.contentResolver.getType(uri) ?: DefaultMimeType
    val extension = mimeType.toFileExtension()

    val directory = File(
      context.cacheDir,
      ImageSourceCacheDirectoryName
    ).apply {
      mkdirs()
    }

    val file = File(
      directory,
      "$ImageSourceFilePrefix${System.currentTimeMillis()}.$extension"
    )

    context.contentResolver
      .openInputStream(uri)
      ?.use { input ->
        FileOutputStream(file).use { output ->
          input.copyTo(output)
        }
      }
      ?: error("Could not read the selected file.")

    return file.absolutePath
  }

  private fun readImageDimensions(sourcePath: String): ImageDimensions {
    val options = BitmapFactory.Options().apply {
      inJustDecodeBounds = true
    }

    BitmapFactory.decodeFile(sourcePath, options)

    require(options.outWidth > 0 && options.outHeight > 0) {
      "Could not read image dimensions."
    }

    return ImageDimensions(
      width = options.outWidth,
      height = options.outHeight
    )
  }

  private data class ImageDimensions(
    val width: Int,
    val height: Int
  )
}