package com.karandaev.geo_inspect.core.json.import_json

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.karandaev.geo_inspect.core.util.other.toast
import kotlinx.coroutines.launch

/**
 * Creates a reusable Android document import action.
 *
 * The returned lambda starts Android document picker flow.
 * Actual reading and importing are performed only after the user chooses a source document.
 *
 * This function is item-type agnostic. It can import notes, settings,
 * or any other payload that can be parsed from text content.
 *
 * @param mimeTypes MIME types passed to Android document picker contract.
 * @param parseContent Parses text content into an import payload.
 * @param onImport Called with parsed import payload.
 * @param successMessage Message shown after successful import.
 * @param failureMessagePrefix Prefix used for import failure messages.
 * @param onImportingChange Called when import starts or finishes.
 * @return Function that launches document import.
 */
@Composable
fun <T> rememberDocumentImporter(
  mimeTypes: Array<String>,
  parseContent: (String) -> T,
  onImport: suspend (T) -> Unit,
  successMessage: String,
  failureMessagePrefix: String = "Import failed",
  onImportingChange: (Boolean) -> Unit = {}
): () -> Unit {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  val currentMimeTypes by rememberUpdatedState(mimeTypes)
  val currentParseContent by rememberUpdatedState(parseContent)
  val currentOnImport by rememberUpdatedState(onImport)
  val currentSuccessMessage by rememberUpdatedState(successMessage)
  val currentFailureMessagePrefix by rememberUpdatedState(failureMessagePrefix)
  val currentOnImportingChange by rememberUpdatedState(onImportingChange)

  val importLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.OpenDocument()
  ) { uri: Uri? ->
    if (uri == null) return@rememberLauncherForActivityResult

    coroutineScope.launch {
      currentOnImportingChange(true)

      runCatching {
        val content = readTextFromDocument(
          context = context,
          uri = uri
        ).getOrThrow()

        val importPayload = currentParseContent(content)

        currentOnImport(importPayload)
      }.onSuccess {
        context.toast(currentSuccessMessage)
      }.onFailure { error ->
        context.toast(
          "$currentFailureMessagePrefix: ${error.message ?: "unknown error"}"
        )
      }

      currentOnImportingChange(false)
    }
  }

  return {
    importLauncher.launch(currentMimeTypes)
  }
}