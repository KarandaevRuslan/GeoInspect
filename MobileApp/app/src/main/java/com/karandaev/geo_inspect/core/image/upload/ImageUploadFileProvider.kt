package com.karandaev.geo_inspect.core.image.upload

/**
 * Creates upload-ready image files from local source file paths.
 */
interface ImageUploadFileProvider {

  /**
   * Creates a multipart upload file from the provided local source file path.
   *
   * @param sourcePath Absolute or relative path to the local source image file.
   *
   * @return Upload file containing a Retrofit multipart file part.
   */
  fun create(
    sourcePath: String
  ): ImageUploadFile
}