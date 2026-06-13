package com.karandaev.geo_inspect.core.ui.components.map.marker.utils

private const val DEFAULT_MARKER_SNIPPET_LENGTH = 80

/**
 * Produces a compact single-line preview for marker descriptions.
 */
fun noteSnippet(content: String): String {
  return content
    .replace("\n", " ")
    .take(DEFAULT_MARKER_SNIPPET_LENGTH)
}