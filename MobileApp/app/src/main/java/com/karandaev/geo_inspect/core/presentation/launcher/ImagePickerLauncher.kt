package com.karandaev.geo_inspect.core.presentation.launcher

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext

/**
 * Remembers common Android image picker launcher.
 *
 * @param onImageSelected Called when image picker returns URI.
 */
@Composable
internal fun rememberImagePickerLauncher(
  onImageSelected: (Context, Uri?) -> Unit
): ImagePickerLauncher {
  val context = LocalContext.current
  val currentOnImageSelected by rememberUpdatedState(onImageSelected)

  val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
  ) { uri ->
    currentOnImageSelected(
      context,
      uri
    )
  }

  return remember(launcher) {
    ImagePickerLauncher(
      launcher = launcher
    )
  }
}

/**
 * Stable image picker launcher wrapper.
 */
@Stable
internal class ImagePickerLauncher(
  private val launcher: ManagedActivityResultLauncher<String, Uri?>
) {

  /**
   * Opens system image picker.
   */
  fun launchImagePicker() {
    launcher.launch(ImageMimeType)
  }
}