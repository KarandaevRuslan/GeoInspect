package com.karandaev.geo_inspect.core.json.import_json.reports

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.image.report.store_and_resolve.INSPECTION_REPORT_IMAGES_DIRECTORY_NAME
import com.karandaev.geo_inspect.core.image.report.store_and_resolve.InspectionReportImageStorage
import com.karandaev.geo_inspect.core.image.report.store_and_resolve.SupportedInspectionReportImageExtensions
import com.karandaev.geo_inspect.core.json.document_tree.findFileWithExtensions
import com.karandaev.geo_inspect.core.json.document_tree.readText
import com.karandaev.geo_inspect.core.json.export_json.reports.INSPECTION_REPORTS_EXPORT_FILE_NAME
import com.squareup.moshi.Moshi

suspend fun importInspectionReportsDirectory(
  context: Context,
  directoryUri: Uri,
  moshi: Moshi
): InspectionReportsDirectoryImport {
  val directory = DocumentFile.fromTreeUri(
    context,
    directoryUri
  ) ?: error("Cannot open import directory")

  val jsonFile = directory.findFile(INSPECTION_REPORTS_EXPORT_FILE_NAME)
    ?: directory.findFirstJsonFile()
    ?: error("Import directory does not contain JSON file")

  val exportedInspectionReports = parseInspectionReportsImportJson(
    json = jsonFile.readText(context),
    moshi = moshi,
    preserveIds = true
  )

  val imagesDirectory = directory.findFile(
    INSPECTION_REPORT_IMAGES_DIRECTORY_NAME
  )?.takeIf { file ->
    file.isDirectory
  }

  return InspectionReportsDirectoryImport(
    items = exportedInspectionReports.map { exportedInspectionReport ->
      ImportedInspectionReport(
        exportedInspectionReportId = exportedInspectionReport.id,
        inspectionReport = exportedInspectionReport.copy(id = 0L),
        imageFile = imagesDirectory?.findFileWithExtensions(
          baseName = exportedInspectionReport.id.toString(),
          extensions = SupportedInspectionReportImageExtensions
        )
      )
    }
  )
}

data class InspectionReportsDirectoryImport(
  val items: List<ImportedInspectionReport>
) {

  val inspectionReports: List<InspectionReport>
    get() = items.map { item -> item.inspectionReport }

  fun saveImagesForInsertedReports(
    context: Context,
    insertedInspectionReportIds: List<Long>
  ) {
    require(insertedInspectionReportIds.size == items.size) {
      "Inserted report ids count must match imported reports count."
    }

    items.zip(insertedInspectionReportIds).forEach { (item, insertedInspectionReportId) ->
      item.saveImageForInsertedReport(
        context = context,
        insertedInspectionReportId = insertedInspectionReportId
      )
    }
  }
}

data class ImportedInspectionReport(
  val exportedInspectionReportId: Long,
  val inspectionReport: InspectionReport,
  val imageFile: DocumentFile?
) {

  fun saveImageForInsertedReport(
    context: Context,
    insertedInspectionReportId: Long
  ) {
    imageFile?.copyToInternalReportImage(
      context = context,
      inspectionReportId = insertedInspectionReportId
    )
  }
}

private fun DocumentFile.findFirstJsonFile(): DocumentFile? {
  return listFiles()
    .firstOrNull { file ->
      file.isFile && file.name?.endsWith(".json", ignoreCase = true) == true
    }
}

private fun DocumentFile.copyToInternalReportImage(
  context: Context,
  inspectionReportId: Long
) {
  val fileName = name ?: return

  context.contentResolver.openInputStream(uri)?.use { input ->
    InspectionReportImageStorage.saveDetectionImageFile(
      context = context,
      inspectionReportId = inspectionReportId,
      sourceFileName = fileName,
      input = input
    )
  }
}