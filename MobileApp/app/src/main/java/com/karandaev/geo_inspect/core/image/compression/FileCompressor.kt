package com.karandaev.geo_inspect.core.image.compression

import java.io.File

/**
 * Compresses files before upload when compression is supported.
 */
interface FileCompressor {

  /**
   * Compresses the file located at the provided path.
   *
   * @param sourcePath Path to the source file.
   *
   * @return Compressed file, or `null` when the file format is not supported.
   */
  fun compress(sourcePath: String): File?
}