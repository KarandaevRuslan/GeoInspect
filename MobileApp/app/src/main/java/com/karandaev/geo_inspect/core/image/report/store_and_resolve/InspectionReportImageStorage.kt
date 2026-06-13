package com.karandaev.geo_inspect.core.image.report.store_and_resolve

import android.content.Context
import java.io.File
import java.io.InputStream

const val INSPECTION_REPORT_IMAGES_DIRECTORY_NAME = "reports"

private const val DefaultInspectionReportImageExtension = "jpg"

val SupportedInspectionReportImageExtensions = setOf(
  "jpg",
  "jpeg",
  "png",
  "webp"
)

/**
 * Storage helper for inspection report detection images.
 *
 * All report images are stored in app internal storage:
 * `filesDir/reports/{inspectionReportId}.{extension}`.
 */
object InspectionReportImageStorage {

  fun findDetectionImagePath(
    context: Context,
    inspectionReportId: Long
  ): String? {
    return findDetectionImageFile(
      context = context,
      inspectionReportId = inspectionReportId
    )?.absolutePath
  }

  fun findDetectionImageFile(
    context: Context,
    inspectionReportId: Long
  ): File? {
    val reportsDirectory = reportsDirectory(context)

    return findExtensionlessImageFile(
      reportsDirectory = reportsDirectory,
      inspectionReportId = inspectionReportId
    ) ?: findImageFileWithSupportedExtension(
      reportsDirectory = reportsDirectory,
      inspectionReportId = inspectionReportId
    )
  }

  fun saveDetectionImageFile(
    context: Context,
    inspectionReportId: Long,
    sourcePath: String?
  ): String? {
    if (sourcePath.isNullOrBlank()) {
      return null
    }

    val sourceFile = File(sourcePath)

    if (!sourceFile.exists() || !sourceFile.isFile) {
      return null
    }

    val targetFile = createTargetImageFile(
      context = context,
      inspectionReportId = inspectionReportId,
      sourceFileName = sourceFile.name
    )

    if (sourceFile.absolutePath == targetFile.absolutePath) {
      return targetFile.absolutePath
    }

    deleteDetectionImageFiles(
      context = context,
      inspectionReportId = inspectionReportId
    )

    sourceFile.inputStream().use { input ->
      copyImageFile(
        input = input,
        targetFile = targetFile
      )
    }

    return targetFile.absolutePath
  }

  fun saveDetectionImageFile(
    context: Context,
    inspectionReportId: Long,
    sourceFileName: String,
    input: InputStream
  ): String? {
    val targetFile = createTargetImageFile(
      context = context,
      inspectionReportId = inspectionReportId,
      sourceFileName = sourceFileName
    )

    deleteDetectionImageFiles(
      context = context,
      inspectionReportId = inspectionReportId
    )

    copyImageFile(
      input = input,
      targetFile = targetFile
    )

    return targetFile.absolutePath
  }

  fun deleteDetectionImageFiles(
    context: Context,
    inspectionReportId: Long
  ) {
    val reportsDirectory = reportsDirectory(context)

    File(
      reportsDirectory,
      inspectionReportId.toString()
    ).delete()

    SupportedInspectionReportImageExtensions.forEach { extension ->
      File(
        reportsDirectory,
        "$inspectionReportId.$extension"
      ).delete()
    }
  }

  private fun reportsDirectory(context: Context): File {
    return File(
      context.filesDir,
      INSPECTION_REPORT_IMAGES_DIRECTORY_NAME
    )
  }

  private fun findExtensionlessImageFile(
    reportsDirectory: File,
    inspectionReportId: Long
  ): File? {
    return File(
      reportsDirectory,
      inspectionReportId.toString()
    ).takeIf { file ->
      file.exists() && file.isFile
    }
  }

  private fun findImageFileWithSupportedExtension(
    reportsDirectory: File,
    inspectionReportId: Long
  ): File? {
    return SupportedInspectionReportImageExtensions
      .asSequence()
      .map { extension ->
        File(
          reportsDirectory,
          "$inspectionReportId.$extension"
        )
      }
      .firstOrNull { file ->
        file.exists() && file.isFile
      }
  }

  private fun createTargetImageFile(
    context: Context,
    inspectionReportId: Long,
    sourceFileName: String
  ): File {
    val reportsDirectory = reportsDirectory(context).apply {
      mkdirs()
    }

    return File(
      reportsDirectory,
      "$inspectionReportId.${sourceFileName.safeImageExtension()}"
    )
  }

  private fun copyImageFile(
    input: InputStream,
    targetFile: File
  ) {
    targetFile.outputStream().use { output ->
      input.copyTo(output)
    }
  }

  private fun String.safeImageExtension(): String {
    val normalizedExtension = substringAfterLast(
      delimiter = ".",
      missingDelimiterValue = ""
    ).lowercase()

    return if (normalizedExtension in SupportedInspectionReportImageExtensions) {
      normalizedExtension
    } else {
      DefaultInspectionReportImageExtension
    }
  }
}