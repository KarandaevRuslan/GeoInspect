package com.karandaev.geo_inspect.core.image.upload

import com.karandaev.geo_inspect.core.image.compression.FileCompressor
import com.karandaev.geo_inspect.core.image.compression.JpegImageFileCompressor
import com.karandaev.geo_inspect.core.image.validation.DefaultImageValidationProvider
import com.karandaev.geo_inspect.core.image.validation.ImageValidationProvider
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

/**
 * Default maximum size of the final file sent to the backend.
 *
 * The value is set to 10 MB.
 */
private const val DefaultMaxFileSizeBytes = 10 * 1024 * 1024

/**
 * Fallback MIME type used when the file extension cannot be mapped to a known image MIME type.
 */
private const val DefaultMimeType = "application/octet-stream"

/**
 * Default multipart form field name expected by most file upload endpoints.
 */
private const val DefaultFormFieldName = "file"

/**
 * Default implementation of [ImageUploadFileProvider].
 *
 * This provider accepts a local source image file path. The source file can then optionally be
 * passed through [fileCompressor] before it is validated and converted into a Retrofit multipart
 * file part.
 *
 * If [fileCompressor] returns `null`, the original source file is uploaded.
 * If [imageValidationProvider] is provided, the source file and final file are validated.
 *
 * This class does not perform any network requests. It only prepares local files for upload.
 *
 * @param fileCompressor Optional compressor applied to the source file before upload.
 * @param imageValidationProvider Optional validator applied to the source and final file.
 * @param maxFileSizeBytes Maximum allowed size of the final upload file in bytes.
 * @param formFieldName Multipart form field name used for the file part.
 */
class DefaultImageUploadFileProvider(
  private val fileCompressor: FileCompressor? = JpegImageFileCompressor(),
  private val imageValidationProvider: ImageValidationProvider? = DefaultImageValidationProvider(),
  private val maxFileSizeBytes: Int = DefaultMaxFileSizeBytes,
  private val formFieldName: String = DefaultFormFieldName
) : ImageUploadFileProvider {

  /**
   * Creates a multipart upload file from the provided local source file path.
   *
   * The method performs the following steps:
   * 1. Resolves and validates the source file.
   * 2. Optionally validates the source image using [imageValidationProvider].
   * 3. Optionally compresses the source file using [fileCompressor].
   * 4. Validates the final file size against [maxFileSizeBytes].
   * 5. Optionally validates the final image using [imageValidationProvider].
   * 6. Converts the final file into an [ImageUploadFile].
   *
   * @param sourcePath Absolute or relative path to the local source image file.
   *
   * @return Upload file containing a Retrofit multipart file part.
   *
   * @throws IllegalArgumentException if the source path is blank, source file does not exist,
   * or the final file is larger than [maxFileSizeBytes].
   */
  override fun create(
    sourcePath: String
  ): ImageUploadFile {
    require(sourcePath.isNotBlank()) {
      "Source path must not be blank."
    }

    val sourceFile = File(sourcePath)

    require(sourceFile.exists()) {
      "Source file does not exist."
    }

    require(sourceFile.isFile) {
      "Source path does not point to a file."
    }

    imageValidationProvider?.validate(sourceFile.absolutePath)

    val finalFile = fileCompressor
      ?.compress(sourceFile.absolutePath)
      ?: sourceFile

    require(finalFile.exists()) {
      "Final upload file does not exist."
    }

    require(finalFile.isFile) {
      "Final upload path does not point to a file."
    }

    require(finalFile.length() <= maxFileSizeBytes) {
      "File is too large. Max size is ${maxFileSizeBytes.toMegabytes()} MB."
    }

    imageValidationProvider?.validate(finalFile.absolutePath)

    val mimeType = finalFile.extension
      .toMimeTypeOrNull()
      ?: DefaultMimeType

    return finalFile.toImageUploadFile(
      mimeType = mimeType
    )
  }

  /**
   * Converts this local file into an [ImageUploadFile].
   *
   * The resulting object contains a Retrofit multipart file part with the configured
   * [formFieldName], current file name, and provided [mimeType].
   *
   * @param mimeType MIME type used as the request body content type.
   *
   * @return Upload file ready to be passed to a Retrofit multipart endpoint.
   */
  private fun File.toImageUploadFile(
    mimeType: String
  ): ImageUploadFile {
    val requestBody = asRequestBody(
      contentType = mimeType.toMediaTypeOrNull()
    )

    val part = MultipartBody.Part.createFormData(
      name = formFieldName,
      filename = name,
      body = requestBody
    )

    return ImageUploadFile(part = part)
  }

  /**
   * Converts bytes to megabytes for user-facing error messages.
   */
  private fun Int.toMegabytes(): Int {
    return this / 1024 / 1024
  }
}