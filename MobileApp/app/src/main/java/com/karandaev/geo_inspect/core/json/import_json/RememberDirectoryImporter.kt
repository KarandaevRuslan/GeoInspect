package com.karandaev.geo_inspect.core.json.import_json

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import com.karandaev.geo_inspect.core.util.other.toast
import kotlinx.coroutines.launch

@Composable
fun <T> rememberDirectoryImporter(
  importFromDirectory: suspend (Context, Uri) -> T,
  onImport: suspend (Context, T) -> Unit,
  successMessage: String,
  failureMessagePrefix: String = "Import failed",
  onImportingChange: (Boolean) -> Unit = {}
): () -> Unit {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  val currentImportFromDirectory by rememberUpdatedState(importFromDirectory)
  val currentOnImport by rememberUpdatedState(onImport)
  val currentSuccessMessage by rememberUpdatedState(successMessage)
  val currentFailureMessagePrefix by rememberUpdatedState(failureMessagePrefix)
  val currentOnImportingChange by rememberUpdatedState(onImportingChange)

  val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.OpenDocumentTree()
  ) { uri: Uri? ->
    if (uri == null) return@rememberLauncherForActivityResult

    coroutineScope.launch {
      currentOnImportingChange(true)

      runCatching {
        val payload = currentImportFromDirectory(
          context,
          uri
        )

        currentOnImport(
          context,
          payload
        )
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
    launcher.launch(null)
  }
}