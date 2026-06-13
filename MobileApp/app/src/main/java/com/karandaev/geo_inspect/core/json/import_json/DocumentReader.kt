package com.karandaev.geo_inspect.core.json.import_json

import android.content.Context
import android.net.Uri

/**
 * Reads text content from an Android document URI.
 *
 * @param context Android context used to access the content resolver.
 * @param uri Source document URI selected by the user.
 * @return Result containing document text or read failure.
 */
fun readTextFromDocument(
  context: Context,
  uri: Uri
): Result<String> {
  return runCatching {
    context.contentResolver.openInputStream(uri)?.use { stream ->
      stream.readBytes().toString(Charsets.UTF_8)
    } ?: error("Cannot open import file")
  }
}