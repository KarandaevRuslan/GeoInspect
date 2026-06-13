package com.karandaev.geo_inspect.core.util.formatters

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val LastUpdatedDatePattern = "d MMMM yyyy"

/**
 * Formats a timestamp into a localized last-updated date string.
 *
 * @param timestampMillis Unix timestamp in milliseconds.
 * @return Localized date string.
 */
fun formatLastUpdatedAt(timestampMillis: Long): String {
  val formatter = SimpleDateFormat(LastUpdatedDatePattern, Locale.getDefault())
  return formatter.format(Date(timestampMillis))
}