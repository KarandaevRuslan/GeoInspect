package com.karandaev.geo_inspect.feature.presentation.create.state

internal object CreateInspectionReportFormSavedStateKeys {
  const val INITIALIZED = "initialized"
  const val TITLE = "title"
  const val CONTENT = "content"
  const val HAS_SELECTED_POINT = "has_selected_point"
  const val SELECTED_POINT_LATITUDE = "selected_point_latitude"
  const val SELECTED_POINT_LONGITUDE = "selected_point_longitude"
  const val MAP_FOCUS_REQUEST = "map_focus_request"
  const val LOADED_INSPECTION_REPORT_WAS_APPLIED = "loaded_inspection_report_was_applied"
  const val DETECTION_MARKED_FOR_REMOVAL = "detection_marked_for_removal"
  const val DETECTION_IMAGE_PATH = "detection_image_path"
  const val APPLIED_DETECTION_REFRESH_ID = "applied_detection_refresh_id"
}