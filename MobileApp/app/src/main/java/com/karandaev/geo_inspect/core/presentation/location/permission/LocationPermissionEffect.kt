package com.karandaev.geo_inspect.core.presentation.location.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

/**
 * Requests Android location permission when [requestKey] changes.
 *
 * The caller owns when to request permission. This effect only launches the Android
 * permission dialog and reports the result back through [onPermissionResult].
 *
 * @param requestKey Monotonic value that triggers a new permission request when changed.
 * @param onPermissionResult Called after the permission dialog returns a result.
 */
@Composable
fun LocationPermissionRequestEffect(
  requestKey: Int,
  onPermissionResult: () -> Unit
) {
  val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestMultiplePermissions(),
    onResult = {
      onPermissionResult()
    }
  )

  LaunchedEffect(requestKey) {
    if (requestKey <= 0) return@LaunchedEffect

    launcher.launch(
      arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
      )
    )
  }
}

/**
 * Finds an [Activity] from a [Context] when the current Compose context is wrapped.
 */
fun Context.findActivity(): Activity? {
  var currentContext = this

  while (currentContext is ContextWrapper) {
    if (currentContext is Activity) {
      return currentContext
    }

    currentContext = currentContext.baseContext
  }

  return null
}