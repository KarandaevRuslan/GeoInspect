package com.karandaev.geo_inspect.core.json.export_json.reports

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.image.report.store_and_resolve.INSPECTION_REPORT_IMAGES_DIRECTORY_NAME
import com.karandaev.geo_inspect.core.image.report.store_and_resolve.InspectionReportImageStorage
import com.karandaev.geo_inspect.core.json.document_tree.copyFromFile
import com.karandaev.geo_inspect.core.json.document_tree.findOrCreateDirectory
import com.karandaev.geo_inspect.core.json.document_tree.replaceFile
import com.karandaev.geo_inspect.core.json.document_tree.writeText
import com.squareup.moshi.Moshi
import java.io.File

private const val JsonMimeType = "application/json"
private const val JpegMimeType = "image/jpeg"
private const val PngMimeType = "image/png"
private const val WebpMimeType = "image/webp"
private const val BinaryMimeType = "application/octet-stream"

suspend fun exportInspectionReportsDirectory(
  context: Context,
  parentDirectoryUri: Uri,
  inspectionReports: List<InspectionReport>,
  moshi: Moshi,
  exportDirectoryName: String = INSPECTION_REPORTS_EXPORT_DIRECTORY_NAME,
  jsonFileName: String = INSPECTION_REPORTS_EXPORT_FILE_NAME
) {
  val parentDirectory = DocumentFile.fromTreeUri(
    context,
    parentDirectoryUri
  ) ?: error("Cannot open export directory")

  val exportDirectory = parentDirectory.findOrCreateDirectory(
    name = exportDirectoryName
  )

  exportDirectory
    .replaceFile(
      mimeType = JsonMimeType,
      fileName = jsonFileName
    )
    .writeText(
      context = context,
      content = buildInspectionReportExportJson(
        inspectionReports = inspectionReports,
        moshi = moshi
      )
    )

  val imagesDirectory = exportDirectory.findOrCreateDirectory(
    name = INSPECTION_REPORT_IMAGES_DIRECTORY_NAME
  )

  inspectionReports.forEach { inspectionReport ->
    exportInspectionReportImage(
      context = context,
      imagesDirectory = imagesDirectory,
      inspectionReport = inspectionReport
    )
  }
}

suspend fun exportInspectionReportDirectory(
  context: Context,
  parentDirectoryUri: Uri,
  inspectionReport: InspectionReport,
  moshi: Moshi
) {
  val parentDirectory = DocumentFile.fromTreeUri(
    context,
    parentDirectoryUri
  ) ?: error("Cannot open export directory")

  val exportDirectory = parentDirectory.findOrCreateDirectory(
    name = inspectionReport.buildExportDirectoryName()
  )

  exportDirectory
    .replaceFile(
      mimeType = JsonMimeType,
      fileName = inspectionReport.buildExportFileName()
    )
    .writeText(
      context = context,
      content = buildInspectionReportExportJson(
        inspectionReport = inspectionReport,
        moshi = moshi
      )
    )

  val imagesDirectory = exportDirectory.findOrCreateDirectory(
    name = INSPECTION_REPORT_IMAGES_DIRECTORY_NAME
  )

  exportInspectionReportImage(
    context = context,
    imagesDirectory = imagesDirectory,
    inspectionReport = inspectionReport
  )
}

private fun exportInspectionReportImage(
  context: Context,
  imagesDirectory: DocumentFile,
  inspectionReport: InspectionReport
) {
  val sourceFile = InspectionReportImageStorage.findDetectionImageFile(
    context = context,
    inspectionReportId = inspectionReport.id
  ) ?: return

  imagesDirectory
    .replaceFile(
      mimeType = sourceFile.imageMimeType(),
      fileName = sourceFile.name
    )
    .copyFromFile(
      context = context,
      sourceFile = sourceFile
    )
}

private fun File.imageMimeType(): String {
  return when (extension.lowercase()) {
    "jpg", "jpeg" -> JpegMimeType
    "png" -> PngMimeType
    "webp" -> WebpMimeType
    else -> BinaryMimeType
  }
}