package com.karandaev.geo_inspect.core.image.cache

import android.content.Context
import java.io.File

/**
 * Clears only temporary image cache owned by the app.
 *
 * Map cache is not touched because osmdroid cache is stored in filesDir/osmdroid.
 */
internal object ImageCacheCleaner {

  fun clearStartupImageCache(
    context: Context
  ) {
    val cacheDirectory = context.cacheDir

    clearDirectoryContents(
      directory = File(cacheDirectory, ImageCaptureCacheDirectoryName)
    )

    clearDirectoryContents(
      directory = File(cacheDirectory, ImageSourceCacheDirectoryName)
    )

    clearDirectoryContents(
      directory = File(cacheDirectory, CroppedImageCacheDirectoryName)
    )

    clearLegacyRootImageCacheFiles(
      cacheDirectory = cacheDirectory
    )
  }

  private fun clearDirectoryContents(
    directory: File
  ) {
    if (!directory.exists()) return

    directory.listFiles()
      ?.forEach { file ->
        file.deleteRecursively()
      }
  }

  private fun clearLegacyRootImageCacheFiles(
    cacheDirectory: File
  ) {
    cacheDirectory.listFiles()
      ?.filter { file ->
        file.isFile && LegacyRootImageCacheFilePrefixes.any { prefix ->
          file.name.startsWith(prefix)
        }
      }
      ?.forEach { file ->
        file.delete()
      }
  }
}