package com.karandaev.geo_inspect.feature.ui.components.maps

internal const val NOTE_MARKER_PREFIX = "note:"
internal const val CURRENT_LOCATION_MARKER_ID = "current-location"
internal const val SELECTED_LOCATION_MARKER_ID = "selected-location"

internal fun noteMarkerId(noteId: Long): String {
  return "$NOTE_MARKER_PREFIX$noteId"
}

internal fun String.toNoteIdOrNull(): Long? {
  if (!startsWith(NOTE_MARKER_PREFIX)) return null

  return removePrefix(NOTE_MARKER_PREFIX).toLongOrNull()
}