package com.karandaev.geo_inspect.core.location.permission_checker

import android.Manifest
import android.app.Activity
import androidx.core.app.ActivityCompat

/**
 * Checks whether Android recommends showing a rationale before requesting location permission again.
 */
object LocationPermissionRationaleChecker {

  fun shouldShowLocationPermissionRationale(activity: Activity): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(
      activity,
      Manifest.permission.ACCESS_FINE_LOCATION
    ) || ActivityCompat.shouldShowRequestPermissionRationale(
      activity,
      Manifest.permission.ACCESS_COARSE_LOCATION
    )
  }
}