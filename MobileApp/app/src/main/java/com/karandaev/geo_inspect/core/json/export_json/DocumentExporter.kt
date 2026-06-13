package com.karandaev.geo_inspect.core.json.export_json

import android.content.Context
import android.net.Uri

/**
 * Writes text content into an Android document URI.
 *
 * @param context Android context used to access the content resolver.
 * @param uri Destination document URI selected by the user.
 * @param content Text content that should be written into the document.
 * @return Result describing whether the write operation succeeded or failed.
 */
fun writeTextToDocument(
  context: Context,
  uri: Uri,
  content: String
): Result<Unit> {
  return runCatching {
    context.contentResolver.openOutputStream(uri)?.use { stream ->
      stream.write(content.toByteArray(Charsets.UTF_8))
    } ?: error("Cannot open export file")
  }
}