package com.karandaev.geo_inspect.core.json.export_json

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.karandaev.geo_inspect.core.util.other.toast
import kotlinx.coroutines.launch

/**
 * Creates a reusable Android document export action.
 *
 * The returned lambda starts Android document creation flow for the provided item.
 * Actual writing is performed only after the user chooses a destination document.
 *
 * This function is item-type agnostic. It can export a single note, a list of notes,
 * or any other payload that can be converted into text content.
 *
 * @param mimeType MIME type passed to Android document creation contract.
 * @param buildFileName Builds the default file name for the exported item.
 * @param buildContent Builds text content that should be written into the document.
 * @param successMessage Message shown after successful export.
 * @param failureMessagePrefix Prefix used for export failure messages.
 * @param onExportingChange Called when export writing starts or finishes.
 * @return Function that launches document export for the provided item.
 */
@Composable
fun <T> rememberDocumentExporter(
  mimeType: String,
  buildFileName: (T) -> String,
  buildContent: (T) -> String,
  successMessage: String,
  failureMessagePrefix: String = "Export failed",
  onExportingChange: (Boolean) -> Unit = {}
): (T) -> Unit {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  val currentBuildFileName by rememberUpdatedState(buildFileName)
  val currentBuildContent by rememberUpdatedState(buildContent)
  val currentSuccessMessage by rememberUpdatedState(successMessage)
  val currentFailureMessagePrefix by rememberUpdatedState(failureMessagePrefix)
  val currentOnExportingChange by rememberUpdatedState(onExportingChange)

  var pendingExportItem by remember {
    mutableStateOf<T?>(null)
  }

  val exportLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.CreateDocument(mimeType)
  ) { uri: Uri? ->
    val item = pendingExportItem

    if (uri == null || item == null) {
      pendingExportItem = null
      return@rememberLauncherForActivityResult
    }

    coroutineScope.launch {
      currentOnExportingChange(true)

      runCatching {
        val content = currentBuildContent(item)

        writeTextToDocument(
          context = context,
          uri = uri,
          content = content
        ).getOrThrow()
      }.onSuccess {
        context.toast(currentSuccessMessage)
      }.onFailure { error ->
        context.toast(
          "$currentFailureMessagePrefix: ${error.message ?: "unknown error"}"
        )
      }

      pendingExportItem = null
      currentOnExportingChange(false)
    }
  }

  return { item ->
    pendingExportItem = item
    exportLauncher.launch(currentBuildFileName(item))
  }
}