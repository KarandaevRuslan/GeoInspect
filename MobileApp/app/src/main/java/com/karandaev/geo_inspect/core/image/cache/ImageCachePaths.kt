package com.karandaev.geo_inspect.core.image.cache

internal const val ImageCaptureCacheDirectoryName = "image_capture"
internal const val ImageSourceCacheDirectoryName = "image_source"
internal const val CroppedImageCacheDirectoryName = "image_cropped"

internal const val ImageCaptureFilePrefix = "image_capture_"
internal const val ImageSourceFilePrefix = "upload_"
internal const val CroppedImageFilePrefix = "cropped_"
internal const val CompressedImageFilePrefix = "compressed_"

internal val LegacyRootImageCacheFilePrefixes = listOf(
  ImageCaptureFilePrefix,
  ImageSourceFilePrefix,
  CroppedImageFilePrefix,
  CompressedImageFilePrefix
)