package com.karandaev.geo_inspect.app.navigation

/**
 * Application navigation route constants and route builders.
 *
 * Coordinates for the create-note route are passed as strings to avoid precision loss.
 * Jetpack Navigation does not provide a dedicated Double argument type.
 */
object Routes {
  const val AUTH = "auth"

  const val AUTH_LOGIN = "$AUTH/login"
  const val AUTH_REGISTER = "$AUTH/register"
  const val AUTH_PASSWORD_RESET = "$AUTH/password-reset"

  const val DETECT = "detect"
  const val PROFILE = "profile"

  const val HOME = "home"
  const val MAP = "map"
  const val SETTINGS = "settings"

  const val CREATE = "create"
  const val VIEW = "view"
  const val EDIT = "edit"

  const val ARG_LAT = "lat"
  const val ARG_LON = "lon"
  const val ARG_INSPECTION_REPORT_ID = "inspectionReportId"

  const val CREATE_PATTERN = "$CREATE?$ARG_LAT={$ARG_LAT}&$ARG_LON={$ARG_LON}"
  const val VIEW_PATTERN = "$VIEW/{$ARG_INSPECTION_REPORT_ID}"
  const val EDIT_PATTERN = "$EDIT/{$ARG_INSPECTION_REPORT_ID}"

  /**
   * Builds a route for the note creation screen.
   *
   * If coordinates are not provided, the route is returned without query parameters.
   */
  fun create(
    latitude: Double? = null,
    longitude: Double? = null
  ): String {
    if (latitude == null || longitude == null) return CREATE

    return "$CREATE?$ARG_LAT=$latitude&$ARG_LON=$longitude"
  }

  /**
   * Builds a route for the note details screen.
   */
  fun viewNote(noteId: Long): String {
    return "$VIEW/$noteId"
  }

  /**
   * Builds a route for the note editing screen.
   */
  fun editNote(noteId: Long): String {
    return "$EDIT/$noteId"
  }
}