package com.karandaev.geo_inspect.core.json.document_tree

import android.content.Context
import androidx.documentfile.provider.DocumentFile
import java.io.File

fun DocumentFile.findOrCreateDirectory(
  name: String
): DocumentFile {
  findFile(name)
    ?.takeIf { file -> file.isDirectory }
    ?.let { directory -> return directory }

  return createDirectory(name)
    ?: error("Cannot create directory: $name")
}

fun DocumentFile.replaceFile(
  mimeType: String,
  fileName: String
): DocumentFile {
  findFile(fileName)?.delete()

  return createFile(
    mimeType,
    fileName
  ) ?: error("Cannot create file: $fileName")
}

fun DocumentFile.findFileWithExtensions(
  baseName: String,
  extensions: Set<String>
): DocumentFile? {
  return extensions
    .asSequence()
    .mapNotNull { extension ->
      findFile("$baseName.$extension")
    }
    .firstOrNull { file ->
      file.isFile
    }
}

fun DocumentFile.writeText(
  context: Context,
  content: String
) {
  context.contentResolver.openOutputStream(uri)?.use { output ->
    output.write(content.toByteArray(Charsets.UTF_8))
  } ?: error("Cannot open output file: $name")
}

fun DocumentFile.readText(
  context: Context
): String {
  return context.contentResolver.openInputStream(uri)?.use { input ->
    input.readBytes().toString(Charsets.UTF_8)
  } ?: error("Cannot open input file: $name")
}

fun DocumentFile.copyFromFile(
  context: Context,
  sourceFile: File
) {
  context.contentResolver.openOutputStream(uri)?.use { output ->
    sourceFile.inputStream().use { input ->
      input.copyTo(output)
    }
  } ?: error("Cannot open output file: $name")
}