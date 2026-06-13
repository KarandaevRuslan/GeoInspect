package com.karandaev.geo_inspect.core.location.permission_checker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * Checks Android location permissions.
 */
object LocationPermissionChecker {

  /**
   * Returns true if precise location permission is granted.
   */
  fun hasFineLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
      context,
      Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
  }

  /**
   * Returns true if either fine or coarse location permission is granted.
   */
  fun hasLocationPermission(context: Context): Boolean {
    val finePermission = ContextCompat.checkSelfPermission(
      context,
      Manifest.permission.ACCESS_FINE_LOCATION
    )

    val coarsePermission = ContextCompat.checkSelfPermission(
      context,
      Manifest.permission.ACCESS_COARSE_LOCATION
    )

    return finePermission == PackageManager.PERMISSION_GRANTED ||
      coarsePermission == PackageManager.PERMISSION_GRANTED
  }
}