package com.karandaev.geo_inspect.core.json.export_json

import android.content.Context
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

@Composable
fun <T> rememberDirectoryExporter(
  exportToDirectory: suspend (Context, Uri, T) -> Unit,
  successMessage: String,
  failureMessagePrefix: String = "Export failed",
  onExportingChange: (Boolean) -> Unit = {}
): (T) -> Unit {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  val currentExportToDirectory by rememberUpdatedState(exportToDirectory)
  val currentSuccessMessage by rememberUpdatedState(successMessage)
  val currentFailureMessagePrefix by rememberUpdatedState(failureMessagePrefix)
  val currentOnExportingChange by rememberUpdatedState(onExportingChange)

  var pendingExportItem by remember {
    mutableStateOf<T?>(null)
  }

  val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.OpenDocumentTree()
  ) { uri: Uri? ->
    val item = pendingExportItem

    if (uri == null || item == null) {
      pendingExportItem = null
      return@rememberLauncherForActivityResult
    }

    coroutineScope.launch {
      currentOnExportingChange(true)

      runCatching {
        currentExportToDirectory(
          context,
          uri,
          item
        )
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
    launcher.launch(null)
  }
}