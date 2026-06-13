package com.karandaev.geo_inspect.app.navigation

import androidx.navigation.NavBackStackEntry

/**
 * Returns an optional double navigation argument.
 *
 * Missing, blank, and invalid values are treated as null.
 */
fun NavBackStackEntry.getOptionalDoubleArgument(name: String): Double? {
  return arguments
    ?.getString(name)
    ?.takeIf { value -> value.isNotBlank() }
    ?.toDoubleOrNull()
}

/**
 * Returns an optional report id.
 *
 * Use this when the current route may not contain a report id.
 */
fun NavBackStackEntry.getOptionalInspectionReportId(): Long? {
  return arguments?.getLong(Routes.ARG_INSPECTION_REPORT_ID)
}