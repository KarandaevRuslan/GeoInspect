package com.karandaev.geo_inspect.core.presentation.launcher

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.karandaev.geo_inspect.core.image.cache.ImageCaptureCacheDirectoryName
import com.karandaev.geo_inspect.core.image.cache.ImageCaptureFilePrefix
import java.io.File

private const val TempImageCaptureFileExtension = ".jpg"

internal fun createTempImageCaptureUri(
  context: Context
): Uri {
  val directory = File(
    context.cacheDir,
    ImageCaptureCacheDirectoryName
  ).apply {
    mkdirs()
  }

  val file = File(
    directory,
    "$ImageCaptureFilePrefix${System.currentTimeMillis()}$TempImageCaptureFileExtension"
  )

  return FileProvider.getUriForFile(
    context,
    "${context.packageName}.fileprovider",
    file
  )
}